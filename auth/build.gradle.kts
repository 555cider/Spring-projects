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
val postgresqlVersion = "42.7.3"

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$springCloudVersion")
    // Web
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    // PostgreSQL
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
}
