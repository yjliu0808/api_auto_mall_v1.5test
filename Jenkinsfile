pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))     // 保留5个构建
        timeout(time: 12, unit: 'MINUTES')                // 整体超时时间缩短
        disableConcurrentBuilds()                         // 禁止并发构建，避免多构建争内存
    }

    tools {
        maven 'maven3.8.6'
        jdk 'jdk1.8'
    }

    environment {
        MAVEN_OPTS = '-Xms128m -Xmx384m -XX:+UseSerialGC'
        JAVA_OPTS = '-Dorg.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL=86400'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '🔄 拉取代码中...'
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo '🚀 执行 Maven 测试（限制内存 & 单线程）'
                lock('build-lock') {
                    script {
                        try {
                            timeout(time: 8, unit: 'MINUTES') {
                                sh '''
                                    echo "[MAVEN TEST] 开始执行..."
                                    mvn clean test -B -DforkCount=1 -DreuseForks=false -Dsurefire.printSummary=true | tee mvn-output.log
                                '''
                            }
                        } catch (err) {
                            echo "❌ 测试失败，请查看 mvn-output.log"
                            error("终止构建")
                        }
                    }
                }
            }
        }

        stage('Allure 报告') {
            when {
                expression { fileExists("target/allure-results") }
            }
            steps {
                echo '📊 生成 Allure 报告...'
                allure([
                    includeProperties: false,
                    results: [[path: 'target/allure-results']]
                ])
            }
        }

        stage('归档产物') {
            steps {
                echo '📦 保存日志和报告...'
                archiveArtifacts artifacts: '**/*.log', allowEmptyArchive: true
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            echo '🧹 清理构建环境...'
            archiveArtifacts artifacts: '**/target/allure-results/**', allowEmptyArchive: true
        }
        success {
            echo '✅ 构建成功！'
        }
        failure {
            echo '❌ 构建失败，请检查 mvn-output.log。'
        }
    }
}
