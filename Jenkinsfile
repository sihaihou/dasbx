pipeline {
    agent any
    environment{
        harbor_url = '192.168.83.33:80'
        repo_name = 'housihai'
        tag = 'latest'
        port = '8080'
    }
    stages {
        stage('拉取代码：Checkout Code') {
            steps {
                checkout scmGit(branches: [[name: '*/${branch}']], extensions: [], userRemoteConfigs: [[credentialsId: 'aaee6791-9a89-4c67-aef5-00bf0e36ff96', url: 'https://github.com/sihaihou/dasbx.git']])
            }
        }
        stage('打包构建项目: Build Package Project') {
            steps {
                sh "echo '构建父项目'"
                sh 'mvn -U clean install -Dmaven.test.skip=true'
                 sh "echo '构建子项目'"
                sh 'mvn -f ${project_name} -U clean install -Dmaven.test.skip=true'
            }
        }
        stage('Build and Push Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: '6ec4aacf-9598-4126-821e-970f77e3ad22', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh 'cd ${project_name} && docker build -t ${repo_name}/${project_name}:${tag} .'
                    sh 'docker tag ${repo_name}/${project_name}:${tag} ${harbor_url}/${repo_name}/${project_name}:${tag}'
                    sh 'docker login -u $username -p $password http://$harbor_url'
                    sh 'docker push $harbor_url/${repo_name}/${project_name}:${tag}'
                    sh 'docker rmi -f ${repo_name}/${project_name}:${tag} $harbor_url/${repo_name}/${project_name}:${tag}'
                 }
            }
        }
        /*stage('部署服务'){
            steps{
                sshPublisher(publishers: [sshPublisherDesc(configName: 'action-server', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '/usr/local/shell/publish.sh ${harbor_url} ${repo_name} ${project_name} ${tag} ${port}', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }*/
    }
}
