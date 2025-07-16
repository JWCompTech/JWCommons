// 🔝 1. Plugins
plugins {
    java
    `maven-publish`
    signing
    application
    id("org.openjfx.javafxplugin") version "0.1.0" // newer version of the JavaFX Maven Plugin
    id("org.beryx.jlink") version "2.25.0" // optional, helps with JavaFX bundling
}

// 📦 2. Group, Version and Properties
group = "com.jwcomptech.commons"
version = "1.0.0-alpha"

val javafxVersion by extra("21.0.7")
val mavenLocalRepo by extra(findProperty("mavenLocalPath")
    ?: System.getenv("MAVEN_LOCAL_REPO")
    ?: "${System.getProperty("user.home")}/.m2/repository")

// 🧰 3. Toolchains and Java setup
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withJavadocJar()
    withSourcesJar()
}

// 🚀 4. Application config
application {
    mainClass.set("com.jwcomptech.commons.Main")
}

// 📡 5. Repositories
repositories {
    mavenCentral()
    maven {
        name = "Repsy"
        url = uri("https://repo.repsy.io/mvn/jwcomptech/public")
    }
    gradlePluginPortal()
}

// 📚 6. Dependencies
allprojects {
    dependencyLocking {
        lockAllConfigurations()
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    implementation("org.jetbrains:annotations:26.0.2")
    // https://mvnrepository.com/artifact/net.java.dev.jna/jna-platform-jpms
    implementation("net.java.dev.jna:jna-platform-jpms:5.17.0")
    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.18.0")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.19.0")
    // https://mvnrepository.com/artifact/commons-codec/commons-codec
    implementation("commons-codec:commons-codec:1.18.0")
    // https://mvnrepository.com/artifact/de.svenkubiak/jBCrypt
    implementation("de.svenkubiak:jBCrypt:0.4.3")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:2.0.17")
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.18")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.1")
    // https://mvnrepository.com/artifact/org.codehaus.plexus/plexus-utils
    implementation("org.codehaus.plexus:plexus-utils:4.0.2")
    // https://mvnrepository.com/artifact/org.apache.maven/maven-model
    implementation("org.apache.maven:maven-model:4.0.0-rc-4")
    // https://mvnrepository.com/artifact/io.vavr/vavr
    implementation("io.vavr:vavr:0.10.6")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    // https://mvnrepository.com/artifact/org.kohsuke/github-api
    implementation("org.kohsuke:github-api:2.0-rc.3")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.1")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.19.1")
    // https://mvnrepository.com/artifact/org.openjfx/javafx-fxml
    implementation("org.openjfx:javafx-fxml:$javafxVersion")
    // https://mvnrepository.com/artifact/org.openjfx/javafx-web
    implementation("org.openjfx:javafx-web:$javafxVersion")
    // https://mvnrepository.com/artifact/org.semver4j/semver4j
    implementation("org.semver4j:semver4j:6.0.0")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot
    implementation("org.springframework.boot:spring-boot:3.5.3")
    // https://mvnrepository.com/artifact/org.fusesource.jansi/jansi
    implementation("org.fusesource.jansi:jansi:2.4.2")
    // https://mvnrepository.com/artifact/org.ow2.asm/asm
    implementation("org.ow2.asm:asm:9.8")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.3")
    // https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:4.0.0-M1")
    // https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.13.3")
}

// 🎨 7. JavaFX config
javafx {
    version = javafxVersion
    modules = listOf("javafx.fxml", "javafx.web")
}

// 🧪 8. Test config
tasks.test {
    useJUnitPlatform()
    jvmArgs(
        "--add-exports=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED",
        "--add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED",
        "--add-exports=javafx.graphics/com.sun.glass.ui=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.stage=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED"
    )
}

