// vars/instructionBuildAPK.groovy

def call(String projectName, String bundleVersion, String versionCode, Boolean buildEmbededAssets, Boolean appBundle, String gitURL, String branch, 
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
