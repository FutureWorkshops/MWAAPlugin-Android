// Top-level build file where you can add configuration options common to all sub-projects/modules.

if (project.rootProject.file("../gradle_files/common.gradle.kts").exists()) {
    apply("../gradle_files/common.gradle.kts")
}

buildscript {
    val kotlinVersion by extra("1.6.10")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        maven {
            name = "Local maven"
            setUrl(project.rootProject.file("../MobileWorkflowCore-Android-Distribution").path)
        }

        maven {
            name = "Mobile Workflow"
            url = uri("https://raw.githubusercontent.com/FutureWorkshops/MobileWorkflowCore-Android-Distribution/main")
            credentials(HttpHeaderCredentials::class) {
                val token = (getProperty("project.githubPAT") as? String)
                    ?: System.getenv("GITHUB_PAT") ?: ""
                name = "Authorization"
                value = "token $token"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

fun getProperty(property: String): Any? {
    val local = java.util.Properties()
    if (project.rootProject.file("local.properties").exists()) {
        local.load(project.rootProject.file("local.properties").inputStream())
    }
    return when {
        local.containsKey(property) -> local.getProperty(property)
        project.hasProperty(property) -> project.property(property)
        else -> null
    }
}