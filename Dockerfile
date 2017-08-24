FROM openjdk:8-jre-alpine

CMD mkdir -p /src/main/resources
COPY target/muon-app-0.1.1-SNAPSHOT.jar /muon-app.jar
#COPY src/main/resources/*.properties /src/main/resources/


ENV DB_TYPE="postgres-hikari"
ENV DB_HOSTNAME="localhost"
ENV DB_PORT="5432"
ENV DB_NAME="travis_ci_test"
ENV DB_USERNAME="postgres"
ENV DB_PASSWORD=""


CMD ["java", "-jar", "./muon-app.jar"]
