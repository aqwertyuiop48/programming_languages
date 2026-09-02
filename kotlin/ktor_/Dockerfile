# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . .
# Build the thin jar and extract dependencies into target/lib via CLI
RUN mvn clean package -DskipTests && \
    mvn dependency:copy-dependencies -DoutputDirectory=target/lib

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the thin JAR and the dependency folder
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/target/lib ./lib

ENV PORT=8080
EXPOSE 8080

# Execute using classpath mode pointing to your main class
ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "com.example.ApplicationKt"]