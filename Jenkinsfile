#!/usr/bin/env groovy

def docsOutDir = 'docsOut'
def docsRepositoryUrl = 'git@github.com:CraftTweaker/CraftTweaker-Documentation.git'
def gitSshCredentialsId = 'crt_git_ssh_key'
def botUsername = 'crafttweakerbot'
def botEmail = 'crafttweakerbot@gmail.com'

def documentationDir = 'CrafttweakerDocumentation'
def exportDirInRepo = 'docs_exported/1.19/initialinventory'

def branchName = '1.19.2'

pipeline {
    agent any
    tools {
        jdk 'jdk-17.0.1'
    }
    environment {
        modrinth_token = credentials('modrinth_token')
    }
    stages {
        stage('Clean') {
            steps {
                withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                    echo 'Cleaning Project'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean'
                    sh "rm -rf $docsOutDir"
                }
            }
        }

        stage('Build') {
            steps {
                withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                    echo 'Building'
                    sh './gradlew build'
                }
            }
        }

        stage('Git Changelog') {
            steps {
                withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                    sh './gradlew genGitChangelog'
                }
            }
        }

        stage('Publish') {
            stages {
                stage('Updating Version') {
                    when {
                        branch branchName
                    }
                    steps {
                        withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                            script {
                                if (sh(script: "git log -1 --pretty=%B | fgrep -i -e '[skip deploy]' -e '[skip-deploy]'", returnStatus: true) == 0) {
                                    echo 'Skipping Update Version due to [skip deploy]'
                                } else {
                                    echo 'Updating Version'
                                    sh './gradlew updateVersionTracker'
                                }
                            }
                        }
                    }
                }

                stage('Deploying to Maven') {
                    when {
                        branch branchName
                    }
                    steps {
                        withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                            echo 'Deploying to Maven'
                            sh './gradlew publish'
                        }
                    }
                }

                stage('Deploying to CurseForge') {
                    when {
                        branch branchName
                    }
                    steps {
                        withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                            script {
                                if (sh(script: "git log -1 --pretty=%B | fgrep -i -e '[skip deploy]' -e '[skip-deploy]'", returnStatus: true) == 0) {
                                    echo 'Skipping CurseForge due to [skip deploy]'
                                } else {
                                    echo 'Deploying to CurseForge'
                                    sh './gradlew publishCurseForge modrinth postDiscord'
                                }
                            }
                        }
                    }
                }

                stage('Exporting Documentation') {
                    when {
                        branch branchName
                    }
                    steps {
                        withCredentials([file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile')]) {
                            echo 'Cloning Repository at Branch main'

                            dir(documentationDir) {
                                git credentialsId: gitSshCredentialsId, url: docsRepositoryUrl, branch: 'main', changelog: false
                            }

                            echo 'Clearing existing Documentation export'
                            dir(documentationDir) {
                                sh "rm --recursive --force ./$exportDirInRepo"
                            }

                            echo 'Moving Generated Documentation to Local Clone'
                            sh "mkdir --parents ./$documentationDir/$exportDirInRepo"
                            sh "mv ./$docsOutDir/* ./$documentationDir/$exportDirInRepo/"

                            echo 'Committing and Pushing to the repository'
                            dir(documentationDir) {
                                sshagent([gitSshCredentialsId]) {
                                    sh "git config user.name $botUsername"
                                    sh "git config user.email $botEmail"
                                    sh 'git add -A'
                                    //Either nothing to commit, or we create a commit
                                    sh "git diff-index --quiet HEAD || git commit -m 'CI Doc export for build ${env.BRANCH_NAME}-${env.BUILD_NUMBER}\n\nMatches git commit ${env.GIT_COMMIT}'"
                                    sh 'git push origin main'
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    options {
        disableConcurrentBuilds()
    }
}
