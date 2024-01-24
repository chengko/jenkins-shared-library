// src/instruction1/Foo.groovy

package com.makewish

class BuildUnityArgs implements Serializable {
    String projectName
    String buildMethod = 'CITool.BuildApp'
    Boolean useIL2CPP = true
    Boolean debug = false

    BuildUnityArgs(Map args = [:]) {
        projectName = args['projectName']
        
    }
}