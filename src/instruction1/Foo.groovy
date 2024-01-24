// src/instruction1/Foo.groovy

package instruction1

class Foo {

    final Script script

    Foo(Script script) {
        this.script = script
    }

    def myCustomFunction(String param1, String param2) {
        script.echo "Parameter 1: ${param1}"
        script.echo "Parameter 2: ${param2}"
    }
}