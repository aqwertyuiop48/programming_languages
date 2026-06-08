# Java Execution Methods in Programming Languages Repository

This document catalogues **all 15 different Java-only methods** discovered for building, running, and executing Java code throughout the repository. Non-Java methods (Kotlin, Scala, etc.) are excluded to focus on pure Java execution techniques with an emphasis on end-to-end execution rather than compilation-only steps.

Each method is intended to show a runnable Java execution path producing actual output, not just a separate compile step.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [javac + java](#11-javac--java)

2. **Maven Build System**
   - 2.1 [Maven Clean Install](#21-maven-clean-install)
   - 2.2 [Maven Spring Boot Run](#22-maven-spring-boot-run)
   - 2.3 [Maven Exec Plugin](#23-maven-exec-plugin)

3. **Gradle Build System**
   - 3.1 [Gradle Build & Run](#31-gradle-build--run)

4. **JShell (Interactive Java Shell)**
   - 4.1 [JShell Direct Execution](#41-jshell-direct-execution)
   - 4.2 [Maven Exec with JShell](#42-maven-exec-with-jshell)

5. **Java JAR Files**
   - 5.1 [Execute JAR Files](#51-execute-jar-files)

6. **IJava Jupyter Kernel**
   - 6.1 [IJava Notebook Installation & Execution](#61-ijava-notebook-installation--execution)

7. **Docker Containerization**
   - 7.1 [Docker Build & Run](#71-docker-build--run)

8. **Class Path Execution**
   - 8.1 [java -cp (With External Libraries)](#81-java--cp-with-external-libraries)

9. **Gradle Wrapper Scripts**
   - 9.1 [Gradlew (Gradle Wrapper)](#91-gradlew-gradle-wrapper)

10. **JUnit Testing**
    - 10.1 [JUnit with Maven](#101-junit-with-maven)
    - 10.2 [JUnit Console Launcher JAR](#102-junit-console-launcher-jar)

11. **Android Gradle Build System**
    - 11.1 [Android Gradle Plugin](#111-android-gradle-plugin)

12. **Maven Test**
    - 12.1 [mvn test (Run Unit Tests)](#121-mvn-test-run-unit-tests)

13. **Java ProcessBuilder (Programmatic Execution)**
    - 13.1 [ProcessBuilder Class](#131-processbuilder-class)

14. **Java Runtime.getRuntime() (Legacy Execution)**
    - 14.1 [Runtime.getRuntime().exec()](#141-runtimegetruntimeexec)

15. **Java with JVM Properties**
    - 15.1 [java -D (JVM System Properties)](#151-java--d-jvm-system-properties)

---

## 1. **Direct Compilation & Execution**

### 1.1 javac + java
**Method:** Direct compilation with `javac` followed by `java` command execution

**Locations:**
- [profiles/.bash_profile](../profiles/.bash_profile#L79) - Bash alias: `alias run_java='javac execute/_50A_py.java && java -cp execute _50A_py'`
- [profiles/bash_profile_windows.txt](../profiles/bash_profile_windows.txt#L136) - Windows bash alias
- [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L101-L102) - Direct compilation and execution
- [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L38) - With JUnit classpath
- [Python/codeforces_script/.github/workflows/main.yml](../Python/codeforces_script/.github/workflows/main.yml#L44) - Compiling multiple Java files
- [java/codeforces_script/.github/workflows/main.yml](../java/codeforces_script/.github/workflows/main.yml#L55) - With Guava library JAR

**Example:**
```bash
javac -cp ".:mysql-connector-j-9.3.0.jar" javaMysql.java
java -cp ".:mysql-connector-j-9.3.0.jar" javaMysql
```

---

## 2. **Maven Build System**

### 2.1 Maven Clean Install
**Method:** Standard Maven dependency resolution, build, and run

**Locations:**
- [AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml](../AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml#L41)
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L63)
- [java/cucumber_/codeforces_script/.github/workflows/main.yml](../java/cucumber_/codeforces_script/.github/workflows/main.yml#L44)
- [java/codeforces_script/.github/workflows/main_kotlin.yml](../java/codeforces_script/.github/workflows/main_kotlin.yml#L51)
- [java/angular_springboot/Expense_Tracker/Jenkinsfile](../java/angular_springboot/Expense_Tracker/Jenkinsfile#L31)

**Example:**
```bash
mvn clean package
java -jar target/app.jar
```

### 2.2 Maven Spring Boot Run
**Method:** Running Spring Boot application via Maven plugin

**Locations:**
- [java/spring_boot_rest_API_app_maven/Dockerfile](../java/spring_boot_rest_API_app_maven/Dockerfile#L116)
- [java/angular_springboot/spring-boot-angular-15-mysql-example/README.md](../java/angular_springboot/spring-boot-angular-15-mysql-example/README.md#L43)
- [java/angular_springboot/spring-boot-angular-15-mysql-example/spring-boot-server/README.md](../java/angular_springboot/spring-boot-angular-15-mysql-example/spring-boot-server/README.md#L103)

**Example:**
```bash
mvn spring-boot:run
```

### 2.3 Maven Exec Plugin
**Method:** Executing Java main class using Maven exec plugin

**Locations:**
- [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L50) - Kotlin Verticle execution
- [.github/workflows/kafka_.yml](../.github/workflows/kafka_.yml#L58-L63) - Kafka producer/consumer examples
- [scala/codeforces_script/.github/workflows/main_maven.yml](../scala/codeforces_script/.github/workflows/main_maven.yml#L34) - Scala with Maven
- [java/temporal/edu-101-java-code/README.md](../java/temporal/edu-101-java-code/README.md#L60-L61) - Temporal workflow examples
- [kotlin/http4j_/http4j_java/pom.xml](../kotlin/http4j_/http4j_java/pom.xml#L67) - HTTP4j framework
- [kotlin/helidon_/codeforces_script/pom.xml](../kotlin/helidon_/codeforces_script/pom.xml#L67) - Helidon framework

**Example:**
```bash
mvn exec:java -Dexec.mainClass="com.example.MainVerticleKt"
mvn exec:exec -Dexec.executable="jshell" -Dexec.args="-"
```

---

## 3. **Gradle Build System**

### 3.1 Gradle Build & Run
**Method:** Using Gradle wrapper or gradle build tool

**Locations:**
- [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L103) - Kotlin compilation with Gradle
- [scala/codeforces_script/.github/workflows/main_gradle.yml](../scala/codeforces_script/.github/workflows/main_gradle.yml#L37) - Scala with Gradle
- [java/micronaut_/codeforces_script/README.md](../java/micronaut_/codeforces_script/README.md#L24)
- [java/helidon/codeforces_script/README.md](../java/helidon/codeforces_script/README.md#L33)
- [kotlin/micronaut_/codeforces_script/README.md](../kotlin/micronaut_/codeforces_script/README.md#L32)
- [kotlin/helidon_/codeforces_script/README.md](../kotlin/helidon_/codeforces_script/README.md#L33)

**Example:**
```bash
./gradlew clean build
./gradlew run
gradle build && gradle run
```

---

## 4. **JShell (Interactive Java Shell)**

### 4.1 JShell Direct Execution
**Method:** Running Java code interactively via JShell

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L91) - Piping code to jshell
- [Python/codeforces_script/test.py](../Python/codeforces_script/test.py#L25) - JShell with classpath
- [kotlin/java_embed/codeforces_script/java_inMemory_inKotlin1.kt](../kotlin/java_embed/codeforces_script/java_inMemory_inKotlin1.kt#L78) - JShell in Kotlin

### 4.2 Maven Exec with JShell
**Method:** Using Maven exec plugin to run JShell

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L97)
- [kotlin/java_embed/codeforces_script/.github/workflows/main1.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main1.yml#L43-L44)

**Example:**
```bash
) | jshell -q -
mvn exec:exec -Dexec.executable=jshell -Dexec.args="-"
jshell --class-path "path/to/classpath" --startup /dev/stdin
```

---

## 5. **Java JAR Files**

### 5.1 Execute JAR Files
**Method:** Running compiled JAR files with `java -jar`

**Locations:**
- [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L44) - Vertx JAR execution
- [.github/workflows/sprint_boot_API_maven_kotlin.yml](../.github/workflows/sprint_boot_API_maven_kotlin.yml#L45) - Spring Boot JAR
- [.github/workflows/spring_boot_API_maven_java_applications.yml](../.github/workflows/spring_boot_API_maven_java_applications.yml#L58)
- [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L71) - CFR tool JAR execution
- [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml](../kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L38-L42) - Multiple JAR files
- [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L73-L76)
- [kotlin/quarkus_/README.md](../kotlin/quarkus_/README.md#L60)

**Example:**
```bash
java -jar target/vertx-application-1.0-SNAPSHOT.jar
java -jar target/*.jar
java -jar Main.jar
```

---

## 6. **IJava Jupyter Kernel**

### 6.1 IJava Notebook Installation & Execution
**Method:** Using IJava kernel in Jupyter notebooks for interactive Java

**Locations:**
- [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L44-L99) - IJava setup and inline execution
- [javascript/java_embed/codeforces_script/README.md](../javascript/java_embed/codeforces_script/README.md#L80-L86)
- [Python/codeforces_script/execute/notebook_java.ipynb](../Python/codeforces_script/execute/notebook_java.ipynb) - Jupyter notebook with IJava
- [Python/codeforces_script/execute/notebook_java2.ipynb](../Python/codeforces_script/execute/notebook_java2.ipynb)

**Example:**
```bash
curl -L https://github.com/SpencerPark/IJava/releases/download/v1.3.0/ijava-1.3.0.zip -o ijava-kernel.zip
unzip -q ijava-kernel.zip -d ijava-kernel && cd ijava-kernel && python3 install.py --sys-prefix
```

---

## 7. **Docker Containerization**

### 7.1 Docker Build & Run
**Method:** Building Docker images and running containerized Java applications

**Docker Files with Java Build:**
- [java/vertx_/Dockerfile](../java/vertx_/Dockerfile) - Vertx framework
- [java/quarkus_/Dockerfile](../java/quarkus_/Dockerfile) - Quarkus framework
- [java/micronaut_/codeforces_script/Dockerfile](../java/micronaut_/codeforces_script/Dockerfile) - Micronaut framework
- [java/javalin_/Dockerfile](../java/javalin_/Dockerfile) - Javalin framework
- [java/http4j_/http4j_java/Dockerfile](../java/http4j_/http4j_java/Dockerfile) - HTTP4j framework
- [java/helidon/codeforces_script/Dockerfile](../java/helidon/codeforces_script/Dockerfile) - Helidon framework
- [java/spring_boot_web_apps_gradle/java_applications/Dockerfile](../java/spring_boot_web_apps_gradle/java_applications/Dockerfile) - Spring Boot with Gradle
- [java/spring_boot_rest_API_app_maven/Dockerfile](../java/spring_boot_rest_API_app_maven/Dockerfile) - Spring Boot with Maven
- [java/angular_springboot/spring-boot-angular-15-mysql-example/.devcontainer/Dockerfile](../java/angular_springboot/spring-boot-angular-15-mysql-example/.devcontainer/Dockerfile)
- [java/angular_springboot/Expense_Tracker/Dockerfile](../java/angular_springboot/Expense_Tracker/Dockerfile)
- [java/algorithms/Java/.devcontainer/Dockerfile](../java/algorithms/Java/.devcontainer/Dockerfile)

**Workflow References:**
- [.github/workflows/docker1.yml](../.github/workflows/docker1.yml)
- [.github/workflows/docker2.yml](../.github/workflows/docker2.yml)
- [.github/workflows/docker3.yml](../.github/workflows/docker3.yml)

**Example:**
```dockerfile
COPY pom.xml ./
RUN mvn clean package
CMD ["java", "-jar", "target/app.jar"]
```

---

## 8. **Class Path Execution**

### 8.1 java -cp (With External Libraries)
**Method:** Running Java with specific classpath for dependencies

**Locations:**
- [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L102) - MySQL connector
- [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L47) - Custom classpath
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L40-L42)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L47-L48)

**Example:**
```bash
java -cp ".:mysql-connector-j-9.3.0.jar" javaMysql
java -cp bito__ Calculator
java -cp ".:${JARS}" Hello1
```

---

## 9. **Gradle Wrapper Scripts**

### 9.1 Gradlew (Gradle Wrapper)
**Method:** Using gradle wrapper for environment-independent builds and direct Java application execution

**Locations:**
- [java/micronaut_/codeforces_script/gradlew](../java/micronaut_/codeforces_script/gradlew) - Java Gradle wrapper script
- [kotlin/micronaut_/codeforces_script/gradlew](../kotlin/micronaut_/codeforces_script/gradlew) - Kotlin/Micronaut wrapper script
- [kotlin/algorithms/Kotlin/gradlew](../kotlin/algorithms/Kotlin/gradlew) - Kotlin wrapper script

**Example:**
```bash
./gradlew clean build
./gradlew run
```

---

## 10. **JUnit Testing**

### 10.1 JUnit with Maven
**Method:** Running JUnit tests through Maven

**Locations:**
- [AI_nization/bito1__/codeforces_script/.github/workflows/main1.yml](../AI_nization/bito1__/codeforces_script/.github/workflows/main1.yml#L35)
- [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L38)

**Example:**
```bash
javac -cp .:junit-platform-console-standalone-1.9.3.jar bito__/Calculator.java bito__/CalculatorTest.java
java -cp .:junit-platform-console-standalone-1.9.3.jar org.junit.platform.console.ConsoleLauncher ...
```

### 10.2 JUnit Console Launcher JAR
**Method:** Running JUnit tests directly via JUnit Console Launcher JAR

**Locations:**
- [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L33-L42) - Download and run JUnit standalone JAR

**Example:**
```bash
wget https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar
java -jar junit-platform-console-standalone-1.9.3.jar --class-path bito__ --scan-class-path
```

---

## 11. **Android Gradle Build System**

### 11.1 Android Gradle Plugin
**Method:** Building Android applications using Gradle and Android plugin

**Locations:**
- [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L61-L121) - Android testing with Gradle
- [java/android_/testing-samples/update_versions.sh](../java/android_/testing-samples/update_versions.sh#L45-L61)
- [java/android_/testing-samples/common_defs.bzl](../java/android_/testing-samples/common_defs.bzl) - Bazel build definitions

**Example:**
```bash
./gradlew assemble
```

---


## 12. **Maven Test**

### 12.1 mvn test (Run Unit Tests)
**Method:** Maven command to run JUnit tests in the project

**Locations:**
- [.github/workflows/spring_boot_web_apps_gradle_java_applications.yml](../.github/workflows/spring_boot_web_apps_gradle_java_applications.yml#L91)
- [.github/workflows/spring_boot_API_maven_java_applications.yml](../.github/workflows/spring_boot_API_maven_java_applications.yml#L66)
- [AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml](../AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml#L42)
- [java/selenium_/.github/workflows/main.yml](../java/selenium_/.github/workflows/main.yml#L46) - With test exclusions
- [java/cucumber_/codeforces_script/.github/workflows/main.yml](../java/cucumber_/codeforces_script/.github/workflows/main.yml#L48)
- [java/algorithms/Java/.github/workflows/run.yml](../java/algorithms/Java/.github/workflows/run.yml#L21)

**Example:**
```bash
mvn test
mvn test -Dtest='!AmazonTest,!AmazeTest'
```

---

## 13. **Java ProcessBuilder (Programmatic Execution)**

### 13.1 ProcessBuilder Class
**Method:** Executing Java code programmatically from within Java using ProcessBuilder API

**Locations:**
- [java/readme.txt](../java/readme.txt#L556-L560) - ProcessBuilder examples
- [java/codeforces_script/src/main/java/com/example/DataStructures.java](../java/codeforces_script/src/main/java/com/example/DataStructures.java#L113-L114) - Executing bash scripts
- [java/codeforces_script/execute1/rust_in_java.java](../java/codeforces_script/execute1/rust_in_java.java#L39)
- [java/codeforces_script/execute1/cpp_in_java.java](../java/codeforces_script/execute1/cpp_in_java.java#L50)
- [java/spring_boot_web_apps_gradle/java_applications/src/main/java/com/example/demo/HelloWorldController.java](../java/spring_boot_web_apps_gradle/java_applications/src/main/java/com/example/demo/HelloWorldController.java#L323) - ProcessBuilder in Spring Boot

**Example:**
```java
ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
pb.redirectErrorStream(true);
Process process = pb.start();
```

---

## 14. **Java Runtime.getRuntime() (Legacy Execution)**

### 14.1 Runtime.getRuntime().exec()
**Method:** Executing commands from within Java using the legacy Runtime API

**Locations:**
- [java/readme.txt](../java/readme.txt#L240) - Runtime execution examples
- [java/readme.txt](../java/readme.txt#L298)
- [java/readme.txt](../java/readme.txt#L446)
- [Python/codeforces_script/test.py](../Python/codeforces_script/test.py#L50) - Runtime in Python-embedded Java

**Example:**
```java
Runtime r = Runtime.getRuntime();
r.exec(new String[] { "python", "-c", command });
```

---


## 15. **Java with JVM Properties**

### 15.1 java -D (JVM System Properties)
**Method:** Running Java with system property flags for configuration

**Locations:**
- [QA/cypress_/codeforces_script/doc/jenkins.md](../QA/cypress_/codeforces_script/doc/jenkins.md#L22) - Jenkins WAR with file encoding

**Example:**
```bash
java -Dfile.encoding=UTF-8 -jar jenkins.war
java -Dproperty=value MainClass
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| javac + java | Simple compilation & execution | [profiles/.bash_profile](../profiles/.bash_profile#L79) |
| Maven | Dependency management & runtime execution | [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L50) |
| Gradle | Modern build system | [java/micronaut_/codeforces_script/README.md](../java/micronaut_/codeforces_script/README.md#L24) |
| JShell | Interactive Java execution | [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L91) |
| java -jar | JAR execution | [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L44) |
| IJava | Jupyter notebooks | [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L44) |
| Docker | Containerized execution | [java/vertx_/Dockerfile](../java/vertx_/Dockerfile) |
| java -cp | Classpath runtime execution | [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L102) |
| Gradlew | Wrapper-based runtime builds | [java/micronaut_/codeforces_script/gradlew](../java/micronaut_/codeforces_script/gradlew) |
| JUnit | Testing | [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L38) |
| Android Gradle | Mobile app builds | [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L61) |
| mvn test | Run unit tests | [.github/workflows/spring_boot_API_maven_java_applications.yml](../.github/workflows/spring_boot_API_maven_java_applications.yml#L66) |
| ProcessBuilder | Programmatic execution | [java/codeforces_script/src/main/java/com/example/DataStructures.java](../java/codeforces_script/src/main/java/com/example/DataStructures.java#L113) |
| Runtime.exec() | Legacy execution API | [java/readme.txt](../java/readme.txt#L240) |
| java -D | JVM properties | [QA/cypress_/codeforces_script/doc/jenkins.md](../QA/cypress_/codeforces_script/doc/jenkins.md#L22) |

---

## Key Frameworks & Tools Integrated

- **Spring Boot** - REST APIs and web applications
- **Quarkus** - High-performance Java framework
- **Vertx** - Event-driven reactive framework
- **Micronaut** - Lightweight framework
- **Javalin** - Simple web framework
- **Helidon** - Cloud-native framework
- **Temporal** - Workflow orchestration
- **Kafka** - Event streaming
- **Android SDK** - Mobile development
- **Selenium** - Web automation testing

---

**Last Updated:** June 8, 2026
**Repository:** c:\Users\Admin\Desktop\sreedhar\git4_\programming_languages
