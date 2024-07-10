pipeline {
    agent any

    stages {
        stage('checkout code') {
            steps {
               checkout scmGit(branches: [[name: '*/${branch}']], extensions: [], userRemoteConfigs: [[credentialsId: '2893c330-9a16-498f-afd6-d66d37e5f806', url: 'https://github.com/sihaihou/dasbx.git']])
            }
        }
        stage('构建父项目') {
            steps {
              sh 'mvn -U clean install -Dmaven.test.skip=true'
            }
        }
        stage('构建子项目') {
            steps {
              sh 'mvn  -f ${projectName} -U clean package'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t housihai:dasbx-${projectName}:0.0.1 .'
            }
        }
    }
}
