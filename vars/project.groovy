// vars/project.groovy

def loadProject(String projectName) {
    def content = libraryResource("projects/${projectName}.yml")
    def project = readYaml text: content
    echo "load project: $project.Name"

    return project
}