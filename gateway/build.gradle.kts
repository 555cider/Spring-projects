plugins {
    id("java")
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
val postgresqlVersion by extra { "42.7.3" }
val jjwtVersion by extra { "0.12.5" }

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${springCloudVersion}")

    // Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:${springCloudVersion}")
    // Reactive Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:${springBootVersion}")
    // Security
    implementation("org.springframework.boot:spring-boot-starter-security:${springBootVersion}")

    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:${springBootVersion}")
    // R2DBC Postgresql
    implementation("org.postgresql:r2dbc-postgresql:${r2dbcPostgresqlVersion}")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

    // encrypt secrets
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}
