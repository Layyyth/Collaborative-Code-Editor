# Use a lightweight JDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the Spring Boot app using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/collaborativecodeeditor-0.0.1-SNAPSHOT.jar"]
