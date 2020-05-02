plugins {
	id("org.springframework.boot") version VersionsConfig.springBootVersion
	id("io.spring.dependency-management") version VersionsConfig.springDependencyManagementVersion
	kotlin("jvm") version VersionsConfig.kotlinVersion apply(false)
	kotlin("plugin.spring") version VersionsConfig.kotlinVersion apply(false)
}

repositories {
	mavenCentral()
}
