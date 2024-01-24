// vars/instructionBuildAPK.groovy

def call(projectName, bundleVersion, versionCode, buildEmbededAssets, appBundle, gitURL, branch, 
    unityVersion, cleanWs, gitReset, archivePreviousBuild, deployMethod, archivePattern, apkName, customGradleVersion,
    appConfig = 'android', buildTarget = 'Android', buildMethod = 'CITool.BuildApp', preprocess1 = '', preprocess2 = '', useIL2CPP = true, useApkExtension = false, debug = false) {
    
    echo "projectName: ${projectName}, appConfig:${appConfig}, gitURL:${gitURL}, buildTarget:${buildTarget}, debug:${debug}"
}

return this