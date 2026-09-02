# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (better cache)
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy all source
COPY . .

# Build and safely copy/rename the fat JAR (ignoring original/sources/javadoc variants)
RUN mvn clean package -DskipTests && \
    find target/ -maxdepth 1 -name "*.jar" ! -name "*sources*" ! -name "*javadoc*" ! -name "original*" -exec cp {} target/app.jar \;

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the exact single file (no wildcards, perfectly safe for all builders)
COPY --from=build /app/target/app.jar app.jar

# Render and Back4App will set PORT, default 8080 locally
ENV PORT=8080
EXPOSE 8080

# Start the Ktor app
ENTRYPOINT ["java", "-jar", "app.jar"]