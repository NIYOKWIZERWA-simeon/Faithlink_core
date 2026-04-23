# Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Fix line endings and make mvnw executable (important for Windows users)
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

# Download dependencies (this caches them for faster subsequent builds)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/faithlinkcore-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render will automatically pick this up or map it via PORT env var)
EXPOSE 8082

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
