plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

val springCloudVersion = "4.1.1"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:${springCloudVersion}")
}
