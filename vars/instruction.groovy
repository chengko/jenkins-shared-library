// vars/instruction.groovy

import com.makewish.BuildUnityArgs
import com.makewish.UploadArtifactsArgs

def fillValues(source, target) {
    source.each{ key, value -> 
        if(target[key] instanceof Boolean) {
            target[key] = Boolean.valueOf(value)
        } else {
            target[key] = value
        }
    }

    target.normalize()
}

def generateDefualtBuildUnityJobParameters(BuildUnityArgs buildArgs) {
    def jobParameters = [
        string(name: 'projectName', value: buildArgs.projectName), 
        string(name: 'buildMethod', value: buildArgs.buildMethod), 
        string(name: 'appConfig', value: buildArgs.appConfig), 
        string(name: 'bundleVersion', value: buildArgs.bundleVersion), 
        booleanParam(name: 'useIL2CPP', value: buildArgs.useIL2CPP), 
        booleanParam(name: 'debug', value: buildArgs.debug), 
        string(name: 'versionCode', value: buildArgs.versionCode), 
        booleanParam(name: 'buildEmbededAssets', value: buildArgs.buildEmbededAssets), 
        string(name: 'gitURL', value: buildArgs.gitURL), 
        string(name: 'branch', value: buildArgs.branch), 
        string(name: 'buildTarget', value: buildArgs.buildTarget), 
        string(name: 'buildImage', value: buildArgs.unityVersion), 
        booleanParam(name: 'archivePreviousBuild', value: buildArgs.archivePreviousBuild), 
        booleanParam(name: 'cleanWs', value: buildArgs.cleanWs), 
        booleanParam(name: 'gitReset', value: buildArgs.gitReset)
    ]

    if(buildArgs.output) {
        jobParameters.add(string(name: 'output', value: buildArgs.output))
    }
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
    fillValues(args, buildArgs)

    if(buildArgs.appBundle) {
        buildArgs.useApkExtension = true
    }
    if(buildArgs.deployMethod != 'None' && !buildArgs.archivePattern) {
        if(buildArgs.appBundle) {
            buildArgs.archivePattern = '*.aab'
        } else {
            buildArgs.archivePattern = '*.apk'
        }
    }

    echo "begin build android, project = ${buildArgs.projectName}"

    def jobParameters = generateDefualtBuildUnityJobParameters(buildArgs)

    jobParameters.add(booleanParam(name: 'appBundle', value: buildArgs.appBundle))
    jobParameters.add(booleanParam(name: 'useApkExtension', value: buildArgs.useApkExtension))

    if(buildArgs.customGradleVersion) {
        jobParameters.add(string(name: 'customGradleVersion', value: buildArgs.customGradleVersion))
    }

    if(buildArgs.sdkVersion) {
        jobParameters.add(string(name: 'sdkVersion', value: buildArgs.sdkVersion))
    }
    
    def result = build job: 'Instruction/BuildUnity', parameters: jobParameters

    return result
}

def buildIOS(Map args = [:]) {
    def buildArgs = new BuildUnityArgs('ios', 'iOS')
    fillValues(args, buildArgs)

    echo "begin build ios, project = ${buildArgs.projectName}"

    def jobParameters = generateDefualtBuildUnityJobParameters(buildArgs)

    jobParameters.add(booleanParam(name: 'provisioningProfile', value: buildArgs.provisioningProfile))

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
    def uploadArgs = new UploadArtifactsArgs(args)
    return uploadArtifacts(uploadArgs.projectName, uploadArgs.job, uploadArgs.buildNumber, uploadArgs.src, uploadArgs.dest + ".zip", "IPA")
}

return this
