# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . .
# Build the app and copy the generated jar to a stable name so deploys do not fail
# when the exact jar filename changes or is not predictable across platforms.
RUN mvn clean package -DskipTests && \
    JAR_FILE=$(find target -maxdepth 1 -type f -name '*.jar' | head -n 1) && \
    cp "$JAR_FILE" /app/app.jar

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/app.jar /app/app.jar

ENV PORT=8080
EXPOSE 8080

# Run the built JAR directly; Render sets PORT at runtime.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]