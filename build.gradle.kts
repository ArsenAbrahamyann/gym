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
    implementation("org.springframework:spring-core:5.3.28") // Update to your Spring version if necessary
    implementation("org.springframework:spring-beans:5.3.28")
    implementation("org.springframework:spring-context:5.3.28")
    implementation("org.springframework:spring-jdbc:5.3.28")
    implementation("org.springframework:spring-tx:5.3.28")

    // Spring ORM for Hibernate integration
    implementation ("org.springframework:spring-orm:5.3.28")

    // Hibernate dependencies
    implementation("org.hibernate:hibernate-core:5.6.15.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.6.15.Final")

    // PostgreSQL Driver
    runtimeOnly("org.postgresql:postgresql:42.6.0")

    // SLF4J
    implementation("org.slf4j:slf4j-api:1.7.36")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")

    // ModelMapper
    implementation("org.modelmapper:modelmapper:3.2.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")

    // Javax Annotation API
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.assertj:assertj-core:3.21.0")
}


tasks.test {
    useJUnitPlatform()
}

checkstyle {
    isIgnoreFailures = false
    maxWarnings = 0
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}