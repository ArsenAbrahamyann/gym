plugins {
    id("java")

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
    implementation("org.springframework:spring-context:5.3.28")
    implementation("org.springframework:spring-beans:5.3.28")
    implementation("org.slf4j:slf4j-api:1.7.36")

    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("org.assertj:assertj-core:3.21.0")
}
sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/test/java"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}


