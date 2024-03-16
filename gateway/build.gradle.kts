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
    // Gateway
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:$springCloudVersion")
    // Eureka Client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$springCloudVersion")
    // Reactive Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:$springBootVersion")
    // disable CSRF protection
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    // encrypt secrets
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}
