package kane

import spock.lang.Specification
import spock.lang.Unroll

class MyFirstSpec extends Specification {
    def "let's try this!"() {
        expect:
        Math.max(1, 2) == 2
    }

    def "let's fail!"() {
        expect:
        Math.max(1, 2) == 3
    }

    def "compare sets"() {
        expect:
        ["a", "ba", "ca"] as Set ==  ["a", "ba", "da"] as Set
    }

    def "compare texts"() {
        expect:
        generateText() ==
                """lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's 
standard ummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. 
It has survived not only five centurie, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised 
in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software
 like Aldus PageMaker including versions of Lorem Ipsum"""
    }

    String generateText() {
        """orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"""
    }

    @Unroll
    def "number #a to the power of #b is #c"(int a, int b, int c) {
        expect:
        Math.pow(a, b) == c

        where:
        a | b | c
        1 | 2 | 1
        2 | 2 | 4
        3 | 2 | 9
    }

    @Unroll
    def "number #a to the power of #b is #c without params"() { //params are optional
        expect:
        Math.pow(a, b) == c

        where:
        a | b | c  //the names of the variables used in the code
        1 | 2 | 1
        2 | 2 | 4
        3 | 2 | 9
    }
}