FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/healthcare-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8004 5003

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5003","-jar","app.jar"]
