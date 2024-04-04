plugins {
    id("java")
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

val springBootVersion = "3.2.4"
val springCloudVersion = "4.1.1"
val r2dbcPostgresqlVersion = "1.0.4.RELEASE"
val postgresqlVersion = "42.7.3"
val jjwtVersion = "0.12.5"

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$springCloudVersion")

    // Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:$springCloudVersion")
    // Reactive Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:$springBootVersion")
    // Security
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")

    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:$springBootVersion")
    // R2DBC Postgresql
    implementation("org.postgresql:r2dbc-postgresql:${r2dbcPostgresqlVersion}")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

    // encrypt secrets
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}
