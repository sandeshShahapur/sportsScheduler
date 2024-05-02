FROM eclipse-temurin:21

# Set the working directory
WORKDIR /app

# Copy the JAR file into the Docker image
COPY target/*.jar app.jar

# Define the entry point for running the application
ENTRYPOINT ["java","-jar","/app.jar"]
