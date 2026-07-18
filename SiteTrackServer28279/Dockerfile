# --- Build Stage ---
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /usr/src/app

# Copy pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy source code and build package
COPY src ./src
RUN mvn package -DskipTests

# --- Production Stage ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /usr/src/app

# Copy the built shadowed jar from build stage
COPY --from=build /usr/src/app/target/SiteTrackServer-1.0.0.jar ./sidetrack-server.jar

# Expose RMI registry port
EXPOSE 4567

# Start the RMI Server
CMD ["java", "-jar", "sidetrack-server.jar"]
