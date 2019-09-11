/*
 * MIT License
 *
 * Copyright (c) 2019 Oluwatobi Adeyinka
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
 *
 */

import org.yaml.snakeyaml.Yaml
import org.jetbrains.dokka.gradle.DokkaTask
import org.jenkinsci.gradle.plugins.jpi.JpiDeveloper
import org.jenkinsci.gradle.plugins.jpi.JpiLicense
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.load.kotlin.JvmType

val dist: String = "dist"
val webAppDir: String = "src/main/webapp"
val npmInstall: String = "npmInstall"
val webAppDev: String = "webAppDev"
val webAppDist: String = "webAppDist"
val webAppClean: String = "webAppClean"
val webAppTasksGroup: String = "Webapp"
val generateDocumentation: String = "generateDocumentation"
val deleteDocumentation: String = "deleteDocumentation"
val deleteWorkDir: String = "deleteWorkDir"
val deleteBuiltPackage: String = "deleteBuiltPackage"

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("org.yaml:snakeyaml:1.24")
    }
}

plugins {
    java
    kotlin("jvm") version "1.3.31"
    kotlin("kapt") version "1.3.31"
    id("org.jenkins-ci.jpi") version "0.28.1"
    id("org.jetbrains.dokka") version "0.9.17"
}

val buildInfo: MutableMap<String, JvmType.Object> = (Yaml().load(
    File("src/main/resources/build.yml").inputStream()
) as MutableMap<String, JvmType.Object>)
val build: MutableMap<String, JvmType.Object> = buildInfo["build"] as MutableMap<String, JvmType.Object>

group = "me.tobiadeyinka.jenkinsci.plugins"
version = build["version"].toString()
description = "A dashboard for monitoring the status of builds"

jenkinsPlugin {
    coreVersion = "2.164.3"
    displayName = "Build Dashboard Plugin"
    url = "https://github.com/beverlyRoadGoose/jenkins-build-dashboard"
    gitHubUrl = "https://github.com/beverlyRoadGoose/jenkins-build-dashboard"
    shortName = "build-dashboard-plugin"
    fileExtension = "jpi"

    developers = this.Developers().apply {
        developer(delegateClosureOf<JpiDeveloper> {
            setProperty("id", "beverlyRoadGoose")
            setProperty("name", "Oluwatobi Adeyinka")
            setProperty("email", "adeyinka.oluwatobi@yahoo.com")
        })
    }

    licenses = this.Licenses().apply {
        license(delegateClosureOf<JpiLicense> {
            setProperty("name", "MIT License")
            setProperty("url", "https://opensource.org/licenses/MIT")
        })
    }
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
    group = webAppTasksGroup
    description = "Install webapp npm dependencies"
    commandLine = listOf("npm", "install", "--prefix", webAppDir)
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

val deleteBuiltPackageTask by tasks.register<Exec>(deleteBuiltPackage) {
    group = "Build"
    description = "Deletes the  build-dashboard-plugin.jpi file"
    commandLine = listOf("rm", "-f", "build-dashboard-plugin.jpi")
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

val distTask by tasks.register<Exec>(dist) {
    group = "Build"
    description = "Generates a jpi file of the plugin that can be installed on a Jenkins instance"
    dependsOn(jpiTask)
    mustRunAfter(jpiTask)
    commandLine = listOf("cp", "build/libs/build-dashboard-plugin.jpi", ".")
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
    dependsOn(deleteBuiltPackage)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kapt.includeCompileClasspath = false

dependencies {
    /*
     * supported jenkins plugins
     */
    optionalJenkinsPlugins("com.sonyericsson.hudson.plugins.rebuild:rebuild:1.31")
    optionalJenkinsPlugins("org.jenkins-ci.plugins.workflow:workflow-aggregator:2.4")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    implementation("org.yaml:snakeyaml:1.24")

    kapt("net.java.sezpoz:sezpoz:1.13")

    testImplementation("junit", "junit", "4.12")
    testImplementation("io.mockk:mockk:1.9")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
