import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.30"
}

group "io.yamelback"
version "0.0.1"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

dependencies {
    fun versionOf(name: String): String = properties["dependency.version.$name"].toString()
    implementation("io.ktor:ktor-client-serialization:${versionOf("ktor")}")
    implementation("io.ktor:ktor-client-cio:${versionOf("ktor")}")
    implementation("ch.qos.logback:logback-classic:${versionOf("logback")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${versionOf("kotlinx")}")
    implementation("org.snakeyaml:snakeyaml-engine:${versionOf("snakeyaml")}")
    testImplementation("io.kotest:kotest-runner-junit5:${versionOf("kotest")}")
    testImplementation("io.kotest:kotest-assertions-core:${versionOf("kotest")}")
    testImplementation("io.kotest:kotest-property:${versionOf("kotest")}")
    testImplementation("io.mockk:mockk:${versionOf("mockk")}")
    testImplementation("io.ktor:ktor-client-mock:${versionOf("ktor")}")
}

tasks {
    withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" }
    withType<Test> { useJUnitPlatform() }
}