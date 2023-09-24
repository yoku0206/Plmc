import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*

buildscript {

    repositories {

        maven { url = uri("https://maven.minecraftforge.net/") }
        mavenCentral()

    }
    dependencies {

        classpath("net.minecraftforge.gradle:ForgeGradle:[6.0,6.2)") { isChanging = true }
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")

    }

}

plugins {

    kotlin("jvm") version "1.8.0"
    idea
    java

}

apply(plugin = "net.minecraftforge.gradle")

group = "me.yoku"
version = "1.0"

repositories {

    mavenCentral()

    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }

}

dependencies {

    "minecraft"("net.minecraftforge:forge:1.16.5-36.2.39")

    implementation("thedarkcolour:kotlinforforge:1.16.0")

}

val Project.minecraft: net.minecraftforge.gradle.common.util.MinecraftExtension
    get() = extensions.getByType()

minecraft.let {

    it.mappings("official", "1.16.5")
    it.runs {

        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                this.create("plmc") {
                    source(sourceSets.main.get())
                }
            }
        }
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                this.create("plmc") {
                    source(sourceSets.main.get())
                }
            }
        }

        create("data") {
            workingDirectory(project.file("run"))

            property("forge.logging.console.level", "debug")

            args("--mod", "plmc", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources"))

            mods {
                this.create("plmc") {
                    source(sourceSets.main.get())
                }
            }
        }

    }

}

tasks {

    val javaVersion = JavaVersion.VERSION_1_8

    withType<JavaCompile> {

        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()

    }

    withType<KotlinCompile> {

        kotlinOptions { jvmTarget = javaVersion.toString() }

    }

    java {

        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion

    }

    withType<Jar> {

        archiveBaseName.set("Plmc-${project.version}")

        manifest {

            attributes(
                mapOf(
                    "Specification-Title" to project.name,
                    "Specification-Vendor" to "author",
                    "Specification-Version" to "1",
                    "Implementation-Title" to project.name,
                    "Implementation-Vendor" to "author",
                    "Implementation-Version" to project.version,
                    "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd").format(Date())
                )
            )

        }

    }

}