plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
    signing

    id("io.github.gradle-nexus.publish-plugin") version "1.2.0"
}


repositories {
    mavenCentral()
    mavenLocal()
}


dependencies {
    implementation("org.jetbrains.kotlinx:dataframe-core:0.14.1")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("script-runtime"))
    testImplementation("io.kotest:kotest-assertions-core:5.4.2")
    testImplementation("com.github.holgerbrandl:kutils:1.1")
}


tasks.test {
    useJUnitPlatform()

    // Tests would fail with default memory settings
    // See https://stackoverflow.com/questions/20490105/gradleworkermain-outofmemoryerror
    maxHeapSize = "2048m"
}


//http://stackoverflow.com/questions/34377367/why-is-gradle-install-replacing-my-version-with-unspecified
group = "com.github.holgerbrandl"
version = "1.4.0-SNAPSHOT"


java {
    withJavadocJar()
    withSourcesJar()
}


kotlin {
    jvmToolchain(11)
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                url.set("https://github.com/holgerbrandl/kdfutils")
                name.set("kdfutils")
                description.set("Misc utilities for kotlin-dataframe")

                scm {
                    connection.set("scm:git:github.com/holgerbrandl/kdfutils.git")
                    url.set("https://github.com/holgerbrandl/kdfutils.git")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://raw.githubusercontent.com/holgerbrandl/kdfutils/master/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("holgerbrandl")
                        name.set("Holger Brandl")
                        email.set("holgerbrandl@gmail.com")
                    }
                }
            }
        }
    }
}


nexusPublishing {
    repositories {
        sonatype()
    }
}


signing {
    sign(publishing.publications["maven"])
}
