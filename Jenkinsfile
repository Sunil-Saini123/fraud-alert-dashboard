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

        stage('Package') {
            steps {
                bat 'mvn -B package'
                archiveArtifacts artifacts: 'target/*.war'
            }
        }

        stage('Deploy') {
        steps {
            script {
                    def tomcatHome = params.TOMCAT_HOME
                    def appName = env.APP_NAME

                    if (!fileExists("${tomcatHome.replace('\\', '/')}/webapps")) {
                        error "Tomcat webapps directory not found: ${tomcatHome}\\webapps"
                    }

                    bat """
                        copy /y "target\\fraud-alert-dashboard-0.0.1-SNAPSHOT.war" "${tomcatHome}\\webapps\\${appName}.war"
                        if errorlevel 1 exit /b 1
                    """

                    echo "WAR copied to ${tomcatHome}\\webapps\\${appName}.war"
                    echo "Application URL: http://localhost:${params.TOMCAT_PORT}/${appName}/"
                }
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
