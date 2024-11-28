pluginManagement {
    repositories {
    }
}

plugins {
//    id("org.gradle.toolchains")
}

rootProject.name = "spring-projects"

include("discovery")
include("gateway")
include("auth")
include("client")
