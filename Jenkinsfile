pipeline {
    agent any

    tools {
        maven 'maven3.8.6'
        jdk 'jdk1.8'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    triggers {
        githubPush()
        // pollSCM('@daily') // 可选：每日定时拉取（备用兜底）
    }

    stages {
        stage('🧪 Checkout') {
            steps {
                echo '🔄 拉取代码中...'
                checkout scm
            }
        }

        stage('🔧 Build & Test') {
            steps {
                echo '🧪 开始执行自动化测试...'
                // 显式指定 bash，避免 sh 不兼容问题
                sh 'bash -c "mvn clean test"'
                // 收集单元测试报告，展示到 Jenkins UI
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('📊 生成 Allure 报告') {
            steps {
                script {
                    try {
                        echo '📊 准备展示 Allure 测试报告...'
                        sh 'ls -l target/allure-results || echo "❗ 未生成 Allure 结果文件"'
                        allure([
                            includeProperties: false,
                            results: [[path: 'target/allure-results']]
                        ])
                    } catch (Exception e) {
                        echo "⚠️ Allure 报告生成失败：${e.message}"
                    }
                }
            }
        }

        stage('📦 归档构建产物（可选）') {
            when {
                expression { fileExists('target') }
            }
            steps {
                echo '📦 归档 jar 包或其他产物...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
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
