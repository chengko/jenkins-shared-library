// vars/instructionBuildAPK.groovy

def call(projectName, appConfig = 'android', buildTarget = 'Android', debug = false) {
    
    echo "${projectName}, ${appConfig}, ${buildTarget}, ${debug}"
}

return this