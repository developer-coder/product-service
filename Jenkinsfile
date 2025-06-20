pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello Product'
            }
        }

        stage('Maven Compile') {
            steps {
                echo 'Running mvn compile...'
                bat 'mvn compile'
            }
        }

        stage('Maven Test') {
            steps {
                echo 'Running mvn test...'
                bat 'mvn clean test'
            }
        }

        stage('Maven Package') {
            steps {
                echo 'Running mvn package...'
                bat 'mvn clean package'
            }
        }
  stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t product-service ."
                }
            }
        }
       

         stage('Run with Docker Compose') {
            steps {
                script {
                   bat 'docker-compose down || exit 0'
                    bat 'docker-compose up -d'
                }
            }
        }
       
    }
}
