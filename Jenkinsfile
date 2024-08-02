pipeline {
    agent {
        label 'docker'
    }

    triggers {
        githubPush()
    }
    
    environment {
        DOCKER_IMAGE = 'junkyard-backend'
        REGISTRY = '3.35.254.168:5000'
        PROJECT_NAME = 'junkyard'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/kiomnd2/junkyard-backend.git'
            }
        }
        
        stage('Build') {
            steps {
                sh '''chmod +x gradlew
                ./gradlew clean build -x test
                '''
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    def modules = ['junkward-app-internal-api']
                    for (module in modules) {
                        dir(module) {
                            sh 'docker build -t ${REGISTRY}/${module}:latest .'
                            sh 'docker push ${REGISTRY}/${module}:latest'
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
