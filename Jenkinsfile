pipeline {
    agent any

    tools {
        jdk 'JDK-21'
        maven 'Maven-3.9.16'
    }

    parameters {
        string(name: 'TOMCAT_HOME', defaultValue: 'C:\\DevTools\\apache-tomcat-11', description: 'Tomcat installation folder (deployment target)')
        string(name: 'TOMCAT_PORT', defaultValue: '8090', description: 'Tomcat HTTP port used in the application URL')
    }

    environment {
        APP_NAME = 'fraud-alert-dashboard'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn -B test -Pselenium'
            }
            post {
                always {
                    junit 'target/surefire-reports/TEST-*.xml'
                    archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package -DskipTests'
                archiveArtifacts artifacts: 'target/*.war'
            }
        }

        stage('Deploy') {
            steps {
                bat 'copy /y target\\*.war "%TOMCAT_HOME%\\webapps\\%APP_NAME%.war"'
                echo "Deployed to http://localhost:${params.TOMCAT_PORT}/${env.APP_NAME}/"
            }
        }
    }

    post {
        success {
            echo 'Pipeline finished successfully'
        }
        failure {
            echo 'Pipeline failed - check the console output'
        }
    }
}
