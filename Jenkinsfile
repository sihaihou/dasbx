pipeline {
    agent any

    stages {
        stage('checkout code') {
            steps {
               checkout scmGit(branches: [[name: '*/${branch}']], extensions: [], userRemoteConfigs: [[credentialsId: '2893c330-9a16-498f-afd6-d66d37e5f806', url: 'https://github.com/sihaihou/dasbx.git']])
            }
        }
        stage('build jar') {
            steps {
              sh 'mvn  -f ${projectName} clean package'
            }
        }
    }
}
