FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/bankwebapp-0.0.1-SNAPSHOT.jar /app/bankwebapp-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","-Dspring.profiles.active=docker","/app/bankwebapp-0.0.1-SNAPSHOT.jar"]