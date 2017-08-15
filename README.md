# Project Muon

[![Build Status](https://travis-ci.org/carlomorelli/project-muon.svg?branch=master)](https://travis-ci.org/carlomorelli/project-muon)

[![Coverage Status](https://coveralls.io/repos/github/carlomorelli/project-muon/badge.svg)](https://coveralls.io/github/carlomorelli/project-muon)

Project Muon (we still don't have a name :) is a basic web app backend prototype.
It is not meant to be anything specific, but a base for more advanced products.

Thus, this repository simply is a *demo integration* of multiple Java technologies, frameworks and tools for the backend and to create the basis for any backend project.

The following Java technologies are combined:

* [Spark](http://sparkjava.com/) for HTTP API (embedding Jetty engine)
* [Google Guice](https://github.com/google/guice) for dependency injection
* [Jackson](https://github.com/FasterXML/jackson) as base JSON engine
* [TestNG](http://testng.org/doc/) + [Mockito](http://site.mockito.org/) for unit and integration testing
* [SLF4J](https://www.slf4j.org/) + [Logback](https://logback.qos.ch/) for app logging and test logging
* [PostgreSQL](https://jdbc.postgresql.org/) + [HikariCP](https://github.com/brettwooldridge/HikariCP) for production database
* [H2](http://www.h2database.com/html/main.html) for database unit testing
* [Sql2o](http://www.sql2o.org/) as POJO wrapper/unwrapper for JDBC 

In addition to that, the project is in Continous Integration on [Travis-CI](https://travis-ci.org/carlomorelli/project-muon) and uses [Coveralls](https://coveralls.io/github/carlomorelli/project-muon) to publish the code coverage results -- You can check out the banners at the top of this README for the current status on both.

Even if the Java technologies are many, a Fat-Jar version of Project-Muon weights **less than 12 Megabytes**, making the minification for possible microservices and Docker containers pretty easy.

## Configuration
The application uses a mixture of configuration file for handling the production database (at `src/main/resources/configuration.properties`), and a Guice module `AppConfig` for injecting the application and the test code.

## Build the project
Required for build are Java 8 JDK and Apache Maven 3.x.

Once the project is cloned, enter the checkout directory and run 
```
mvn clean package
```
This will build and unit-test the code and produce the fat-jar in the `target/` directory. Once this is done, the application can be simply run with
```
java -jar target/project-muon-0.0.1-SNAPSHOT.jar
```
To run the integration test suite, a local install of PostgreSQL is also needed. The application has been tested with version 9.4 and 9.5 on both Windows and Linux platforms.

To prepare the database service:
* Install PostgreSQL 9.x as a service
* Create a database with `createdb <dbname> -h localhost -U postgres`
* Submit the schema file with `psql -d <dbname> -a -f ./src/main/resources/db/schema.sql`

After the database service is prepared, integration tests can be run with
```
mvn clean verify
```

## HTTP api exposed
* GET `http://localhost:8080/webapi/items` - Get list of items and their details
* GET `http://localhost:8080/webapi/items/1` - Get details on single item with id 1
* POST `http://localhost:8080/webapi/items` - Submit new item

## To-dos
- [ ] Implement DELETE
- [ ] Dockerfile
- [ ] Integrate with Heroku
- [ ] Maven release plugin

Happy coding!
/Carlo




