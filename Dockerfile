FROM openjdk:23-ea-17-jdk
WORKDIR /app
COPY ./target/tdd-billing-backend-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tdd-billing-backend-0.0.1-SNAPSHOT.jar"]