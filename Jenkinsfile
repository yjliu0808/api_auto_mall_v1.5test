pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('🧪 Test GitHub Connection') {
            steps {
                echo '🔍 A------正在测试 Jenkins 是否能访问 GitHub...'
                sh 'curl -I https://github.com'
                echo '✅ GitHub 连接测试完成'
            }
        }
    }
    post {
        success {
            echo '🎉 构建成功！Jenkins 可以正常访问 GitHub。'
        }
        failure {
            echo '❌ 构建失败，请检查网络连接或 GitHub 状态。'
        }
    }
}
