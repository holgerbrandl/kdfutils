plugins {
    kotlin("jvm") version "2.0.21"
    `maven-publish`
    signing

//    id("io.github.gradle-nexus.publish-plugin") version "1.2.0"
}

version = "${rootProject.version}"
group = "com.github.holgerbrandl"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(project(":"))

    api( "org.jetbrains.kotlinx:dataframe-excel:0.14.1")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:5.4.2")
}

tasks.test {
    useJUnitPlatform()
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


signing {
    sign(publishing.publications["maven"])
}
