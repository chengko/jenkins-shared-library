// src/com/makewish/UploadArtifactsArgs.groovy

package com.makewish

class UploadArtifactsArgs implements Serializable {

    String projectName
    String job = 'Instruction/BuildUnity'
    String buildNumber
    String dir
    String src = '*'
    String dest = ''

    String deployMethod = 'Archive'
    Boolean appBundle = false

    def normalize() {}
}