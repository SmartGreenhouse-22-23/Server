plugins {
    java
    jacoco
}

group = "it.unibo.smartgh"
version = "0.1.0"

jacoco {
    toolVersion = "0.8.13"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "it.unibo.smartgh.operation.OperationServiceLauncher"
    }
}


