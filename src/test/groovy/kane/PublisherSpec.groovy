package kane

import spock.lang.Specification

class PublisherSpec extends Specification{
    def pub = new Publisher()
    def sub1 = Mock(Subscriber.class)
    def sub2 = Mock(Subscriber) //In groovy you don't need to write the .class

    def setup() {
        pub.subscribers << sub1 << sub2  //fancy way of pub.getSubscribers().add(sub1) ....
    }

    def "deliver events to all of its subscribers"() {
        given:
        def event = "event"

        when:
        pub.send(event) //real object

        then:
        1 * sub1.receive(event) //these are the mocks expectations
        1 * sub2.receive(event)
    }

    def "one subscriber fails the others don't"(){
        sub1.receive(_) >> { throw new Exception() } //groovy closure, very similar to java 8 lambda expressions

        when:
        pub.send("event1")
        pub.send("event2")

        then:
        1 * sub2.receive("event1")
        1 * sub2.receive("event2")
    }
}
