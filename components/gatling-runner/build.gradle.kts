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

	implementation("io.gatling.highcharts:gatling-charts-highcharts:3.2.1")
	implementation("io.gatling:gatling-core:3.2.1")
	implementation("io.gatling:gatling-http:3.2.1")
	implementation("org.scala-lang:scala-library")
}
