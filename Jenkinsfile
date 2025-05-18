pipeline {
    agent any

    environment {
        // Jenkins credentials configuration
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub_credentials') // Docker Hub credentials ID stored in Jenkins
        DOCKER_IMAGE = 'mjf128216989/teedy' // your Docker Hub user name and repository name
        DOCKER_TAG = "${env.BUILD_NUMBER}" // use build number as Docker image tag
    }
    

    

    stages {
        stage('Debug Docker Login') {
        steps {
            sh 'whoami'
        }
    }

        stage('Build') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/master']],
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/Pluviophil3/Teedy.git']]
                )
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Building image') {
            steps {
                script {
                    docker.build("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                }
            }
        }

        stage('Upload image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub_credentials') {
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push()
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push('latest')
                    }
                }
            }
        }

        stage('Run containers') {
            steps {
                script {
                    // Stop and remove any existing container
                    sh 'docker stop teedy-container-8081 || true'
                    sh 'docker rm teedy-container-8081 || true'

                    // Run new container
                    docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run(
                        '--name teedy-container-8081 -d -p 8081:8080'
                    )

                    // Optional: list all running Teedy containers
                    sh 'docker ps --filter "name=teedy-container"'
                }
            }
        }
    }
}
