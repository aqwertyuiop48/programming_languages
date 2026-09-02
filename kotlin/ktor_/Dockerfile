# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (better cache)
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy all source
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Isolate the exact fat JAR (with proper wildcard matching) and copy to a fixed path
RUN cp $(find target -maxdepth 1 -name "*.jar" ! -name "*.original" ! -name "*-sources.jar" ! -name "*-javadoc.jar") /app/app.jar

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the static, pre-resolved file. No wildcards mean this works identically on both Render and Back4App.
COPY --from=build /app/app.jar app.jar

# Render and Back4App set PORT, default 8080 locally
ENV PORT=8080
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]