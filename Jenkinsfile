pipeline {
    agent any

    tools {
        maven 'maven3.8.6'  // Jenkins 全局工具配置的 Maven 名称
        jdk 'jdk1.8'        // Jenkins 全局工具配置的 JDK 名称
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m -XX:+UseG1GC'
        JAVA_OPTS = '-Dorg.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL=86400'
    }

    triggers {
        githubPush() // GitHub 推送触发
        // pollSCM('@daily') // 可选：每天自动构建一次
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
                    timeout(time: 10, unit: 'MINUTES') {
                        sh 'mvn clean test -B -Dsurefire.printSummary=true | tee mvn-output.log'
                    }
                }
            }
        }

        stage('📊 生成 Allure 报告') {
            steps {
                echo '📊 正在生成 Allure 报告...'
                sh 'ls -lh target/allure-results || echo "⚠️ 未找到 Allure 结果文件"'
                allure([
                    includeProperties: false,
                    results: [[path: 'target/allure-results']]
                ])
            }
        }

        stage('📦 归档构建产物') {
            steps {
                echo '📦 保存构建产物和测试报告...'
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