// 📦 9. Publishing config
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.jwcomptech.commons"
            artifactId = "jwcommons"
            version = "1.0.0-alpha"

            pom {
                name.set("JWCommons")
                description.set("Utility functions and classes for all JWCT projects.")
                url.set("https://github.com/JWCompTech/JWCommons")
                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
                    }
                }
                developers {
                    developer {
                        id.set("jlwisedev")
                        name.set("Joshua Wise")
                        email.set("jlwise@jwcomptech.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/JWCompTech/JWCommons.git")
                    developerConnection.set("scm:git:ssh://git@github.com/JWCompTech/JWCommons.git")
                    url.set("https://github.com/JWCompTech/JWCommons")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Repsy"
            url = uri("https://repo.repsy.io/mvn/jwcomptech/public")
            credentials {
                username = findProperty("repsyUsername") as String?
                    ?: System.getenv("REPSY_USERNAME")
                password = findProperty("repsyPassword") as String?
                    ?: System.getenv("REPSY_PASSWORD")
            }
        }

        // Simulate `mvn install` into ~\.m2\repository or custom path
        maven {
            name = "localMaven"
            url = uri("$mavenLocalRepo")
        }
    }
}

// 🔐 10. Signing config
signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

// 💡 11. Tasks
tasks.register<EnforceModularity>("checkJavaFxUsage") {
    group = "verification"
    description = "Ensures JavaFX APIs are only used inside the javafx package"

    // Make it run after classes are compiled
    dependsOn(tasks.named("classes"))

    // Set input directory to the compiled output
    classesDir.set(layout.buildDirectory.dir("classes/java/main"))

    allowedPackages.set(listOf("com.jwcomptech.commons.javafx"))
    forbiddenPrefixes.set(listOf("javafx."))
    allowedViolations.set(
        listOf(
            "com.jwcomptech.commons.resources.enums.ResourceType",
            "com.jwcomptech.commons.resources.Resource",
            "com.jwcomptech.commons.Main"
        )
    )
}

tasks.register<EnforceModularity>("checkSpringUsage") {
    group = "verification"
    description = "Checks for illegal usage of Spring classes outside allowed packages"

    // Make it run after classes are compiled
    dependsOn(tasks.named("classes"))

    // Set input directory to the compiled output
    classesDir.set(layout.buildDirectory.dir("classes/java/main"))

    allowedPackages.set(listOf("com.jwcomptech.commons.spring"))
    forbiddenPrefixes.set(listOf(
        "org.springframework.",
        "org.springframework.boot."
    ))
    allowedViolations.set(listOf(
        "com.jwcomptech.commons.logging.LoggingManager",
        "com.jwcomptech.commons.properties.PropertiesUtils",
        "com.jwcomptech.commons.properties.PropertyLoader"
    ))
}

tasks.named("check") {
    dependsOn("checkJavaFxUsage")
    dependsOn("checkSpringUsage")
}

tasks.register("mavenInstallToDev") {
    group = "publishing"
    description = "Installs artifact to Maven-style local repo at $mavenLocalRepo"

    dependsOn("publishMavenJavaPublicationToLocalMavenRepository")
}

tasks.register("mavenInstallToRepsy") {
    group = "publishing"
    description = "Installs artifact to Maven-style Repsy repo"

    dependsOn("publishMavenJavaPublicationToRepsyRepository")
}

tasks.register("deployAll") {
    group = "publishing"
    description = "Deploy to local Maven repo and then to Repsy"

    dependsOn(
        "mavenInstallToDev",
        "mavenInstallToRepsy",
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(
        listOf(
            "-Xlint:unchecked",
            "-XDcompilePolicy=simple",
            "--should-stop=ifError=FLOW",
            "-parameters"
        )
    )
    options.isDeprecation = true       // showDeprecation
    options.isWarnings = true          // showWarnings
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            "Main-Class" to "com.jwcomptech.commons.Main",
            "Class-Path" to configurations.runtimeClasspath.get()
                .joinToString(" ") { "lib/${it.name}" }
        )

        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "JWCompTech",
            "Specification-Title" to project.name,
            "Specification-Version" to project.version,
            "Specification-Vendor" to "JWCompTech"
        )
    }
}

tasks.withType<Javadoc>().configureEach {
    (options as? StandardJavadocDocletOptions)?.apply {
        tags(
            "implNote:a:Implementation Note:",
            "apiNote:a:API Note:",
            "implSpec:a:Implementation Requirements:"
        )
    }
}
