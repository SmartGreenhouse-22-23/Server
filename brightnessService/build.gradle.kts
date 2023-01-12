plugins {
    java
}

group = "it.unibo.smartgh"
version = "0.1.0-archeo+f2c73a7"

repositories {
    mavenCentral()
}

dependencies{
    implementation(project(":common"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


tasks.create<JavaExec>("Start"){
    group = "custom"
    description = "run the service of the Server"

    mainClass.set("it.unibo.smartgh.brightness.BrightnessServiceLauncher")
    classpath = project.sourceSets.main.get().runtimeClasspath
}
