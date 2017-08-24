FROM openjdk:8-jre-alpine

CMD mkdir -p /src/main/resources
COPY target/muon-app-*.jar /muon-app.jar

ENV DB_HOSTNAME="localhost"
ENV DB_PORT="5432"
ENV DB_NAME="travis_ci_test"
ENV DB_USERNAME="postgres"
ENV DB_PASSWORD=""
ENV DB_TYPE="postgres-hikari"

CMD ["java", "-jar", "./muon-app.jar"]
