plugins {
    kotlin("jvm") version "1.3.50"
    application
    id("com.github.ben-manes.versions") version "0.27.0"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

sourceSets {
    main {
        java.srcDir("src/main")
    }
    test {
        java.srcDir("src/test")
    }
}

application {
    mainClassName = "InterpreterKt"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation("io.kotlintest", "kotlintest-runner-junit5", "3.4.2")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
