// vars/project.groovy

def loadSettings(String projectName) {
    def content = libraryResource("project/${projectName}/settings.yml")
    def project = readYaml text: content
    echo "load project settings: $project.Name"

    return project
}