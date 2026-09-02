# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -DskipTests -B

# Safely isolate the single fat JAR
RUN JAR_FILE=$(find target -maxdepth 1 -type f -name "*.jar" ! -name "*.original" ! -name "*-sources.jar" ! -name "*-javadoc.jar" | head -n 1) && \
    cp "$JAR_FILE" /app/app.jar

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install socat for external-to-internal port forwarding
RUN apt-get update && apt-get install -y socat && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/app.jar app.jar

ENV PORT=8080
EXPOSE 8080

# Start Java app in background, then forward external $PORT traffic to internal 127.0.0.1:8080
ENTRYPOINT ["/bin/sh", "-c", "java -jar app.jar & socat TCP-LISTEN:${PORT:-8080},fork,reuseaddr TCP:127.0.0.1:8080"]