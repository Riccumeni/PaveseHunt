pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}



rootProject.name = "PaveseHunt"
include(":app")

include(":unityLibrary")
project(":unityLibrary").projectDir = File("..\\androidBuild\\unityLibrary")

include(":unityLibrary:xrmanifest.androidlib")

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("${project(":unityLibrary").projectDir}/libs")
        }
    }
}