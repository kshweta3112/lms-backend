pipeline {

    agent any

    environment {
        IMAGE_NAME = "lms-backend"
        CONTAINER_NAME = "lms-backend"
    }

    stages {

        stage('Build') {
            steps {
                echo 'Building LMS application...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Automated Testing') {
            steps {
                echo 'Running automated tests...'
                sh 'mvn test'
            }
        }

        stage('Backup Previous Version') {
            steps {
                echo 'Backing up previous Docker image...'

                sh '''
                    if docker image inspect lms-backend:latest > /dev/null 2>&1; then
                        docker tag lms-backend:latest lms-backend:previous
                    fi
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t lms-backend:latest .'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying LMS application...'

                sh '''
                    docker stop lms-backend || true
                    docker rm lms-backend || true

                    docker run -d \
                    --name lms-backend \
                    -p 8081:8081 \
                    lms-backend:latest
                '''
            }
        }

        stage('Health Check') {
            steps {
                echo 'Checking LMS application health...'

                sh '''
                    sleep 15
                    curl -f http://localhost:8081/actuator/health
                '''
            }
        }
    }

    post {
    success {
        echo 'LMS DEPLOYMENT SUCCESSFUL!'

        emailext(
            to: 'kundelishweta@gmail.com',
            subject: "LMS Deployment SUCCESS - Build #${BUILD_NUMBER}",
            body: """
LMS Deployment Successful!

Build Number: ${BUILD_NUMBER}
Job: ${JOB_NAME}
Status: SUCCESS

Health Check: UP
"""
        )
    }

    failure {
        echo 'LMS DEPLOYMENT FAILED!'

        emailext(
            to: 'kundelishweta@gmail.com',
            subject: "LMS Deployment FAILED - Build #${BUILD_NUMBER}",
            body: """
LMS Deployment Failed!

Build Number: ${BUILD_NUMBER}
Job: ${JOB_NAME}
Status: FAILED

Please check the Jenkins console output.
"""
        )
    }
}
}
