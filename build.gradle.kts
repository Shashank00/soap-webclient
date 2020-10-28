import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.5.BUILD-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "com.axis.web-starter"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.ws:spring-ws-core")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.projectreactor:reactor-test")
	implementation ("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
	implementation ("org.glassfish.jaxb:jaxb-runtime:2.3.3")

	annotationProcessor ("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
	annotationProcessor ("org.glassfish.jaxb:jaxb-runtime:2.3.3")
	annotationProcessor ("javax.annotation:javax.annotation-api:1.3.2")

	implementation("org.jsoup:jsoup:1.7.2")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.11.1")
}

java {
	withSourcesJar()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	enabled = false
}
tasks.getByName<Jar>("jar") {
	enabled = true
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
