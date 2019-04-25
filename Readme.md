#Spock

Is a developer testing framework for java and groovy fully compatible with Junit so everything 
you do in Junit normally works. But being groovy introduces a more expressive way which in pure 
java isn't possible.


- http://spockframework.org/spock/docs/1.3/index.html
- http://meetspock.appspot.com/

###Advantages of use Spock - Groovy over JUnit

- Type inference
- Syntactic sugar
- you can write groovy and java
- Test structure
- Has a built-in mocking framework 
- Data tables for parameterized testing
- Failure context

##Configuration - Maven 

Mainly you need spock-core and groovy-all and for stubbing you will need cglib and objenesis
And to run your tests from maven you need to include the gmaven-plus plugin

Another thing, the Surefire plugin configuration ensures that both JUnit and Spock unit tests are honored by Maven.

```
<dependency>
	<groupId>org.spockframework</groupId>
	<artifactId>spock-core</artifactId>
	<version>1.2-groovy-2.5</version>
	<scope>compile</scope>
</dependency>
<dependency>
	<groupId>org.codehaus.groovy</groupId>
	<artifactId>groovy-all</artifactId>
	<version>2.4.7</version>
	<scope>test</scope>
</dependency>
<dependency>
	<groupId>cglib</groupId>
	<artifactId>cglib-nodep</artifactId>
	<version>3.2.10</version>
</dependency>
<dependency>
	<groupId>org.objenesis</groupId>
	<artifactId>objenesis</artifactId>
	<version>3.0.1</version>
	<scope>test</scope>
</dependency>
```

```
<plugin>
        <!-- The gmavenplus plugin is used to compile Groovy code. To learn more about this plugin,
        visit https://github.com/groovy/GMavenPlus/wiki -->
        <groupId>org.codehaus.gmavenplus</groupId>
        <artifactId>gmavenplus-plugin</artifactId>
        <version>1.6</version>
        <executions>
          <execution>
            <goals>
              <goal>compile</goal>
              <goal>compileTests</goal>
            </goals>
          </execution>
        </executions>
</plugin>
<!-- Optional plugins for using Spock -->
<!-- Only required if names of spec classes don't match default Surefire patterns (`*Test` etc.) -->
<!-- .groovy DOESN't work, or you name your tests *Test or add the *Spec.java to the configuration-->
<plugin>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.20.1</version>
        <configuration>
          <useFile>false</useFile>
          <includes>
            <include>**/*Test.java</include>
            <include>**/*Spec.java</include>
          </includes>
        </configuration>
</plugin>
<!-- Only required for build -->
<plugin>
        <artifactId>maven-deploy-plugin</artifactId>
        <version>2.8.2</version>
        <configuration>
          <skip>true</skip>
        </configuration>
</plugin>

```

##The Code

usually you need a groovy class that extends Specification from the package spock.lang.* and every method
is a test. To maintain the code organized create a folder called groovy under src/test and name the files
with the extension (.groovy)


```
import spock.lang.*

class MyFirstSpec extends Specification {

	// fields
  // fixture methods
  // feature methods
  // helper methods
  
  def "let's try this!"() {
    expect:
    Math.max(1, 2) == 2
  }
}
```

The fields are re-initialized for every test, they are not shared unless you specify it
###Fixture Methods
```   
   def setupSpec() {}    // runs once -  before the first feature method
   def setup() {}        // runs before every feature method
   def cleanup() {}      // runs after every feature method
   def cleanupSpec() {}  // runs once -  after the last feature method
```

for example in the code above we don't need a method return type just "def" to define the method and 
the best part is the name, it can be a string (a full sentence), this a huge advantage because that is the name that
appears on the reports and logs so it can be very descriptive.  

