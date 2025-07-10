FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/analisis-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]