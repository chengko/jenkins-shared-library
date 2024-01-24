// vars/instructionBuildAPK.groovy
import com.makewish.BuildUnityArgs

def call(Map parameters = [:]) {
    def debug = parameters.debug ?: false
    def useIL2CPP = parameters.useIL2CPP ?: true
    def buildMethod = parameters.buildMethod ?: 'CITool.BuildApp'
    echo "projectName: ${parameters.projectName}, buildMethod:${buildMethod}, bundleVersion:${parameters.bundleVersion}, gitURL:${parameters.gitURL}, buildTarget:${parameters.buildTarget}, parameters.il2cpp:${parameters.useIL2CPP}, il2cpp:${useIL2CPP}, parameters.debug:${parameters.debug}, debug:${debug}"
    //echo "projectName: ${projectName}, appConfig:${appConfig}, gitURL:${gitURL}, buildTarget:${buildTarget}, debug:${debug}"
    if(parameters.debug != null) {
        echo "debug not null ${parameters.debug}"
    } else {
        echo "debug is null"
    }
    if(parameters.debug) {
        echo "2debug is ${parameters.debug}"
    } else {
        echo "2debug is false"
    }

    def args = new BuildUnityArgs(parameters)
    def metaPro = args.getMetaClass().properties
    println "class - $metaPro"
    args.getMetaClass().properties.each { propertyName ->
        println "Field $propertyName"
        //def propertyValue = args."$propertyName"
        //println "Field $propertyName value: $propertyValue"
    }
}

return this


                    