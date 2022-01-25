import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    java
    kotlin("jvm") version "1.5.30"
}

group = "org.example"
version = "0.1.3"

repositories {
    mavenCentral()
    jcenter()
}

tasks.withType(KotlinJvmCompile::class.java) {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    api("net.mamoe", "mirai-core", "2.9.2")
    implementation("com.alibaba:fastjson:1.2.79")
}