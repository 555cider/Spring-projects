plugins {
    id("java")
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

val springBootVersion = "3.2.3"
val springCloudVersion = "4.1.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$springCloudVersion")
}
