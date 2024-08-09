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
                    def modules = ['junkward-app-internal-api']
                    for (module in modules) {
                        dir(module) {
                            sh "docker build --platform linux/amd64 -t ${env.REGISTRY}/${module}:latest ."
                            sh "docker push ${env.REGISTRY}/${module}:latest"
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            agent {
                 label 'main'
            }
            steps {
                script {
                    dir('compose') {
                        git branch: 'main', url: "${env.COMPOSE_REPO}"
                    }
                    sh 'docker-compose -f compose/docker-compose.yml pull'
                    sh 'docker-compose -f compose/docker-compose.yml up --force-recreate --no-deps -d'
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
