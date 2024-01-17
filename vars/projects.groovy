// vars/project.groovy

def load(String projectName) {
    def content = libraryResource("project/${projectName}.yml")
    def project = readYaml text: content
    echo "load project: $project.Name"

    return project
}