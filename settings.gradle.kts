pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Movio"
include(":app")
include(":presentation:ui")
include(":presentation:design_system")
include(":presentation:viewmodel")
include(":domain:usecase")
include(":domain:entity")
include(":data:repository")
include(":data:remote")
