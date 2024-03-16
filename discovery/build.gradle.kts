plugins {
    id("java")
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

val springCloudVersion by extra { "4.1.0" }

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:$springCloudVersion")
}
