FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY build/libs/RestGym-1.0-SNAPSHOT.jar restgym.jar
ENTRYPOINT ["java","-jar","/restgym.jar"]