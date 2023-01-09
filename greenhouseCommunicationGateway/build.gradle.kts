plugins {
    id("java")
}

group = "it.unibo.smartgh"
version = "0.1.0-archeo+47042ec"

repositories {
    mavenCentral()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}