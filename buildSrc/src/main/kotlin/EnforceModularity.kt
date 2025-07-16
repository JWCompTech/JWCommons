import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class EnforceModularity : DefaultTask() {

    @get:InputDirectory
    abstract val classesDir: DirectoryProperty

    @get:Input
    abstract val allowedPackages: ListProperty<String>

    @get:Input
    abstract val forbiddenPrefixes: ListProperty<String>

    @get:Input
    abstract val allowedViolations: ListProperty<String>

    init {
        group = "verification"
        description = "Checks for illegal references to forbidden libraries."
        outputs.upToDateWhen { false } // Always run
    }

    @TaskAction
    fun checkModularity() {
        val violations = mutableListOf<String>()

        val classDirFile = classesDir.get().asFile
        if (!classDirFile.exists()) {
            logger.warn("⚠️ No class files found. Skipping ${name}.")
            return
        }

        val allowedViolationsSet = allowedViolations.get().toSet()
        val allowedPackagesSet = allowedPackages.get().toSet()

        classDirFile.walkTopDown()
            .filter { it.extension == "class" }
            .forEach { file ->
                val bytes = file.readBytes()

                forbiddenPrefixes.get().forEach { prefix ->
                    val searchBytes = prefix.replace('.', '/').toByteArray(Charsets.UTF_8)

                    val className = file.relativeTo(classDirFile)
                        .invariantSeparatorsPath
                        .removeSuffix(".class")
                        .replace("/", ".")

                    val foundIndex = bytes.indexOfSubArray(searchBytes)

                    if (foundIndex >= 0) {
                        val isAllowed = allowedPackagesSet.any { className.startsWith(it) }
                        val isException = allowedViolationsSet.any { className.startsWith(it) }

                        if (!isAllowed && !isException) {
                            val normalizedPrefix = prefix.trimEnd('.')
                            val searchPrefix = normalizedPrefix.replace('.', '/')

                            val allStrings = bytes.toString(Charsets.ISO_8859_1).split('\u0000')

                            fun cleanString(str: String): String {
                                val droppedLeading = str.dropWhile { !it.isLetter() }
                                return droppedLeading.takeWhile {
                                    it.isLetterOrDigit() || it == '/' || it == '.' || it == '$' }
                            }

                            val matches = allStrings
                                .map { cleanString(it) }
                                .filter { it.startsWith(searchPrefix) }
                                .distinct()

                            if (matches.isEmpty()) {
                                println("  No specific matches found, adding fallback violation.")
                                violations += "$className illegally references forbidden class $normalizedPrefix.*"
                            } else {
                                matches.forEach { matchedClass ->
                                    val convertedMatchedClass  = matchedClass.replace('/', '.')
                                    println("  Adding violation for forbidden class: $matchedClass")
                                    violations += "$className illegally references forbidden class $convertedMatchedClass"
                                }
                            }
                        }
                    }
                }
            }

        if (violations.isNotEmpty()) {
            logger.error("🚨 $name failed with ${violations.size} violation(s):")
            violations.forEach { logger.error("🔥 $it") }
            throw GradleException("🚨 $name failed with ${violations.size} violation(s). See logs for details.")
        } else {
            logger.lifecycle("✅ $name passed — no violations found.")
        }
    }

    private fun ByteArray.indexOfSubArray(subArray: ByteArray): Int {
        outer@ for (i in 0..this.size - subArray.size) {
            for (j in subArray.indices) {
                if (this[i + j] != subArray[j]) continue@outer
            }
            return i
        }
        return -1
    }
}