(show the http://meetspock.appspot.com/)

###Blocks

Divide the test in semantic parts denoting them with labels and hopefully they make the code more readable. They can be 
compare to behavior driven development (BDD). Also they can have a description


####setup: or given:
Prepare the object you want to test. It the first part of the method and it is implicit, anything 
above the first block is setup.

####when:
you exercise the object you prepared, this means call the method we want to test.

####then:
you place the assertions. Plain boolean expressions. if you put then sequentially it evaluates
the order of the calls. 

####and:
####expect:
This is a way of performing our stimulus and assertion within the same block. Not used with when and then blocks
used to test pure functional methods, with no side effects.

####cleanup:
to delete real resources like files or close connections

####where:
A special block used for data driven testing.


When it fails it shows you the boolean expression and both values of the expression, also the stacktrace
is filtered showing only the code you can access. And in intellij you get an extra feature <click to see difference>
that opens a viewer with two panels showing the differences.

```
Condition not satisfied:

account.balance == 4
|       |       |
|       2       false
<kane.Account@769f71a9 balance=2>
 <Click to see difference>


	at kane.AccountSpec.withdraw a positive value(AccountSpec.groovy:14)
```

There is also a built-in way to test exceptions, no need to use try catch blocks. 

`ClassName objectName = thrown()`

Example: `NegativeAmountException e = thrown()`

and then you can add assertions about the exception itself (see code). Another way to deal with exception is

```
then:
thrown(IndexOutOfBoundsException)
```

Alternatively, there is method `notThrown(exception)`
###Data Driven Testing

Is when we test the same behavior multiple times with different parameters and assertions. 
In Java, usually the term is parameterized testing. In spock we need to create data tables
to provide the params to run the test

```
param1 | param2 | result
1      | 2      | 2
2      | 2      | 4
3      | 1      | 3
```

This can be queries from dbs, data read from files.

###Mocking

Stubs are fake classes that come with preprogrammed return values. 
Mocks are fake classes that we can examine after a test has finished and see which methods were run or not.

####Mocks
Is a way of changing the behavior of a class which our object under test collaborates with. 
It’s a helpful way of being able to test business logic in isolation of its dependencies.

A classic example of this would be replacing a class which makes a network call with something 
which simply pretends to.

To mock and object simple use `def myClass = Mock(MyClass)` or `MyClass myClass = Mock()`

To give a mock behavior use `>>`. 

For example: 
 
```
then:
1 * myClass.add(*) >> 4
``` 

if the you define what the method is going to return outside the then: block it is called Stubbing

This can be read as when calling the method add of myClass object with any arguments give 4 as result.

in mockito you would have written

`when(myClass.add(anyMatcher())).thenReturn(4)`

Methods which have not been defined will return sensible defaults, as opposed to throwing an exception.

####Stubs

Stubs cannot demand interactions. Stubs are more ambitious with the return values than Mocked objects.
It always try to return something. p.e. Empty collection instead of null.

####Spying

It's an advance use case. Only works on actual classes not on interfaces. Creates real objects behind the 
spy. Is useful when you need to change the return value from an object. 

It needs an especial construction to build objects:
```
def person = Spy(Person, constructorArgs: ["Larvarete", 40])
```

####Interactions
They are written inside the `then` block and they are more a DSL than real code. They are the assertions
done over the mocks we created before.

`1 * sub1.receive(event)`

read: you expect that the method receive from the object sub1 is called just once with the event argument 

In mockito
```
verify(sub1, times(1)).receive(event)
```

The cardinality that can be used 
```
1 *     // just one call
0 *     // not called
(1..3)  // between one and three calls (inclusive)
(1.._)  // at least one call
(_..3)  // at most three calls
_       // any number of calls
```

####Params or argument matchers

you can mix and match real arguments with argument matcher

```
_                   //just one value, and could be anything
_ as String         //more strict, especify the type
* _                 //any number of params with any value
![value]            //a param with a diffenrent value
literal (5, "foo")  //the params must be exactly this value
{it -> ... }        //groovy closure, it's evaluated to true or false
```
An special case is to use closure to validate the param used in an interaction

```
 1 * userService.sendWelcomeEmail({ 
        User u -> u.email == email && u.name == name 
     })
 ``` 
###Annotations
####@Shared
to shared a field between feature methods and not initialize them every time
####@Before
the @Before annotation used in Junit can be used but you can write a method called setup instead 
and it will be called before every test 
####@Ignore
ignore the annotated feature
####@IgnoreRest
ignore the other features in the class
####@Timeout
####@Unroll
needed for data driving test, you should annotate the methods using data tables with @Unroll to get 
a separated report for every entry.
####@Stepwise
To execute your feature methods in the declaration order


##Others

ask for old state over your objects
```
then:
stack.size == old(stack.size) + 1
```

###Spring
For integration testing it uses Spring test Context Framework underneath, you just need to include 
spock-spring dependency.

###Geb
For testing web application it uses Geb (that is groovy library to interact with Selenium). You need to extend
GebReportingSpec. It even captures screenshots.
You also need the @Stepwise annotation to tell spock that all methods in your class are steps of a workflow.
otherwise every method will be independent.
It has a DSL similar to JQuery to access the objects of the page and to set values to forms inputs is just as
assigning variables.
