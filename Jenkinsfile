pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))      // 保留最近10次构建
        timeout(time: 15, unit: 'MINUTES')                  // 整个流水线最大执行时间
    }

    tools {
        maven 'maven3.8.6'  // Jenkins 全局工具配置的 Maven 名称
        jdk 'jdk1.8'        // Jenkins 全局工具配置的 JDK 名称
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m -XX:+UseG1GC'
        JAVA_OPTS = '-Dorg.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL=86400'
    }

    triggers {
        githubPush()
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
                    script {
                        try {
                            timeout(time: 10, unit: 'MINUTES') {
                                sh 'mvn clean test -B -Dsurefire.printSummary=true | tee mvn-output.log'
                            }
                        } catch (err) {
                            echo '🚨 Maven 执行失败，请查看构建日志或 mvn-output.log'
                            error "构建失败: ${err}"
                        }
                    }
                }
            }
        }

        stage('📊 生成 Allure 报告') {
            steps {
                echo '📊 正在生成 Allure 报告...'
                script {
                    if (fileExists('target/allure-results')) {
                        echo '✅ 找到 Allure 结果，生成中...'
                        allure([
                            includeProperties: false,
                            results: [[path: 'target/allure-results']]
                        ])
                    } else {
                        echo '⚠️ 未找到 Allure 结果文件，跳过报告生成'
                    }
                }
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
            echo '✅ 构建成功!!!'
        }

        failure {
            echo '❌ 构建失败，请检查 Jenkins 控制台输出或下载 mvn-output.log 查看详情。'
        }
    }
}
