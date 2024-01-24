// vars/instruction.groovy

import com.makewish.BuildUnityArgs

def buildAPK(Map args = [:]) {
    
    def buildArgs = new BuildUnityArgs('android', 'Android')
    buildArgs.fill(args)

    echo "begin build unity project - ${buildArgs.projectName}"

    if(buildMethod == '') {
        buildMethod = '[DryRun]'
    }
    def jobParameters = [
        string(name: 'projectName', value: buildArgs.projectName), 
        string(name: 'buildMethod', value: buildArgs.buildMethod), 
        string(name: 'appConfig', value: buildArgs.appConfig), 
        string(name: 'bundleVersion', value: buildArgs.bundleVersion), 
        booleanParam(name: 'useIL2CPP', value: buildArgs.useIL2CPP), 
        booleanParam(name: 'debug', value: buildArgs.debug), 
        string(name: 'versionCode', value: buildArgs.versionCode), 
        booleanParam(name: 'useApkExtension', value: buildArgs.useApkExtension), 
        booleanParam(name: 'appBundle', value: buildArgs.appBundle), 
        string(name: 'output', value: buildArgs.apkName), 
        booleanParam(name: 'buildEmbededAssets', value: buildArgs.buildEmbededAssets), 
        string(name: 'gitURL', value: buildArgs.gitURL), 
        string(name: 'branch', value: buildArgs.branch), 
        string(name: 'buildTarget', value: buildArgs.buildTarget), 
        string(name: 'buildImage', value: buildArgs.unityVersion), 
        booleanParam(name: 'archivePreviousBuild', value: buildArgs.archivePreviousBuild), 
        booleanParam(name: 'cleanWs', value: buildArgs.cleanWs), 
        booleanParam(name: 'gitReset', value: buildArgs.gitReset), 
        string(name: 'deployMethod', value: buildArgs.deployMethod)
    ]
    if(buildArgs.archivePattern) {
        jobParameters.add(sstring(name: 'archivePattern', value: buildArgs.archivePattern))
    }
    if(buildArgs.preprocess1) {
        jobParameters.add(string(name: 'preprocess1', value: preprocess1))
    }
    if(buildArgs.preprocess2) {
        jobParameters.add(string(name: 'preprocess2', value: preprocess2))
    }

    jobParameters.each { key, value ->
        println "Key: $key, Value: $value"
    }

    //def result = build job: 'Instruction/BuildUnity', parameters: jobParameters

    //return result
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

def uploadAPK(String projectName, String fromJob, String fromBuildNumber, Boolean appBundle = false, String deployMethod = 'Archive', String src = '*', String dest = '') {
    
    def job = fromJob
    def buildNumber = fromBuildNumber
    
    if (deployMethod == 'Encrypt') {
        
        job = "Instruction/EncryptApk"
        
        def encryptResult = build job: job, parameters: [
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

return this
