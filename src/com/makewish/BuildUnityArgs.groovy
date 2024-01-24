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
    Boolean buildEmbededAssets = false
    Boolean appBundle = false
    String gitURL
    String branch
    String unityVersion
    Boolean cleanWs = false
    Boolean gitReset = false
    Boolean archivePreviousBuild = false
    String deployMethod
    String archivePattern = ''
    String apkName
    String customGradleVersion
    Boolean useIL2CPP = true
    Boolean debug = false

    BuildUnityArgs(String appConfig, String buildTarget) {
        this.appConfig = appConfig
        this.buildTarget = buildTarget
    }

    def fill(Map args = [:]) {
        args.each { key, value -> this[key] = value }
        if(this.buildMethod == '') {
            this.buildMethod = '[DryRun]'
        }
    }
}