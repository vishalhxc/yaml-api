import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.30"
}

group "org.gokapio"
version "0.0.1"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

dependencies {
    fun String.version(): String = properties["dependency.version.$this"].toString()
    implementation("io.ktor:ktor-client-serialization:${"ktor".version()}")
    implementation("io.ktor:ktor-client-cio:${"ktor".version()}")
    implementation("ch.qos.logback:logback-classic:${"logback".version()}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${"kotlinx".version()}")
    implementation("org.snakeyaml:snakeyaml-engine:${"snakeyaml".version()}")
    testImplementation("io.kotest:kotest-runner-junit5:${"kotest".version()}")
    testImplementation("io.kotest:kotest-assertions-core:${"kotest".version()}")
    testImplementation("io.kotest:kotest-property:${"kotest".version()}")
    testImplementation("io.mockk:mockk:${"mockk".version()}")
}

tasks {
    withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" }
    withType<Test> { useJUnitPlatform() }
}