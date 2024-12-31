# Start with the OpenJDK 19 base image
FROM openjdk:19-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/message-handler.jar message-handler.jar

# Expose the port the application runs on
EXPOSE 8081

# Run the application with the necessary arguments
ENTRYPOINT ["java", "-jar", "/app/message-handler.jar", "--jasypt.encryptor.password=enc_key"]
