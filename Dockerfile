# 🔥 Use Java 17 image
FROM eclipse-temurin:17-jdk

# 📁 Working directory
WORKDIR /app

# 📦 Copy JAR file
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# 🚀 Run application
ENTRYPOINT ["java", "-jar", "app.jar"]