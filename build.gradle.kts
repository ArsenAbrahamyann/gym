plugins {
    java
    checkstyle
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}



dependencies {
    // Spring dependencies
    implementation("org.springframework:spring-core:6.0.8")
    implementation("org.springframework:spring-beans:6.0.8")
    implementation("org.springframework:spring-context:6.0.8")
    implementation("org.springframework:spring-webmvc:6.0.8")
    implementation("org.springframework:spring-jdbc:6.0.8")
    implementation("org.springframework:spring-tx:6.0.8")
    implementation("org.springframework:spring-orm:6.0.8")
    implementation("org.springframework.data:spring-data-rest-webmvc:3.7.0") // Corrected version

    // Jetty dependencies for embedded server
    implementation("org.eclipse.jetty:jetty-server:11.0.0")
    implementation("org.eclipse.jetty:jetty-servlet:11.0.0")

    // OpenAPI (Swagger) for documentation
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

    // Hibernate dependencies
    implementation("org.hibernate:hibernate-core:6.1.7.Final") // Updated to Hibernate 6.x
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0") // Use Jakarta Persistence API
    implementation("org.flywaydb:flyway-core:9.0.0")

    // PostgreSQL Driver
    runtimeOnly("org.postgresql:postgresql:42.6.0")

    // SLF4J for logging
    implementation("org.slf4j:slf4j-api:2.0.0")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.0")

    // ModelMapper for object mapping
    implementation("org.modelmapper:modelmapper:3.2.0")

    // Lombok for reducing boilerplate code
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")

    // Javax Annotation API
    implementation("jakarta.annotation:jakarta.annotation-api:1.3.5")

    // Testing dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("com.h2database:h2:2.1.214")
}


tasks.test {
    useJUnitPlatform()
}

checkstyle {
    isIgnoreFailures = false
    maxWarnings = 0
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}