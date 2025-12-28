import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.11"

project {

    vcsRoot(HttpsGithubComScorpelord803hw)
    vcsRoot(HttpsGithubComScorpelordExampleTeamcityRefsHeadsMaster)

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    artifactRules = "target/*.jar"

    vcs {
        root(HttpsGithubComScorpelordExampleTeamcityRefsHeadsMaster)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
        maven {
            name = "Deploy to Nexus (master only)"
            id = "Deploy_to_Nexus_master_only"

            conditions {
                equals("teamcity.build.branch.is_default", "true")
            }
            goals = "clean deploy"
            userSettingsSelection = "Upload settings.xml file"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object HttpsGithubComScorpelord803hw : GitVcsRoot({
    name = "https://github.com/scorpelord/8-03-hw"
    url = "https://github.com/scorpelord/8-03-hw"
    branch = "refs/heads/main"
    authMethod = password {
        userName = "scorpelord"
        password = "credentialsJSON:95b399f1-993e-4207-b334-c744195015bc"
    }
})

object HttpsGithubComScorpelordExampleTeamcityRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/scorpelord/example-teamcity#refs/heads/master"
    url = "https://github.com/scorpelord/example-teamcity"
    branch = "refs/heads/master"
    branchSpec = "+:refs/heads/*"
    authMethod = password {
        userName = "scorpelord"
        password = "credentialsJSON:2899d1a2-e2cc-426c-894e-a856eab8cb2a"
    }
})
