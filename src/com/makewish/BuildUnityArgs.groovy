// src/instruction1/Foo.groovy

package com.makewish

class BuildUnityArgs implements Serializable {
    String projectName
    String buildMethod = 'CITool.BuildApp'
    Boolean useIL2CPP = true
    Boolean debug = false

    BuildUnityArgs(Map args = [:]) {
        echo "Building..."
        args.each { key, value ->
            try {
                this[key] = value
            } catch (MissingPropertyException e) {
                
                // Field does not exist in the object
                println "Field '$key' does not exist in the object. Available fields: ${this.properties.keySet()}"
                throw e
            }
        }
    }
}