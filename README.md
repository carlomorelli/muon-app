# Muon

[![Build Status](https://travis-ci.org/carlomorelli/muon-app.svg?branch=master)](https://travis-ci.org/carlomorelli/muon-app)

[![Coverage Status](https://coveralls.io/repos/github/carlomorelli/muon-app/badge.svg?branch=master)](https://coveralls.io/github/carlomorelli/muon-app?branch=master)

Muon is a backend web application prototype.
It not is meant to be anything specific, but simply is a *demo integration* of multiple Java technologies, libraries and tools for the backend and to create the basis for any backend project.
The concept is somewhat inspired by [Dropwizard](www.dropwizard.io), but with less ambitions and hopefully with a clearer way to use.

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

The project is in Continuous Integration on [Travis-CI](https://travis-ci.org/carlomorelli/project-muon).
The [Cobertura Maven plugin](http://www.mojohaus.org/cobertura-maven-plugin/) and [Coveralls](https://coveralls.io/github/carlomorelli/project-muon) service are used for reporting the code coverage results; you can check out the `pom.xml` in the `<plugin>...</plugin>` section to understand how the service is integrated.

Even if the Java technologies employed are a few, a Uber-Jar version of Muon weights **less than 12 Megabytes**, making the minification for possible microservices and Docker containers pretty easy. We measured a start up time between 1 and 4 seconds including connection pooling to the database. Using the sample Dockerfile the size of the Docker image is about **90 Megabytes**. 

## Configuration
The application can be configured via Env variables or using file in classpath `src/main/resources/application.properties`. Please check out the wiki page [Configuration management](https://github.com/carlomorelli/muon-app/wiki/Configuration-management).

The integration tests follow the same configurations of the application (e.g. the database).

## Build the project
Required for build are Java 8 JDK and Apache Maven 3.x.

Once the project is cloned, enter the checkout directory and run 
```bash
mvn clean package
```
This will build and unit-test the code and produce the Uber-Jar in the `target/` directory. 

## Run the application
To run the application or the integration test, the requirement is to have a local install of *PostgreSQL 9.x* correctly setup. In order to prepare the database service, make reference to Appendix A.

Once the Uber-Jar is built, the application can be simply run with
```bash
java -jar target/muon-app-<version>.jar
```
To test the app, you can hit the version page and the `/webapi/items` API: if JSON content is returned, eveything is fine! On Linux or Windows Powershell:
```bash
curl localhost:8080/version
curl localhost:8080/webapi/items
```
To start the app on a different port than the default 8080, simply append the port on the launch command line. Example: `java -jar target/muon-app-0.0.1-SNAPSHOT.jar 9090` will start on port 9090.

Integration tests can be launched with 
```bash
mvn clean verify
```


## Run using Docker
The provided `Dockerfile` will allow to easily ramp up a Docker container. The tricky part is to configure communication between the application inside Docker and the database server running on the Host machine.

To build an image from the `Dockerfile`, first build the application normally (the Uber-Jar needs to be ready in directory `target/`), then run:
```bash
docker build -t muon-app-image .
```
To launch:
```bash
docker run --net=host -p 8080:8080 muon-app-image
```
The line `--net=host` part is needed for the muon app inside the docker container to access the Postgres service running in the Host OS.

Also in this case, try hitting the application's version page or the `/webapi/items/` API. If you see JSON content returned in both cases, everything is well. 

It is worth noting that the `Dockerfile` included uses the official OpenJDK-8-Alpine base images, which are very compact. Also, it is sufficient to use the JRE Runtime base image to run our built JAR inside the container. As mentioned earlier, a built `muon-app-image` will weight around 90 Megabytes as you can see:
```bash 
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
muon-app-image      latest              dba8dd844d49        23 minutes ago      92MB
openjdk             8-jre-alpine        c4f9d77cd2a1        7 weeks ago         81.4MB
```

 
## HTTP API reference
* GET `http://localhost:8080/version` - Get application version page
* GET `http://localhost:8080/webapi/items` - Get list of items and their details
* GET `http://localhost:8080/webapi/items/1` - Get details on single item with id 1
* POST `http://localhost:8080/webapi/items` - Submit new item


## Rationale
The idea behind Muon is simply to benchmark how small or large a web-application can be when based on the JDK when all minimal parts are assembled.

Many of the toolkits an libraries used in Muon are industry standard. The less known libraries used here are probably *Spark*, *Sql2o* and *RestAssured*.

Spark, even if it is not a widely used HTTP API library, is straightforward, lean, and contained: the web-app part of Muon is just `App` and `AbstractHandler` classes and the work needed to switch to Jersey/JAX-RS (for example) is minimal.

RestAssured is the greatest testing tool ever written: it allows to write REST integration tests in a very nice fluent DSL "given -> when -> then". It also integrates a nice JSON --> POJO unwrapping out of the box.

I chose not to use Hibernate or other ORM libraries, so the database is accessed through JDBC and SQL queries. Sql2o places itself between JDBC and the business logic: It wraps JDBC input and output streams with entity objects so the user does not need, for example, to navigate manually a ResultSet query and reconstruct manually the entity object retrieved. 


## Guides and reference documentation

* [Configuration management](https://github.com/carlomorelli/muon-app/wiki/Configuration-management)
* [Setting up PostgreSQL](https://github.com/carlomorelli/muon-app/wiki/Setting-up-PostgreSQL)
* [Running with Docker](https://github.com/carlomorelli/muon-app/wiki/Running-with-Docker)


Happy coding!

/Carlo


