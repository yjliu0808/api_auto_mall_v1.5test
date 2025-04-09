pipeline {
    agent any

    stages {
        stage('Test GitHub Connection') {
            steps {
                sh 'curl -I https://github.com'
            }
        }
    }
}
