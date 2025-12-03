pipeline {

    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        disableConcurrentBuilds()
    }

    triggers {
        pollSCM('H/2 * * * *')
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    stages {

        stage('Fix Git Safe Directory') {
            steps {
                bat 'git config --global --add safe.directory C:/ProgramData/Jenkins/.jenkins/workspace/lab-ci-cd'
            }
        }

        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/vikasok05/lab'
                    ]]
                ])
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -v'
                bat 'mvn -B -U clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn -B test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'

                    publishHTML(reportDir: 'target/site/jacoco',
                                reportFiles: 'index.html',
                                reportName: 'JaCoCo Coverage')
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Quality Gates') {
            when {
                anyOf { branch 'main'; branch 'develop' }
            }
            steps {
                echo 'Quality checks (Checkstyle, SpotBugs, SonarQube) — placeholder'
            }
        }

        stage('Deploy (local)') {
            when {
                branch 'main'
            }
            steps {
                bat '''
                echo Starting application...
                start java -jar target/*.jar
                '''
            }
        }
    }

    post {
        success {
            echo "CI/CD pipeline successful for branch: ${env.BRANCH_NAME}"
        }
        failure {
            echo "Build failed for ${env.BRANCH_NAME}"
        }
    }
}
