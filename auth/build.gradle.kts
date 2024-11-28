plugins {
    id("java")
    id("org.springframework.boot") version "3.2.5"
}

group = "com.example"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

val springBootVersion by extra { "3.3.5" }
val springCloudVersion by extra { "4.1.3" }
val r2dbcPostgresqlVersion by extra { "1.0.4.RELEASE" }

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${springCloudVersion}")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-webflux:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-security:${springBootVersion}")

    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server:${springBootVersion}")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:${springBootVersion}")
    implementation("org.postgresql:r2dbc-postgresql:${r2dbcPostgresqlVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:${springBootVersion}")

    implementation("org.modelmapper:modelmapper:3.2.1")
}
