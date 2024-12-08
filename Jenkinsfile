pipeline {
    agent any
    environment {
        VM_USER = 'azureuser'
        VM_HOST = '48.209.80.214'
        APP_NAME = 'spring-boot-app'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy') {
            steps {
                sshagent(['vm-ssh-credentials-id']) {
                    sh '''
                        scp target/${APP_NAME}.jar ${VM_USER}@${VM_HOST}:/home/${VM_USER}/
                        ssh ${VM_USER}@${VM_HOST} "nohup java -jar /home/${VM_USER}/${APP_NAME}.jar > /dev/null 2>&1 &"
                    '''
                }
            }
        }
    }
}
