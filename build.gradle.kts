import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij") version "1.17.0"
}

// Getting values defined inside gradle.properties
fun properties(key: String) = project.findProperty(key).toString()

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set(properties("platformVersion"))
    updateSinceUntilBuild.set(false)
    type.set(properties("platformType")) // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17) // https://kotlinlang.org/docs/gradle-compiler-options.html#types-for-compiler-options
    }

    patchPluginXml {
        sinceBuild.set(properties("pluginSinceBuild"))
        //untilBuild.set("232.*")
    }

    signPlugin { // https://plugins.jetbrains.com/docs/intellij/plugin-signing.html
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin { // https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}