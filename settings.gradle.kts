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

rootProject.name = "Android Demo"
include(":app")
include(":common")
include(":data:calendar")
include(":feature:calendar")
include(":network")
include(":common:android")
include(":common:dataHelper")
include(":data:payment")
include(":data:flower")
include(":feature:authentication")
include(":data:authentication")
include(":core")
