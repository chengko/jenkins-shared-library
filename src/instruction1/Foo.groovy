// src/instruction1/Foo.groovy

package instruction1

class Foo implements Serializable {

    def myCustomFunction(String param1, String param2) {
        sh "echo \"Parameter 1: ${param1}\""
        sh "echo \"Parameter 2: ${param2}\""
    }
}