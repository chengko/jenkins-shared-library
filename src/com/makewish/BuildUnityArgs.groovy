// src/com/makewish/BuildUnityArgs.groovy

package com.makewish

class BuildUnityArgs implements Serializable {

    String projectName
    String appConfig
    String buildTarget
    String bundleVersion
    String versionCode
    String buildMethod = 'CITool.BuildApp'
    String preprocess1
    String preprocess2
    String unityVersion
    String gitURL
    String branch
    Boolean cleanWs = false
    Boolean gitReset = false
    Boolean buildEmbededAssets = false
    Boolean archivePreviousBuild = false
    String buildPath
    String deployMethod = 'None'
    String archivePattern = ''
    String output
    Boolean useIL2CPP = true
    Boolean debug = false

    String provisioningProfile
    String customPlist
    
    Boolean appBundle = false
    Boolean useApkExtension = false
    String sdkVersion
    String customGradleVersion

    BuildUnityArgs(String appConfig, String buildTarget) {
        this.appConfig = appConfig
        this.buildTarget = buildTarget
    }

    def normalize() {

        if(this.buildMethod == '') {
            this.buildMethod = '[DryRun]'
        }
    }
}