pipeline {
    environment {
        registryCredential = 'dockerhub_id'
        dockerImage = ''
    }
    agent any
    stages {
        stage('拉取代码: Checkout Code') {
            steps {
               checkout scmGit(branches: [[name: '*/${branch}']], extensions: [], userRemoteConfigs: [[credentialsId: '2893c330-9a16-498f-afd6-d66d37e5f806', url: 'https://github.com/sihaihou/dasbx.git']])
            }
        }
        stage('构建父项目: build Parent Project') {
            steps {
              sh 'mvn -U clean install -Dmaven.test.skip=true'
            }
        }
        stage('构建子项目: build Sub Project') {
            steps {
                //sh 'cd ${projectName}'
                sh 'mvn  -f ${projectName} -U clean package'
            }
        }
        stage('制作Docker镜像：Build Docker Image') {
            steps {
                sh 'cd ${projectName}'
                script {
                    dockerImage = docker.build "housihai/dasbx-"+${projectName}+":0.0.1"
                }
            }
        }
    }
}
