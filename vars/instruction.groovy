
def uploadArtifacts(String projectName, String fromJob, String fromBuildNumber, String apkName) {
    // Your uploadArtifacts logic here
    build job: 'Instruction/UploadArtifacts', parameters: [
        string(name: 'projectName', value: projectName),
        string(name: 'fromJob', value: fromJob),
        string(name: 'buildNumber', value: fromBuildNumber),
        string(name: 'archivePattern', value: "unity-andorid.apk"),
        string(name: 'changeName', value: "${apkName}.apk"),
        string(name: 'dir', value: "APK")]
    def artifactLink = "<a href='https://${projectName}-artifacts.s3.amazonaws.com/APK/${apkName}.apk'>${apkName}.apk</a>"
    currentBuild.description = artifactLink

    return artifactLink
}