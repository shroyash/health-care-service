
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven built JAR into container
COPY target/healthcare-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8004 55005

# Run the JAR with remote debugging enabled
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","app.jar"]




