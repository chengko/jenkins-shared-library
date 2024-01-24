// src/instruction1/Foo.groovy

package com.makewish

class BuildUnityArgs implements Serializable {
    String projectName
    String buildMethod = 'CITool.BuildApp'
    bool useIL2CPP = true
    bool debug = false

    BuildUnityArgs(Map args = [:]) {
        projectName = args['projectName']
        
    }
}