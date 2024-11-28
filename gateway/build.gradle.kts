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

val springBootVersion by extra { "3.2.5" }
val springCloudVersion by extra { "4.1.1" }
val r2dbcPostgresqlVersion by extra { "1.0.4.RELEASE" }

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${springCloudVersion}")

    // Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:${springCloudVersion}")
    // Security
    implementation("org.springframework.boot:spring-boot-starter-security:${springBootVersion}")

    // OAuth
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:${springBootVersion}")

    // DB - Reactive Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:${springBootVersion}")
    // DB - R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:${springBootVersion}")
    // DB - R2DBC Postgresql
    implementation("org.postgresql:r2dbc-postgresql:${r2dbcPostgresqlVersion}")

    // encrypt secrets
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}
