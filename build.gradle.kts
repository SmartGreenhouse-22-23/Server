plugins {
    id("java")
    alias(libs.plugins.gitSemVer)
}

group = "it.unibo.smartgh"
version = "1.0-SNAPSHOT"

var vertx = "4.3.3"
var junit = "5.8.1"

repositories {
    mavenCentral()
}
allprojects{
    apply(plugin = "java")
    dependencies {
        implementation(rootProject.libs.bundles.vertx.dependencies)
        implementation(rootProject.libs.swagger.core)
        implementation(rootProject.libs.mongodb.driver)
        testImplementation(rootProject.libs.junit.api)
        testRuntimeOnly(rootProject.libs.junit.engine)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}