import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val hpi: String = "hpi"
val webAppDir: String = "src/main/webapp"
val npmInstall: String = "npmInstall"
val webAppDev: String = "webAppDev"
val webAppDist: String = "webAppDist"
val webAppClean: String = "webAppClean"
val webAppTasksGroup: String = "Webapp"
val generateDocumentation: String = "generateDocumentation"
val deleteDocumentation: String = "deleteDocumentation"
val deleteWorkDir: String = "deleteWorkDir"

plugins {
    java
    kotlin("jvm") version "1.3.31"
    kotlin("kapt") version "1.3.31"
    id("org.jenkins-ci.jpi") version "0.28.1"
    id("org.jetbrains.dokka") version "0.9.17"
}

group = "me.tobiadeyinka.jenkinsci.plugins"
version = "0.1.0"
description = "A dashboard for monitoring the status of builds"

jenkinsPlugin {
    coreVersion = "2.60.3"
    displayName = "Build Dashboard"
    url = "https://github.com/beverlyRoadGoose/jenkins-build-dashboard"
    gitHubUrl = "https://github.com/beverlyRoadGoose/jenkins-build-dashboard"
    shortName = "build-dashboard-plugin"
}

repositories {
    jcenter()
    mavenCentral()
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/javadoc"
}

val npmInstallTask by tasks.register<Exec>(npmInstall) {
    commandLine = listOf("npm", "install", "--prefix", webAppDir)
    group = webAppTasksGroup
    description = "Install webapp npm dependencies"
}

val webAppDevTask by tasks.register<Exec>(webAppDev) {
    group = webAppTasksGroup
    description = "Build development version of webapp"
    dependsOn(npmInstallTask)
    commandLine = listOf("npm", "run", "build", "--prefix", webAppDir)
}

val webAppDistTask by tasks.register<Exec>(webAppDist) {
    group = webAppTasksGroup
    description = "'Build distribution version of webapp"
    dependsOn(npmInstallTask)
    commandLine = listOf("npm", "run", "dist", "--prefix", webAppDir)
}

val webAppCleanTask by tasks.register<Exec>(webAppClean) {
    group = webAppTasksGroup
    description = "Delete generated webapp files"
    commandLine = listOf("rm", "-rf", String.format("%s/webapp/", webAppDir))
}

val deleteDocumentationTask by tasks.register<Exec>(deleteDocumentation) {
    group = "Documentation"
    description = "Deletes the docs dir"
    commandLine = listOf("rm", "-rf", "docs")
}

val generateDocumentationTask by tasks.register<Exec>(generateDocumentation) {
    group = "Documentation"
    description = "Generates new dokka documentation in the docs dir"
    dependsOn(deleteDocumentationTask)
    dependsOn(dokka)
    commandLine = listOf("mv", "build/javadoc/", "docs")
}

val jpiTask by tasks.named<Task>("jpi") {
    dependsOn(webAppDistTask)
    mustRunAfter(webAppDistTask)
}

val hpiTask by tasks.register<Exec>(hpi) {
    group = "Build"
    description = "Generates a hpi file of the plugin that can be installed on a Jenkins instance"
    dependsOn(jpiTask)
    mustRunAfter(jpiTask)
    commandLine = listOf("cp", "build/libs/build-dashboard-plugin.hpi", ".")
}

val deleteWorkDirTask by tasks.register<Exec>(deleteWorkDir) {
    group = "Build"
    description = "Delete jenkins work directory"
    commandLine = listOf("rm", "-rf", "work")
}

tasks.named<Task>("build") {
    dependsOn(webAppDistTask)
    mustRunAfter(webAppDistTask)
}

tasks.named<Task>("server") {
    dependsOn(webAppDevTask)
    mustRunAfter(webAppDevTask)
}

tasks.named<Task>("clean") {
    dependsOn(webAppCleanTask)
    dependsOn(deleteWorkDirTask)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kapt.includeCompileClasspath = false

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

    kapt("net.java.sezpoz:sezpoz:1.13")

    testImplementation("junit", "junit", "4.12")
    testImplementation("io.mockk:mockk:1.9")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
