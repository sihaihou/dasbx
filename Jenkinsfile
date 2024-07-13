def harbor_url = '192.168.83.33:80'
def harBorCredentialsId = '6ec4aacf-9598-4126-821e-970f77e3ad22'
def repo_name = 'dasbx'
def gitCredentialsId = 'aaee6791-9a89-4c67-aef5-00bf0e36ff96'
def gitRepositoryUrl = 'https://github.com/sihaihou/dasbx.git'
def tag = 'latest'
def port = '8080'
node {
    stage('拉取代码：Checkout Code') {
        checkout scmGit(branches: [[name: '*/${branch}']], extensions: [], userRemoteConfigs: [[credentialsId: "${gitCredentialsId}", url: "${gitRepositoryUrl}"]])
    }
    stage('打包构建项目: Build Package Parent Project') {
        sh 'mvn -U clean install -Dmaven.test.skip=true'
    }
    stage('构建并推送镜像: Build and Push Image') {
        withCredentials([usernamePassword(credentialsId: "${harBorCredentialsId}", passwordVariable: 'password', usernameVariable: 'username')]) {
            sh "docker login -u $username -p $password http://$harbor_url"
            sh "cd ${project_name} && docker build -t ${repo_name}/${project_name}:${tag} ."
            sh "docker tag ${repo_name}/${project_name}:${tag} ${harbor_url}/${repo_name}/${project_name}:${tag}"
            sh "docker push $harbor_url/${repo_name}/${project_name}:${tag}"
        }
    }
    stage('删除本地镜像: Delete Local Image') {
        sh "docker rmi -f ${repo_name}/${project_name}:${tag} $harbor_url/${repo_name}/${project_name}:${tag}"
    }
    /*stage('部署服务'){
        sshPublisher(publishers: [sshPublisherDesc(configName: 'action-server', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '/usr/local/shell/publish.sh ${harbor_url} ${repo_name} ${project_name} ${tag} ${port}', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
    }*/
}
