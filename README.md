# Muon

[![Build Status](https://travis-ci.org/carlomorelli/project-muon.svg?branch=master)](https://travis-ci.org/carlomorelli/project-muon)

[![Coverage Status](https://coveralls.io/repos/github/carlomorelli/project-muon/badge.svg)](https://coveralls.io/github/carlomorelli/project-muon)

Muon is a backend web application prototype.
It not is meant to be anything specific, but simply is a *demo integration* of multiple Java technologies, libraries and tools for the backend and to create the basis for any backend project.
The concept is somewhat inspired by [Dropwizard](www.dropwizard.io), but with the idea of being purely an integration of easy-to-use toolkits more than a full fledged opinionated framework with rigid directives.

In Muon the following Java technologies are combined:

* [Spark](http://sparkjava.com/) for HTTP API (embedding Jetty engine)
* [Google Guice](https://github.com/google/guice) for dependency injection
* [Jackson](https://github.com/FasterXML/jackson) as base JSON engine
* [TestNG](http://testng.org/doc/) + [Mockito](http://site.mockito.org/) for unit and integration testing
* [RestAssured](http://rest-assured.io/) as HTTP client used for integration testing
* [SLF4J](https://www.slf4j.org/) + [Logback](https://logback.qos.ch/) for logging
* [PostgreSQL](https://jdbc.postgresql.org/) + [HikariCP](https://github.com/brettwooldridge/HikariCP) for production database
* [H2](http://www.h2database.com/html/main.html) for database unit testing
* [Sql2o](http://www.sql2o.org/) as POJO wrapper/unwrapper for JDBC 

In addition to that, the project is in Continuous Integration on [Travis-CI](https://travis-ci.org/carlomorelli/project-muon).
The [Cobertura Maven plugin](http://www.mojohaus.org/cobertura-maven-plugin/) and [Coveralls](https://coveralls.io/github/carlomorelli/project-muon) service are used for reporting the code coverage results -- You can check out the `pom.xml` in the `<plugin>...</plugin> section to understand how integration is accomplished.

Even if the Java technologies employed are a few, a FatJar version of Muon weights **less than 12 Megabytes**, making the minification for possible microservices and Docker containers pretty easy. We measured a start up time between 1 and 3 seconds including connection pooling to the database.

## Configuration
The application uses a mixture of configuration file for handling the production database (at `src/main/resources/configuration.properties`), and a Guice module `AppConfig` for injecting the application and the test code.

## Build the project
Required for build are Java 8 JDK and Apache Maven 3.x.

Once the project is cloned, enter the checkout directory and run 
```
mvn clean package
```
This will build and unit-test the code and produce the FatJar in the `target/` directory. 

## Run the application
To run the application or the integration test, the requirement is to have a local install of PostgreSQL 9.x. *The application has been tested with version 9.4 and 9.5 on both Windows and Linux platforms*.

To prepare the database service (one-off):
* Install PostgreSQL 9.x as a service
* Create a database with `createdb <dbname> -h localhost -U postgres`
* Submit the schema file with `psql -d <dbname> -a -f ./src/main/resources/db/schema.sql`

Once the FatJar is built, the application can be simply run with
```
java -jar target/muon-app-0.0.1-SNAPSHOT.jar
```
To test the app, you can hit the version page. On Linux or Windows Powershell:
```
$ curl localhost:8080/version
```
To start the app on a different port than the default 8080, simply append the port on the launch command line. Example: `java -jar target/muon-app-0.0.1-SNAPSHOT.jar 9090` will start on port 9090.
Integration tests can be launched with 
```
mvn clean verify
```


## HTTP api
* GET `http://localhost:8080/webapi/items` - Get list of items and their details
* GET `http://localhost:8080/webapi/items/1` - Get details on single item with id 1
* POST `http://localhost:8080/webapi/items` - Submit new item

## Rationale
The idea behind Muon is simply to benchmark how minimal can a web-app be when based on the JDK.

Many of the toolkits an libraries used in Muon are industry standard. The less known libraries used here are probably *Spark*, *Sql2o* and *RestAssured*.

Spark, even if it is not a widely used HTTP API library, is straightforward, lean, and contained: the web-app part of Muon is just `App` and `AbstractHandler` classes and the work needed to switch to Jersey (for example) is minimal.

RestAssured is the greatest testing tool ever written: it allows to write REST integration tests in a very nice fluent DSL "given -> when -> then". It also integrates a nice JSON -> Class marshalling out of the box.

I chose not to use Hibernate or other ORM libraries, so the database is accessed through JDBC and SQL queries. Sql2o places itself between JDBC and the business logic: It wraps JDBC input and output streams with entity objects so the user does not need, for example, to navigate manually a ResultSet query and reconstruct manually the entity object retrieved. 



Happy coding!
/Carlo




