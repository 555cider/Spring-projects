plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.2.5"
}

group = "com.example"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val springBootVersion by extra { "3.2.5" }
val springCloudVersion by extra { "4.1.1" }
val r2dbcPostgresqlVersion by extra { "1.0.4.RELEASE" }

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${springCloudVersion}")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-webflux:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springBootVersion}")

    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:${springBootVersion}")
    // R2DBC Postgresql
    implementation("org.postgresql:r2dbc-postgresql:${r2dbcPostgresqlVersion}")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    testImplementation("io.projectreactor:reactor-test:3.6.4")
    testImplementation("com.google.code.gson:gson:2.8.9")
}
