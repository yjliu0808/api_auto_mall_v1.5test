pipeline {
    agent any

    options {
        // 保留最近10次构建历史
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // 全局构建超时时间：15分钟
        timeout(time: 15, unit: 'MINUTES')
    }

    tools {
        maven 'maven3.8.6'   // Jenkins 全局工具配置中的名称
        jdk 'jdk1.8'         // Jenkins 全局工具配置中的名称
    }

    environment {
        // Maven 的 JVM 内存参数（适配低内存服务器）
        MAVEN_OPTS = '-Xms128m -Xmx512m -XX:+UseG1GC'
        // Jenkins durabletask 心跳机制防卡死
        JAVA_OPTS = '-Dorg.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL=86400'
    }

    triggers {
        githubPush()
        // pollSCM('@daily') // 可选：每天构建
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
                echo '🧪 开始执行自动化测试（单线程 & 输出日志）...'
                lock('build-lock') {
                    script {
                        try {
                            timeout(time: 10, unit: 'MINUTES') {
                                sh '''
                                    echo "🧰 正在运行 Maven 测试 ..."
                                    mvn clean test -B -DforkCount=1 -DreuseForks=false -Dsurefire.printSummary=true | tee mvn-output.log
                                '''
                            }
                        } catch (err) {
                            echo "🚨 Maven 执行失败，请查看 mvn-output.log"
                            error("构建终止")
                        }
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
                archiveArtifacts artifacts: '**/*.log', allowEmptyArchive: true
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
            echo '❌ 构建失败，请检查控制台日志或 mvn-output.log 查看详情。'
        }
    }
}
