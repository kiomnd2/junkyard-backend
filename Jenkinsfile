pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        DOCKER_IMAGE = 'junkyard-backend'
        REGISTRY = '3.35.254.168:5000'
        PROJECT_NAME = 'junkyard'
        COMPOSE_REPO = 'https://github.com/kiomnd2/junkyard-compose.git'
    }

    stages {
        stage('Checkout') {
            agent { label 'docker' }
            steps {
                git branch: 'main', url: 'https://github.com/kiomnd2/junkyard-backend.git'
            }
        }

        stage('Build') {
            agent { label 'docker' }
            steps {
                sh '''chmod +x gradlew
                ./gradlew clean bootJar
                '''
            }
        }

        stage('Docker Build and Push') {
            agent { label 'docker' }
            steps {
                script {
                    def modules = ['junkyard-app-internal-api', 'junkyard-telegram-server']
                    for (module in modules) {
                        dir(module) {
                            sh "docker buildx build --push --platform=linux/amd64,linux/arm64 -t ${env.REGISTRY}/${module}:latest ."
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
