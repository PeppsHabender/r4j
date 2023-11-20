/**
 * MIT License
 *
 * Copyright (c) 2023 PeppsHabender
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.peppshabender.r4j.gradle

import de.peppshabender.r4j.gradle.utils.R4JConstants.R4J_STR
import de.peppshabender.r4j.gradle.utils.R4JConstants.R4J_CONFIG_STR
import de.peppshabender.r4j.gradle.utils.normalizeClassName
import de.peppshabender.r4j.gradle.utils.r4jConfig
import de.peppshabender.r4j.gradle.utils.r4jModuleDir
import de.peppshabender.r4j.gradle.utils.sourceSets
import de.peppshabender.r4j.gradle.spi.IResourceFileBuilder
import de.peppshabender.r4j.gradle.spi.R
import de.peppshabender.r4j.gradle.spi.R.ResourceContainer
import de.peppshabender.r4j.gradle.utils.R4JConstants
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.writeText


/**
 * Plugin entry point, registers the extension, task and adds all source sets r4j will
 * write to.
 */
class R4JPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        this.extensions.add(R4J_CONFIG_STR, R4JConfig::class.java)
        val r4jTask: TaskProvider<R4JTask> = this.tasks.register(R4J_STR, R4JTask::class.java)

        this.dependencies.add(IMPLEMENTATION_STR, DEPENDENCY_FORMAT.format("api"))

        afterEvaluate { project ->
            val config: R4JConfig = project.r4jConfig

            project.tasks.findByName("processResources")?.finalizedBy(R4J_STR)
            project.sourceSets.asMap.values.forEach { source ->
                source.allJava.srcDirs.filter {
                    project.buildDir.absolutePath !in it.absolutePath
                }.forEach {
                    source.java.srcDir(r4jModuleDir(it.parentFile.name, config))
                }
            }

            if(config.builder == R4JConfig.JAVA_BUILDER) {
                project.tasks.findByName("compileJava")?.dependsOn(r4jTask)
            } else if(config.builder == R4JConfig.KOTLIN_BUILDER) {
                project.tasks.findByName("compileKotlin")?.dependsOn(r4jTask)
            }
        }
    }

    companion object {
        private const val IMPLEMENTATION_STR: String = "implementation"
        private val DEPENDENCY_FORMAT: String = "de.peppshabender.r4j:r4j-%s:${R4JConstants.R4J_VERSION}"
    }
}

/**
 * Main entry point for the incremental r4j task that scans all resource folders and
 * collects the resources using the selected builder and writes that to the build dir.
 */
open class R4JTask : DefaultTask() {
    private val config: R4JConfig = this.project.r4jConfig

    @OutputDirectory
    val output: Path = this.project.buildDir
        .resolve("generated").resolve("sources").resolve(R4J_STR).toPath()

    @InputFiles
    val resourceDirs: List<Path> = this.project.sourceSets.asMap.values.filter {
        it.name !in this.config.excludedSourceSets
    }.flatMap { source ->
        source.allJava.srcDirs.filter {
            // We already added those to the source sets
            this.project.buildDir.absolutePath !in it.absolutePath
        }.map(File::toPath).map {
            // Fetch the respective resource folder
            it.resolveSibling("resources")
        }
    }

    init {
        this.group = R4J_STR
        this.description = "Generates resource classes."
    }

    @TaskAction
    fun execute() {
        val config: R4JConfig = this.project.r4jConfig

        this.resourceDirs.filter(Path::exists).map(::ResourceContainer).filterNot(ResourceContainer::isEmpty).forEach {
            // At this point we only have non-empty resource directories
            // Module name is automatically the parent
            val moduleName: String = it.path.parent.name
            // Outer class is just the combination of the module and the configured name
            val className: String = (moduleName.capitalized() + config.className).normalizeClassName()

            // Provide us with the builder
            val builder: IResourceFileBuilder = config.builder.provide(className, config)

            // Add all resources
            it.addResourcesTo(builder)

            this.project.r4jModuleDir(moduleName, config) // Build output
                .resolve(config.pkg.replace('.', File.separatorChar)) // Write package
                .createDirectories() // Create if it doesnt exist
                .resolve("$className.${builder.fileExtension}") // Resolve the wanted file
                .also(Path::deleteIfExists) // Delete should it exist
                .createFile().writeText(builder.build()) // Aaand write the built class
        }
    }

    private fun ResourceContainer.addResourcesTo(builder: IResourceFileBuilder) {
        forEach {
            when(it) {
                is R.Resource -> builder.addResource(it)
                is ResourceContainer -> it.addResourcesTo(
                    // Nested resources simply keep their folder name
                    builder.addNested(it.path.name.capitalized().normalizeClassName())
                )
            }
        }
    }
}