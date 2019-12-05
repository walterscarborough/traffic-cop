import org.asciidoctor.gradle.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.asciidoctor.convert") version VersionsConfig.asciidoctorVersion
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":components:gatling-context"))
    implementation(project(":components:gatling-runner"))

    asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:${VersionsConfig.springRestDocsVersion}")
    testCompile("org.springframework.restdocs:spring-restdocs-mockmvc:${VersionsConfig.springRestDocsVersion}")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("com.github.tomakehurst:wiremock-jre8:${VersionsConfig.wireMockVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<AsciidoctorTask> {
    dependsOn(tasks.withType<Test>())

    doLast {
        copy {
            from("$buildDir/asciidoc/html5")
            into("$buildDir/resources/main/static")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Jar> {
    dependsOn(tasks.withType<AsciidoctorTask>())
}
