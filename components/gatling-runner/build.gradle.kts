plugins {
	id("scala")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":components:gatling-context"))

	implementation("io.gatling.highcharts:gatling-charts-highcharts:${VersionsConfig.gatlingVersion}")
	implementation("io.gatling:gatling-core:${VersionsConfig.gatlingVersion}")
	implementation("io.gatling:gatling-http:${VersionsConfig.gatlingVersion}")
	implementation("org.scala-lang:scala-library")
}
