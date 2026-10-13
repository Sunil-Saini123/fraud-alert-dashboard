# Runtime image: only a Java 21 JRE is needed to run the packaged application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the WAR built by "mvn clean package -DskipTests"
COPY target/*.war app.war

# The application listens on port 8081 (server.port in application.properties)
EXPOSE 8081

# Spring Boot WAR files are executable and contain their own embedded Tomcat
ENTRYPOINT ["java", "-jar", "app.war"]
