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
    //apply(plugin = rootProject.libs.plugins.gitSemVer)
    dependencies {
        implementation(rootProject.libs.bundles.vertx.dependencies)
        implementation(rootProject.libs.mongodb.driver)
        implementation("io.vertx:vertx-junit5:4.1.5")
        testImplementation(rootProject.libs.junit.api)
        testRuntimeOnly(rootProject.libs.junit.engine)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}