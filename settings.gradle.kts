// [optional] define the plugins your project uses.
pluginManagement {
    repositories {
//        gradlePluginPortal()
//        google()
    }
}

// [optional] declare the plugins which only affect the Settings object.
plugins {
//    id("org.gradle.toolchains")
}

// defines your project name
rootProject.name = "spring-projects"

// [optional] declare the repositories your project relies on.
dependencyResolutionManagement {
    repositories {
//        mavenCentral()
    }
}

// defines the structure of the project by adding all the subprojects
include("discovery")
include("gateway")
include("auth")
