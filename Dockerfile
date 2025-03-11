FROM openjdk:17-jdk-alpine

# Create a user and group
RUN addgroup -S spring && adduser -S spring -G spring

# Expose port 8080
EXPOSE 8080

# Copy the jar file and change ownership to the spring user
COPY build/libs/RestGym-1.0-SNAPSHOT.jar /app/restgym.jar
RUN chown spring:spring /app/restgym.jar

# Change to the spring user
USER spring

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/restgym.jar"]