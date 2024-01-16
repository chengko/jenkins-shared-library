// src/common/upload.groovy

package common

//import static common.Project.NAME

def uploadArtifacts(String projectName, String fromJob, String fromBuildNumber, String APK_NAME) {
    // Your uploadArtifacts logic here

    def artifactLink = "<a href='https://${projectName}-artifacts.s3.amazonaws.com/APK/${APK_NAME}.apk'>${APK_NAME}.apk</a>"
    currentBuild.description = artifactLink

    return artifactLink
}
