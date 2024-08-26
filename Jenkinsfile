pipeline {
    agent any

    // Define environment variables, using the Docker Hub credentials configured in Jenkins
    environment {
        dockerCredentials = 'dockerHubCredentials'
    }

    stages {
        // Stage to build the Docker image
        stage('Docker Image Build') {
            steps {
                script {
                    // Build the Docker image using a specified tag
                    def image = docker.build("yourUsername/yourRepositoryName")
                }
            }
        }

        // Stage to push the Docker image to Docker Hub
        stage('Docker Image Push') {
            steps {
                script {
                    // Use the Docker Hub credentials and push the image
                    docker.withRegistry('https://registry.hub.docker.com', dockerCredentials) {
                        image.push("${env.BUILD_ID}")
                        image.push('latest')
                    }
                }
            }
        }
    }
}
