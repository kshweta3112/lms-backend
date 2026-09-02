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
                    curl -f http://localhost:8081/api/courses
                '''
            }
        }
    }

    post {

        success {
            echo 'LMS DEPLOYMENT SUCCESSFUL!'
        }

        failure {
            echo 'DEPLOYMENT FAILED!'
            echo 'STARTING ROLLBACK...'

            sh '''
                docker stop lms-backend || true
                docker rm lms-backend || true

                if docker image inspect lms-backend:previous > /dev/null 2>&1; then

                    docker run -d \
                    --name lms-backend \
                    -p 8081:8081 \
                    lms-backend:previous

                    echo "ROLLBACK SUCCESSFUL"

                else

                    echo "NO PREVIOUS VERSION AVAILABLE FOR ROLLBACK"

                fi
            '''
        }
    }
}
