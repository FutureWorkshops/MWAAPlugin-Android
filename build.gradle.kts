// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

if (project.rootProject.file("../gradle_files/common.gradle.kts").exists()) {
    apply("../gradle_files/common.gradle.kts")
}

buildscript {
    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }

        maven {
            name = "Mobile Workflow"
            url = uri("https://raw.githubusercontent.com/FutureWorkshops/MobileWorkflowCore-Android-Distribution/main")
            credentials(HttpHeaderCredentials::class) {
                val properties = java.util.Properties()
                var token = ""
                token = if (project.rootProject.file("local.properties").exists()) {
                    properties.load(project.rootProject.file("local.properties").inputStream())
                    properties.getProperty("project.githubPAT")
                } else {
                    System.getenv("GITHUB_PAT") ?: ""
                }
                name = "Authorization"
                value = "token $token"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}