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

dependencies {
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$springCloudVersion")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")

    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:$springBootVersion")
    // R2DBC Postgresql
    implementation("org.postgresql:r2dbc-postgresql:${r2dbcPostgresqlVersion}")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("io.projectreactor:reactor-test:3.6.4")
    testImplementation("com.google.code.gson:gson:2.8.9")
}
