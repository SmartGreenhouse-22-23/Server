plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "5.2.0"
    alias(libs.plugins.gitSemVer)
}

group = "it.unibo.smartgh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects{
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")
    dependencies {
        implementation(rootProject.libs.bundles.vertx.dependencies)
        implementation(rootProject.libs.mongodb.driver)
        implementation(rootProject.libs.gson)
        testImplementation(rootProject.libs.junit.api)
        testRuntimeOnly(rootProject.libs.junit.engine)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}