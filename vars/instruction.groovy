// vars/instruction.groovy

import com.makewish.BuildUnityArgs
import com.makewish.UploadArtifactsArgs

def generateDefualtBuildUnityJobParameters(BuildUnityArgs buildArgs) {
    def jobParameters = [
        string(name: 'projectName', value: buildArgs.projectName), 
        string(name: 'buildMethod', value: buildArgs.buildMethod), 
        string(name: 'appConfig', value: buildArgs.appConfig), 
        string(name: 'bundleVersion', value: buildArgs.bundleVersion), 
        booleanParam(name: 'useIL2CPP', value: buildArgs.useIL2CPP), 
        booleanParam(name: 'debug', value: buildArgs.debug), 
        string(name: 'versionCode', value: buildArgs.versionCode), 
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
        jobParameters.add(string(name: 'archivePattern', value: buildArgs.archivePattern))
    }
    if(buildArgs.preprocess1) {
        jobParameters.add(string(name: 'preprocess1', value: buildArgs.preprocess1))
    }
    if(buildArgs.preprocess2) {
        jobParameters.add(string(name: 'preprocess2', value: buildArgs.preprocess2))
    }

    return jobParameters
}

def buildAndroid(Map args = [:]) {
    
    def buildArgs = new BuildUnityArgs('android', 'Android')
    println "origin buildArgs.value = ${buildArgs.useApkExtension}"
    //buildArgs.fill(args)

    args.each{ key, value -> 
        println "args.value = ${value}, ${value.getClass()}"
        println "origin buildArgs.value[${key} , ${buildArgs[key]}"
        buildArgs[key] = value
        
        println "after buildArgs.value[${key} , ${buildArgs[key]}"
    }

    
    println "prev buildArgs.value = ${buildArgs.useApkExtension}"
    if(buildArgs.appBundle == true) {
        buildArgs.useApkExtension = true
    }
    println "after appBundle = ${buildArgs.appBundle} buildArgs.value = ${buildArgs.useApkExtension}"

    echo "begin build android, project = ${buildArgs.projectName}"

    def jobParameters = generateDefualtBuildUnityJobParameters(buildArgs)

    jobParameters.add(booleanParam(name: 'appBundle', value: buildArgs.appBundle))
    jobParameters.add(booleanParam(name: 'useApkExtension', value: buildArgs.useApkExtension))

    if(buildArgs.customGradleVersion) {
        jobParameters.add(string(name: 'customGradleVersion', value: buildArgs.customGradleVersion))
    }
    jobParameters.each{ value -> 
        println "jobParameters.value = $value"
    }
    return null
    def result = build job: 'Instruction/BuildUnity', parameters: jobParameters

    return result
}

def buildIOS(Map args = [:]) {
    def buildArgs = new BuildUnityArgs('ios', 'iOS')
    buildArgs.fill(args)

    echo "begin build ios, project = ${buildArgs.projectName}"

    def jobParameters = generateDefualtBuildUnityJobParameters(buildArgs)

    def result = build job: 'Instruction/BuildUnity', parameters: jobParameters

    return result
}

def uploadArtifacts(String projectName, String fromJob, String buildNumber, String src, String dest, String dir) {
    
    def buildResult = build job: 'Instruction/UploadArtifacts', parameters: [
        string(name: 'projectName', value: projectName),
        string(name: 'fromJob', value: fromJob),
        string(name: 'buildNumber', value: buildNumber),
        string(name: 'archivePattern', value: src),
        string(name: 'changeName', value: dest),
        string(name: 'dir', value: dir)]

    currentBuild.description = buildResult.description
}

def uploadAPK(Map args = [:]) {
    
    def uploadArgs = new UploadArtifactsArgs(args)

    def job = uploadArgs.job
    def buildNumber = uploadArgs.buildNumber
    def dest = uploadArgs.dest
    
    if (uploadArgs.deployMethod == 'Encrypt') {
        
        job = "Instruction/EncryptApk"
        
        def encryptResult = build job: job, parameters: [
            string(name: 'projectName', value: uploadArgs.projectName),
            string(name: 'buildNumber', value: uploadArgs.buildNumber),
            string(name: 'apkName', value: uploadArgs.dest), 
            booleanParam(name: 'appBundle', value: uploadArgs.appBundle)]
            
        dest = encryptResult.description
        buildNumber = encryptResult.number
    } else {
        if(uploadArgs.appBundle) {
            dest = dest + ".aab"
        } else {
            dest = dest + ".apk"
        }
    }

    return uploadArtifacts(uploadArgs.projectName, job, buildNumber, uploadArgs.src, dest, "APK")
}

def uploadIPA(Map args = [:]) {

    return uploadArtifacts(args.projectName, args.job, args.buildNumber, args.src, args.dest + ".zip", "IPA")
}

return this
