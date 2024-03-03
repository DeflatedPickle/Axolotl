plugins {
    application

    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-io:commons-io:2.15.1")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    implementation("com.deflatedpickle:speckle:")
    implementation("com.deflatedpickle:monoconskt:")
    implementation("com.deflatedpickle:marvin:")

    implementation("io.github.andrewauclair:modern-docking-api:0.11.1")
    implementation("io.github.andrewauclair:modern-docking-ui:0.11.1")
    implementation("io.github.andrewauclair:modern-docking-single-app:0.11.1")

    implementation("ch.randelshofer:org.monte.media:17.1")
    implementation("ch.randelshofer:org.monte.media.amigaatari:17.1")

    implementation("org.swinglabs.swingx:swingx-all:1.6.5-1")
    implementation("com.formdev:jide-oss:3.7.14")
    implementation("ch.randelshofer:org.monte.media.swing:17.1")
    implementation("de.huxhorn.sulky:de.huxhorn.sulky.swing:8.3.0")

    implementation("com.formdev:flatlaf:3.3")
    implementation("com.formdev:flatlaf-extras:3.3")
    implementation("com.formdev:flatlaf-swingx:3.3")
    implementation("com.formdev:flatlaf-jide-oss:3.3")

    implementation("com.formdev:flatlaf-intellij-themes:3.3")

    implementation("com.formdev:flatlaf-fonts-inter:4.0")
    implementation("com.formdev:flatlaf-fonts-jetbrains-mono:2.304")
    implementation("com.formdev:flatlaf-intellij-themes:3.3")
    implementation("com.formdev:flatlaf-fonts-roboto:2.137")
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

application {
    mainClass = "com.deflatedpickle.axolotl.MainKt"
}