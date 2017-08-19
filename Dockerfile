FROM openjdk:8-jre-alpine

CMD mkdir -p /src/main/resources
COPY target/muon-app-0.1.1-SNAPSHOT.jar /muon-app.jar
COPY src/main/resources/*.properties /src/main/resources/

CMD ["java", "-jar", "./muon-app.jar"]
