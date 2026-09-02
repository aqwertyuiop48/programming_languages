# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests

# Pick exactly one jar and copy it under a fixed name
RUN JAR_FILE=$(find target -maxdepth 1 -type f -name "*.jar" ! -name "*.original" ! -name "*-sources.jar" ! -name "*-javadoc.jar" | head -n 1) && \
    cp "$JAR_FILE" /app/app.jar

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/app.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]