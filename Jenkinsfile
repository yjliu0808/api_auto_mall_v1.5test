pipeline { //声明一个流水线（Pipeline）构建任务
    agent any  //agent any：允许在任意可用节点（主节点或构建代理）上执行任务

    tools {
        maven 'maven3.8.6'     // Jenkins 全局配置的 Maven 名
        jdk 'jdk1.8'            // Jenkins 全局配置的 JDK 名
    }

    triggers { //triggers 块（触发器）
        githubPush()  //表示每当 GitHub 上有 push 操作时，就自动触发一次构建；
                      //要生效必须 Jenkins 和 GitHub Webhook 配置正确。
    }

    environment {
        // 可以设置一些环境变量，如果你需要的话
    }

    stages {
        stage('🧪 Checkout') {
            steps {
                echo '🔄 拉取代码中...'
                checkout scm //从你在 Jenkins 项目配置的 Git 仓库拉代码
            }
        }

        stage('🔧 Build & Test') {
            steps {
                echo '🧪 开始执行自动化测试...'
                sh 'mvn clean test'
            }
        }

        stage('📊 生成 Allure 报告') {
            steps {
                echo '📊 准备展示 Allure 测试报告...'
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            echo '🧹 构建结束，执行收尾操作...'
            archiveArtifacts artifacts: '**/target/allure-results/**', allowEmptyArchive: true
        }

        success {
            echo '✅ 构建成功！'
        }

        failure {
            echo '❌ 构建失败，请检查 Jenkinsfile、网络或测试代码。'
        }
    }
}
