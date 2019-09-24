import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
    application
}

version = "1.0-SNAPSHOT"

application {
    mainClassName = "eventer.MainKt"
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

repositories {
    mavenCentral()
    jcenter()
}

object Versions {
    val ktor = "1.2.4"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback", "logback-classic", "1.2.3")

    implementation("io.github.microutils", "kotlin-logging", "1.5.9")

    implementation("com.sksamuel.hoplite", "hoplite-core", "1.0.3")
    implementation("com.sksamuel.hoplite", "hoplite-hocon", "1.0.8")

    implementation("org.jetbrains.exposed", "exposed", "0.17.3")
    implementation("org.postgresql", "postgresql", "42.2.2")
    implementation("org.flywaydb", "flyway-core", "6.0.4")

    implementation("io.ktor", "ktor-server-netty", Versions.ktor)
    implementation("io.ktor", "ktor-server-core", Versions.ktor)
    implementation("io.ktor", "ktor-auth", Versions.ktor)
    implementation("io.ktor", "ktor-mustache", Versions.ktor)

    implementation("org.mindrot", "jbcrypt", "0.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}