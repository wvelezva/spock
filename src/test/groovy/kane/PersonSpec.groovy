package kane

import spock.lang.*

class PersonSpec extends Specification {

    @Ignore
    def "Stubbing"() {
        given:
        def person = Stub(Person)

        expect:
        person.name == ""
        person.bestFriend instanceof Person
        person.bestFriend.name == "Unknown"
    }

    def "define interactions upfront during declaration"() {
        given:
        def person = Stub(Person) {
            sing() >> "cada momento"
        }

        expect:
        person.sing() == "cada momento"
    }

    def "Spies"() {
        given:
        def person = Spy(Person, constructorArgs: ["Larvarete", 40])

        expect:
        person.name == "Larvarete"

        when:
        def text = person.sing()

        then:
        1 * person.sing() >> { callRealMethod() * 2} // you can decide if mock or stub. here call the sing method twice
        text == "tra-la-latra-la-la"
    }
}
