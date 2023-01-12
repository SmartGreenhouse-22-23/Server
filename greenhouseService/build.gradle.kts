group = "it.unibo.smartgh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(project(":common"))
    testImplementation(project(":clientCommunicationGateway"))
    testImplementation(project(":brightnessService"))
    testImplementation(project(":humidityService"))
    testImplementation(project(":soilMoistureService"))
    testImplementation(project(":temperatureService"))
    testImplementation(project(":operationService"))
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "it.unibo.smartgh.greenhouse.GreenhouseServiceLauncher"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}