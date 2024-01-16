// vars/upload.groovy

def doSomething(String projectName, String fromJob, String fromBuildNumber, String apkName) {
    // Your uploadArtifacts logic here

    def artifactLink = "<a href='https://${projectName}-artifacts.s3.amazonaws.com/APK/${apkName}.apk'>${apkName}.apk</a>"
    currentBuild.description = artifactLink

    return artifactLink
}

def func2() {
	
}