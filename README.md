# POC WebFlux-R2DBC H2-Kotlin

Test project for Spring Boot 2.4.0/Kotlin with coroutines integration and R2DBC repositories.

### Base project

[stat.spring.io](https://start.spring.io/#!type=gradle-project&language=kotlin&platformVersion=2.4.0.RELEASE&packaging=jar&jvmVersion=11&groupId=br.com.movile&artifactId=afromovile&name=afromovile&description=Afromovile%20project%20for%20Spring%20Boot&packageName=br.com.movile.afromovile&dependencies=webflux,data-r2dbc,h2)
 - gradle (kotlin)
 - kotlin
 - webflux
 - spring-data-r2dbc
 - h2
 - actuator
 
 Versions:
 - Spring Boot 2.4.0
 - Gradle 6.6.1
 - Kotlin 1.4.10
 - MockK 1.10.2
 
### Extra dependencies

````kotlin
testImplementation("io.mockk:mockk:1.10.2")
````

### Remarks
- Unit tests and Integration tests are including (100% coverage :))

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/gradle-plugin/reference/html/)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.2.1.RELEASE/spring-framework-reference/languages.html#coroutines)
* [Spring Data R2DBC [Experimental]](https://docs.spring.io/spring-data/r2dbc/docs/1.0.x/reference/html/#reference)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#production-ready)

### Guides
The following guides illustrate how to use some features concretely:

* [R2DBC example](https://github.com/spring-projects-experimental/spring-boot-r2dbc/tree/master/spring-boot-example-h2)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [R2DBC Homepage](https://r2dbc.io)

