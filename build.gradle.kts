plugins {
    id("java")
    alias(libs.plugins.gitSemVer)
}

group = "it.unibo.smartgh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
allprojects{
    apply(plugin = "java")
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