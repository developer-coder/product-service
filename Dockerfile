# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port 4325 (this should match the port in docker-compose)
EXPOSE 4322

# List the files in the /app directory (this will help with debugging)
RUN ls -l /app

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
