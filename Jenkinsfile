pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        
        stage('Docker Build') {
            steps {
                bat 'docker build -t travelapp:latest .'
            }
        }
        
        stage('Docker Run') {
            steps {
                bat 'docker stop travelapp || true'
                bat 'docker rm travelapp || true'
                bat 'docker run -d -p 8080:8080 --name travelapp travelapp:latest'
            }
        }
    }
}