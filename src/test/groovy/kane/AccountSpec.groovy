package kane

import spock.lang.Specification

class AccountSpec extends Specification {
    def "withdraw a positive value" () {
        given:
        def account = new Account(5) //because of groovy no need to specify type and no semicolon, no need to create bigdecimals

        when:
        account.withdraw(3)

        then:
        account.balance == 4 //this works because in groovy you don't need to specify the get method but it also work
                             // if you want to use java code
    }

    def "withdrawal throws an exception with negative numbers"() {
        given: "an account with balance of 5 "
        def account = new Account(5)

        when: "withdrawal 3"
        account.withdraw(-3)

        then: "an exception should be thrown"
        NegativeAmountException e = thrown()
        e.amount == -3
    }
}
