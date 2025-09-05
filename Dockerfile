FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/bankwebapp-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "bankwebapp-0.0.1-SNAPSHOT.jar"]