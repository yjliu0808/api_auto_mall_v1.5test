pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('🧪 Test GitHub Connection') {
            steps {
                echo '正在测试 GitHub 连接...A111'
                sh 'curl -I https://github.com'
            }
        }
    }

    post {
        success {
            echo '构建成功！！!'
        }
        failure {
            echo ' !构建失败，请排查 Jenkinsfile 或网络问题111。'
        }
    }
}
