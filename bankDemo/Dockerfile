#Use a lightweight JRE for smaller images
FROM eclipse-temurin:21-jre-alpine
#ADD the pre-built JAR file
ADD target/bankDemo-0.0.1-SNAPSHOT.jar bankDemo-0.0.1-SNAPSHOT.jar
# Expose the application port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "/bankDemo-0.0.1-SNAPSHOT.jar"]