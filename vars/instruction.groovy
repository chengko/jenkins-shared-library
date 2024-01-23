// vars/instruction.groovy

def buildAPK(String projectName, String bundleVersion, String versionCode, Boolean buildEmbededAssets, Boolean appBundle, String gitURL, String branch, 
    String unityVersion, Boolean cleanWs, Boolean gitReset, Boolean archivePreviousBuild, String deployMethod, String archivePattern, String apkName, String customGradleVersion,
    String appConfig = 'android', String buildTarget = 'Android', String buildMethod = 'CITool.BuildApp', String preprocess1 = '', String preprocess2 = '', Boolean useIL2CPP = true, Boolean useApkExtension = false, Boolean debug = false) {
    
    if(buildMethod == '') {
        buildMethod = '[DryRun]'
    }

    def buildParameters = [
        string(name: 'projectName', value: projectName), 
        string(name: 'buildMethod', value: buildMethod), 
        string(name: 'appConfig', value: appConfig), 
        string(name: 'bundleVersion', value: bundleVersion), 
        booleanParam(name: 'useIL2CPP', value: useIL2CPP), 
        booleanParam(name: 'debug', value: debug), 
        string(name: 'versionCode', value: versionCode), 
        booleanParam(name: 'useApkExtension', value: useApkExtension), 
        booleanParam(name: 'appBundle', value: appBundle), 
        string(name: 'output', value: apkName), 
        booleanParam(name: 'buildEmbededAssets', value: buildEmbededAssets), 
        string(name: 'gitURL', value: gitURL), 
        string(name: 'branch', value: branch), 
        string(name: 'buildTarget', value: buildTarget), 
        string(name: 'buildImage', value: unityVersion), 
        booleanParam(name: 'archivePreviousBuild', value: archivePreviousBuild), 
        booleanParam(name: 'cleanWs', value: cleanWs), 
        booleanParam(name: 'gitReset', value: gitReset), 
        string(name: 'deployMethod', value: deployMethod), 
        string(name: 'archivePattern', value: archivePattern)
    ]

    if(preprocess1 != '') {
        buildParameters.add(string(name: 'preprocess1', value: preprocess1))
    }
    if(preprocess2 != '') {
        buildParameters.add(string(name: 'preprocess2', value: preprocess2))
    }


    def result = build job: 'Instruction/BuildUnity', parameters: buildParameters

    return result
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
