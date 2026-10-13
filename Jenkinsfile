pipeline {
    agent any

    triggers {
        pollSCM('H/2 * * * *')
    }

    tools {
        jdk 'JDK-21'
        maven 'Maven-3.9.16'
    }

    parameters {
        string(name: 'TOMCAT_HOME', defaultValue: 'C:\\DevTools\\apache-tomcat-11', description: 'Tomcat installation folder (deployment target)')
        string(name: 'TOMCAT_PORT', defaultValue: '8090', description: 'Tomcat HTTP port used in the application URL')
    }

    environment {
        APP_NAME       = 'fraud-alert-dashboard'
        REGISTRY       = 'localhost:5000'
        IMAGE_NAME     = 'fraud-alert-dashboard'
        IMAGE_TAG      = "1.0.${BUILD_NUMBER}"
        CONTAINER_NAME = 'fraud-app'
        CONTAINER_PORT = '8082'
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
                bat 'mvn -B test -Pselenium -Dbrowser=chrome'
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

        stage('Docker Build') {
            steps {
                bat 'docker build -t %REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG% -t %REGISTRY%/%IMAGE_NAME%:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                bat 'docker push %REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG%'
                bat 'docker push %REGISTRY%/%IMAGE_NAME%:latest'
            }
        }

        stage('Run Container') {
            steps {
                bat(returnStatus: true, script: 'docker rm -f %CONTAINER_NAME%')
                bat 'docker pull %REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG%'
                bat 'docker run -d --name %CONTAINER_NAME% -p %CONTAINER_PORT%:8081 %REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG%'
                echo "Container started: http://localhost:${env.CONTAINER_PORT}/ using ${env.REGISTRY}/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
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
