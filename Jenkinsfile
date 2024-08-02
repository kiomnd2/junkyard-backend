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
                git 'https://github.com/kiomnd2/junkyard-backend.git'
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    def modules = ['app', 'batch', 'queue']
                    for (module in modules) {
                        dir(module) {
                            docker.build("${DOCKER_IMAGE}-${module}:${env.BUILD_ID}", ".")
                            docker.withRegistry("${REGISTRY}") {
                                docker.image("${DOCKER_IMAGE}-${module}:${env.BUILD_ID}").push()
                            }
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
