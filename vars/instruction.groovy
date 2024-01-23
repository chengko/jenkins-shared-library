

def uploadAPK(String projectName, String fromJob, String fromBuildNumber, Boolean appBundle = false, String deployMethod = 'Archive', String src = '*', String dest = '') {
    
    def job = fromJob
    def buildNumber = fromBuildNumber
    
    if (deployMethod == 'Encrypt') {
        
        job = "Instruction/EncryptApk"
        
        def encryptResult = build job: fromJob, parameters: [
            string(name: 'projectName', value: projectName),
            string(name: 'buildNumber', value: fromBuildNumber),
            string(name: 'apkName', value: dest), 
            booleanParam(name: 'appBundle', value: appBundle)]
            
        dest = encryptResult.description
        buildNumber = encryptResult.number
    } else {
        if(appBundle) {
            dest = dest + ".aab"
        } else {
            dest = dest + ".apk"
        }
    }

    return uploadArtifacts(projectName, job, buildNumber, src, dest, "APK")
}
def uploadIPA(String projectName, String fromJob, String fromBuildNumber, String src, String dest) {
    return uploadArtifacts(projectName, fromJob, fromBuildNumber, src, dest + ".zip", "IPA")
}

def uploadArtifacts(String projectName, String fromJob, String fromBuildNumber, String src, String dest, String dir) {
    def buildResult = build job: 'Instruction/UploadArtifacts', parameters: [
        string(name: 'projectName', value: projectName),
        string(name: 'fromJob', value: fromJob),
        string(name: 'buildNumber', value: fromBuildNumber),
        string(name: 'archivePattern', value: src),
        string(name: 'changeName', value: dest),
        string(name: 'dir', value: dir)]

    currentBuild.description = buildResult.description
}
/*
def uploadIPA(String projectName, String fromJob, String fromBuildNumber, String file) {
    // Your uploadArtifacts logic here
    def buildResult = build job: 'Instruction/UploadArtifacts', parameters: [
        string(name: 'projectName', value: projectName),
        string(name: 'fromJob', value: fromJob),
        string(name: 'buildNumber', value: fromBuildNumber),
        string(name: 'archivePattern', value: "unity-andorid.apk"),
        string(name: 'changeName', value: "${apkName}.apk"),
        string(name: 'dir', value: "APK")]
    def artifactLink = "<a href='${buildResult.description}/${apkName}.apk'>${apkName}.apk</a>"
    currentBuild.description = artifactLink
}

def uploadAPK(String projectName, String fromJob, String fromBuildNumber, String file) {
    // Your uploadArtifacts logic here
    def buildResult = build job: 'Instruction/UploadArtifacts', parameters: [
        string(name: 'projectName', value: projectName),
        string(name: 'fromJob', value: fromJob),
        string(name: 'buildNumber', value: fromBuildNumber),
        string(name: 'archivePattern', value: "unity-andorid.apk"),
        string(name: 'changeName', value: "${apkName}.apk"),
        string(name: 'dir', value: "APK")]
    def artifactLink = "<a href='${buildResult.description}/${apkName}.apk'>${apkName}.apk</a>"
    currentBuild.description = artifactLink
}

def uploadArtifacts(String projectName, String fromJob, String fromBuildNumber, String dir, String file) {
    // Your uploadArtifacts logic here
    def buildResult = build job: 'Instruction/UploadArtifacts', parameters: [
        string(name: 'projectName', value: projectName),
        string(name: 'fromJob', value: fromJob),
        string(name: 'buildNumber', value: fromBuildNumber),
        string(name: 'archivePattern', value: "unity-andorid.apk"),
        string(name: 'changeName', value: "${file}"),
        string(name: 'dir', value: "APK")]
    def artifactLink = "<a href='${buildResult.description}/${file}'>${file}</a>"
    currentBuild.description = artifactLink
}
def maruko() {
    stage("Upload") {
        def fromJob = "$JOB"
        def fromBuildNumber = "${result.number}"
        
        if (params.deployMethod == 'Encrypt') {
            
            fromJob = "Instruction/EncryptApk"
            
            def encryptResult = build job: "$fromJob", parameters: [
                string(name: 'projectName', value: "$PROJECT_NAME"),
                string(name: 'buildNumber', value: "${result.number}"),
                string(name: 'user', value: "$USER"),
                string(name: 'paramA', value: "$PARAM_A"),
                string(name: 'paramC', value: "$PARAM_C"),
                string(name: 'apkName', value: "$APK_NAME"), 
                booleanParam(name: 'appBundle', value: "$appBundle")]
                
            fromBuildNumber = "${encryptResult.number}"
        }
        
        build job: 'Instruction/UploadArtifacts', parameters: [
            string(name: 'projectName', value: "$PROJECT_NAME"),
            string(name: 'fromJob', value: "$fromJob"),
            string(name: 'buildNumber', value: "$fromBuildNumber"),
            string(name: 'dir', value: "APK")]

        build job: 'Instruction/UploadArtifacts', parameters: [
            string(name: 'projectName', value: "$PROJECT_NAME"),
            string(name: 'fromJob', value: "$JOB"),
            string(name: 'buildNumber', value: "${result.number}"),
            string(name: 'dir', value: "IPA")]
        
    }
}
def pocketstore() {
    stage("Upload") {
        def fromJob = "$JOB"
        def fromBuildNumber = "${result.number}"
        
        if (params.deployMethod == 'Encrypt') {
            
            fromJob = "Instruction/EncryptApk"
            
            def encryptResult = build job: "$fromJob", parameters: [
                string(name: 'projectName', value: "$PROJECT_NAME"), 
                string(name: 'buildNumber', value: "${result.number}"),
                string(name: 'user', value: "$USER"),
                string(name: 'paramA', value: "$PARAM_A"),
                string(name: 'paramC', value: "$PARAM_C"),
                string(name: 'apkName', value: "$APK_NAME"), 
                booleanParam(name: 'appBundle', value: "$appBundle")]
                
            fromBuildNumber = "${encryptResult.number}"
        }
        
        build job: 'Instruction/UploadArtifacts', parameters: [
            string(name: 'projectName', value: "$PROJECT_NAME"),
            string(name: 'fromJob', value: "$fromJob"),
            string(name: 'buildNumber', value: "$fromBuildNumber"),
            string(name: 'archivePattern', value: "unity-andorid.apk"),
            string(name: 'changeName', value: "${APK_NAME}.apk"),
            string(name: 'dir', value: "APK")]
        
        currentBuild.description = "<a href='https://pocketstore-artifacts.s3.amazonaws.com/APK/${APK_NAME}.apk'>${APK_NAME}.apk</a>"

        build job: 'Instruction/UploadArtifacts', parameters: [
            string(name: 'projectName', value: "$PROJECT_NAME"),
            string(name: 'fromJob', value: "$JOB"),
            string(name: 'buildNumber', value: "${result.number}"),
            string(name: 'dir', value: "IPA")]
    }
}

*/