FROM openjdk:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file into the Docker image
COPY target/sportsScheduler-0.0.1-SNAPSHOT.jar app.jar

# Define the entry point for running the application
ENTRYPOINT ["java", "-jar", "app.jar"]
