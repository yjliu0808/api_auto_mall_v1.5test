pipeline {
    agent any

    tools {
        maven 'maven3.8.6'  // Jenkins 全局工具配置中的名称
        jdk 'jdk1.8'
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m -XX:+UseG1GC'
        JAVA_OPTS = '-Dorg.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL=86400'
    }

    triggers {
        githubPush()
        // pollSCM('@daily') // 可选兜底触发器
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
                lock('build-lock') {
                    script {
                        try {
                            timeout(time: 10, unit: 'MINUTES') {
                                sh 'mvn clean test -B -Dsurefire.printSummary=true | tee mvn-output.log'
                            }
                        } catch (err) {
                            error "❌ Maven 构建失败，请检查 mvn-output.log 中的错误详情"
                        }
                    }
                }
            }
        }

        stage('📊 生成 Allure 报告') {
            steps {
                echo '📊 尝试生成 Allure 报告...'
                sh 'ls -lh target/allure-results || echo "⚠️ 未找到 Allure 结果文件"'
                allure([
                    includeProperties: false,
                    results: [[path: 'target/allure-results']]
                ])
            }
        }

        stage('📦 归档构建产物') {
            steps {
                echo '📦 保存测试产物...'
                archiveArtifacts artifacts: '**/target/**/*.log', allowEmptyArchive: true
                junit '**/target/surefire-reports/*.xml'
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
            echo '❌ 构建失败，请检查 Jenkins 控制台输出或 mvn-output.log。'
        }
    }
}
