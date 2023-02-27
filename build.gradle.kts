plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
}

group = "me.golden.trio"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("io.github.classgraph:classgraph:4.8.130")
    implementation(kotlin("reflect"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}