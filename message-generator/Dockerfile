FROM openjdk:19
COPY target/message-generator.jar message-generator.jar
ENTRYPOINT ["java", "-jar", "/message-generator.jar", "--jasypt.encryptor.password=enc_key"

# Start with the OpenJDK 19 base image
FROM openjdk:19-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/message-generator.jar message-generator.jar

# Expose the port the application runs on
EXPOSE 8082

# Run the application with the necessary arguments
ENTRYPOINT ["java", "-jar", "/app/message-generator.jar", "--jasypt.encryptor.password=enc_key"]
