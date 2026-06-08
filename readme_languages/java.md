# Java Execution Methods in Programming Languages Repository

This document catalogues **all distinct Java-only methods** discovered for building, running, and executing Java code throughout the repository. Non-Java methods (Kotlin, Scala, etc.) are excluded to focus on pure Java execution techniques with an emphasis on end-to-end execution rather than compilation-only steps.

Each method takes Java source code as input and produces the program's output. Intermediate steps (e.g., running a pre-built JAR, executing already-compiled `.class` files, dependency pre-fetching, JVM-flag wrappers) are not listed as separate methods; if a single command performs compile + run together, that counts as one method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [javac + java](#11-javac--java)
   - 1.2 [java <file.java> (Single-File Source Launcher)](#12-java-filejava-single-file-source-launcher)
   - 1.3 [java --source with Zsh Process Substitution (Inline Heredoc)](#13-java---source-with-zsh-process-substitution-inline-heredoc)
   - 1.4 [java --source Shebang Script](#14-java---source-shebang-script)

2. **Maven Build System**
   - 2.1 [Maven Clean Install](#21-maven-clean-install)
   - 2.2 [Maven Spring Boot Run](#22-maven-spring-boot-run)
   - 2.3 [Maven Exec Plugin](#23-maven-exec-plugin)
   - 2.4 [Maven Quarkus Dev Mode (`mvn quarkus:dev`)](#24-maven-quarkus-dev-mode-mvn-quarkusdev)

3. **Gradle Build System**
   - 3.1 [Gradle Build & Run](#31-gradle-build--run)

4. **JShell (Interactive Java Shell)**
   - 4.1 [JShell Direct Execution](#41-jshell-direct-execution)
   - 4.2 [Maven Exec with JShell](#42-maven-exec-with-jshell)

5. **IJava Jupyter Kernel**
   - 5.1 [IJava Notebook Installation & Execution](#51-ijava-notebook-installation--execution)

6. **Docker Containerization**
   - 6.1 [Docker Build & Run](#61-docker-build--run)

7. **JUnit Testing**
   - 7.1 [JUnit with Maven](#71-junit-with-maven)

8. **Android Gradle Build System**
   - 8.1 [Android Gradle Plugin](#81-android-gradle-plugin)

9. **Maven Test**
   - 9.1 [mvn test (Run Unit Tests)](#91-mvn-test-run-unit-tests)

10. **Java ProcessBuilder (Programmatic Execution)**
    - 10.1 [ProcessBuilder Class](#101-processbuilder-class)

11. **Java Runtime.getRuntime() (Legacy Execution)**
    - 11.1 [Runtime.getRuntime().exec()](#111-runtimegetruntimeexec)

12. **JBang (Single-File Java Launcher)**
    - 12.1 [jbang <file.java>](#121-jbang-filejava)
    - 12.2 [jbang -c (One-Liner)](#122-jbang--c-one-liner)
    - 12.3 [jbang <alias>@<org> (Remote / Aliased Scripts)](#123-jbang-aliasorg-remote--aliased-scripts)

13. **Bazel Build System**
    - 13.1 [bazel test (Android Instrumentation Tests)](#131-bazel-test-android-instrumentation-tests)

---

## 1. **Direct Compilation & Execution**

### 1.1 javac + java
**Method:** Direct compilation with `javac` followed by `java` command execution

**Locations:**
- [profiles/.bash_profile](../profiles/.bash_profile#L79) - Bash alias: `alias run_java='javac execute/_50A_py.java && java -cp execute _50A_py'`
  - Remote (submodule `profiles` @ branch `main`): [profiles/.bash_profile#L79](https://github.com/aqwertyuiop48/profiles/blob/main/.bash_profile#L79)
- [profiles/bash_profile_windows.txt](../profiles/bash_profile_windows.txt#L136) - Windows bash alias
  - Remote (submodule `profiles` @ branch `main`): [profiles/bash_profile_windows.txt#L136](https://github.com/aqwertyuiop48/profiles/blob/main/bash_profile_windows.txt#L136)
- [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L101-L102) - Direct compilation and execution
- [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L38) - With JUnit classpath
  - Remote (submodule `AI_nization/codeforces_script` @ branch `bito_`): [AI_nization/codeforces_script/.github/workflows/main.yml#L38](https://github.com/aqwertyuiop48/codeforces_script/blob/bito_/.github/workflows/main.yml#L38)
- [Python/codeforces_script/.github/workflows/main.yml](../Python/codeforces_script/.github/workflows/main.yml#L44) - Compiling multiple Java files
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L44](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L44)
- [java/codeforces_script/.github/workflows/main.yml](../java/codeforces_script/.github/workflows/main.yml#L55) - With Guava library JAR
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/.github/workflows/main.yml#L55](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/.github/workflows/main.yml#L55)

**Example:**
```bash
javac -cp ".:mysql-connector-j-9.3.0.jar" javaMysql.java
java -cp ".:mysql-connector-j-9.3.0.jar" javaMysql
```

### 1.2 java <file.java> (Single-File Source Launcher)
**Method:** Java 11+ single-file source-code launcher — compile and run a `.java` file in one command without writing a `.class` file to disk (inline shell-friendly execution analogous to `kotlin <file.kts>`)

**Locations:**
- [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L25-L26) - `java MainOracle.java`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main_java.yml#L25-L26](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L25-L26)
- [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L36) - `java Main.java < input.txt` (stdin redirection)
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main_java.yml#L36](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L36)
- [typescript/inputs/shell_java.js](../typescript/inputs/shell_java.js#L79-L80) - Inline heredoc-style: `echo ${java_code} > /tmp/TmpClass.java && java /tmp/TmpClass.java`

**Example:**
```bash
java Main.java
java Main.java < input.txt
echo 'class Hi { public static void main(String[] a) { System.out.println("hi"); } }' > Hi.java && java Hi.java
```

### 1.3 java --source with Zsh Process Substitution (Inline Heredoc)
**Method:** Use zsh's `=( ... )` process substitution to feed an inline heredoc directly into the Java source launcher — no temp file written by the user, no manual cleanup. The Java code is embedded literally in the shell command (true inline-in-shell execution, analogous to `kotlin -e` for a full class)

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L36-L46) - `shell: zsh {0}` step with `java --source 21 =(cat <<'EOF' ... EOF)`
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L36-L46](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L36-L46)

**Example:**
```zsh
java --source 21 =(cat <<'EOF'
class A {
    public static void main(String[] args) {
        System.out.println("Zsh magic: No manual file cleanup needed!");
    }
}
EOF
)
```

### 1.4 java --source Shebang Script
**Method:** Make a `.sh` file executable with a `#!/usr/local/bin/java --source <N>` (or `#!/usr/bin/env -S java --source <N>`) shebang so the Java source itself becomes a runnable script accepting CLI args

**Locations:**
- [javascript/java_embed/codeforces_script/shebang_job.sh](../javascript/java_embed/codeforces_script/shebang_job.sh#L1) - `#!/usr/local/bin/java --source 21` with a `public class sum { public static void main(String[] args) { ... } }` body that sums CLI args
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/shebang_job.sh#L1](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/shebang_job.sh#L1)
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L68-L69) - `chmod +x shebang_job.sh && java --source 21 shebang_job.sh 1 3 5`
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L68-L69](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L68-L69)

**Example:**
```bash
chmod +x shebang_job.sh
java --source 21 shebang_job.sh 1 3 5
# or, if the shebang line is honoured directly:
./shebang_job.sh 1 3 5
```

---

## 2. **Maven Build System**

### 2.1 Maven Clean Install
**Method:** Standard Maven dependency resolution, build, and run

**Locations:**
- [AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml](../AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml#L41)
  - Remote (submodule `AI_nization/bito1__/codeforces_script` @ branch `bito1_`): [AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml#L41](https://github.com/aqwertyuiop48/codeforces_script/blob/bito1_/.github/workflows/maven.yml#L41)
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L63)
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L63](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L63)
- [java/cucumber_/codeforces_script/.github/workflows/main.yml](../java/cucumber_/codeforces_script/.github/workflows/main.yml#L44)
  - Remote (submodule `java/cucumber_/codeforces_script` @ branch `java_cucumber`): [java/cucumber_/codeforces_script/.github/workflows/main.yml#L44](https://github.com/aqwertyuiop48/codeforces_script/blob/java_cucumber/.github/workflows/main.yml#L44)
- [java/codeforces_script/.github/workflows/main_kotlin.yml](../java/codeforces_script/.github/workflows/main_kotlin.yml#L51)
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/.github/workflows/main_kotlin.yml#L51](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/.github/workflows/main_kotlin.yml#L51)
- [java/angular_springboot/Expense_Tracker/Jenkinsfile](../java/angular_springboot/Expense_Tracker/Jenkinsfile#L31)
  - Remote (submodule `java/angular_springboot/Expense_Tracker` @ branch `main`): [java/angular_springboot/Expense_Tracker/Jenkinsfile#L31](https://github.com/aqwertyuiop48/Expense_Tracker/blob/main/Jenkinsfile#L31)

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
  - Remote (submodule `java/angular_springboot/spring-boot-angular-15-mysql-example` @ branch `main`): [java/angular_springboot/spring-boot-angular-15-mysql-example/README.md#L43](https://github.com/aqwertyuiop48/spring-boot-angular-15-mysql-example/blob/main/README.md#L43)
- [java/angular_springboot/spring-boot-angular-15-mysql-example/spring-boot-server/README.md](../java/angular_springboot/spring-boot-angular-15-mysql-example/spring-boot-server/README.md#L103)
  - Remote (submodule `java/angular_springboot/spring-boot-angular-15-mysql-example` @ branch `main`): [java/angular_springboot/spring-boot-angular-15-mysql-example/spring-boot-server/README.md#L103](https://github.com/aqwertyuiop48/spring-boot-angular-15-mysql-example/blob/main/spring-boot-server/README.md#L103)

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
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main_maven.yml#L34](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_maven.yml#L34)
- [java/temporal/edu-101-java-code/README.md](../java/temporal/edu-101-java-code/README.md#L60-L61) - Temporal workflow examples
  - Remote (submodule `java/temporal/edu-101-java-code` @ branch `main`): [java/temporal/edu-101-java-code/README.md#L60-L61](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/README.md#L60-L61)
- [kotlin/http4j_/http4j_java/pom.xml](../kotlin/http4j_/http4j_java/pom.xml#L67) - HTTP4j framework
  - Remote (submodule `kotlin/http4j_/http4j_java` @ branch `kotlin_`): [kotlin/http4j_/http4j_java/pom.xml#L67](https://github.com/aqwertyuiop48/http4j_java/blob/kotlin_/pom.xml#L67)
- [kotlin/helidon_/codeforces_script/pom.xml](../kotlin/helidon_/codeforces_script/pom.xml#L67) - Helidon framework
  - Remote (submodule `kotlin/helidon_/codeforces_script` @ branch `helidon_kotlin_`): [kotlin/helidon_/codeforces_script/pom.xml#L67](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_kotlin_/pom.xml#L67)

**Example:**
```bash
mvn exec:java -Dexec.mainClass="com.example.MainVerticleKt"
mvn exec:exec -Dexec.executable="jshell" -Dexec.args="-"
```

### 2.4 Maven Quarkus Dev Mode (`mvn quarkus:dev`)
**Method:** Quarkus live-coding mode — the Maven Quarkus plugin compiles Java sources and runs the application in the foreground with hot-reload (single command = source-in, running app + output)

**Locations:**
- [java/quarkus_/README.md](../java/quarkus_/README.md#L36) - `./mvnw quarkus:dev`
- [quarkus_/README.md](../quarkus_/README.md#L36) - Same instructions (top-level Quarkus README)

**Example:**
```bash
cd java/quarkus_
./mvnw quarkus:dev
# Edits to .java files under src/main/java are picked up on the next request (live reload)
```

---

## 3. **Gradle Build System**

### 3.1 Gradle Build & Run
**Method:** Using Gradle wrapper or gradle build tool

**Locations:**
- [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L103) - Kotlin compilation with Gradle
- [scala/codeforces_script/.github/workflows/main_gradle.yml](../scala/codeforces_script/.github/workflows/main_gradle.yml#L37) - Scala with Gradle
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main_gradle.yml#L37](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_gradle.yml#L37)
- [java/micronaut_/codeforces_script/README.md](../java/micronaut_/codeforces_script/README.md#L24)
  - Remote (submodule `java/micronaut_/codeforces_script` @ branch `micronaut_java_`): [java/micronaut_/codeforces_script/README.md#L24](https://github.com/aqwertyuiop48/codeforces_script/blob/micronaut_java_/README.md#L24)
- [java/helidon/codeforces_script/README.md](../java/helidon/codeforces_script/README.md#L33)
  - Remote (submodule `java/helidon/codeforces_script` @ branch `helidon_java_`): [java/helidon/codeforces_script/README.md#L33](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_java_/README.md#L33)
- [kotlin/micronaut_/codeforces_script/README.md](../kotlin/micronaut_/codeforces_script/README.md#L32)
  - Remote (submodule `kotlin/micronaut_/codeforces_script` @ branch `micronaut_kotlin_`): [kotlin/micronaut_/codeforces_script/README.md#L32](https://github.com/aqwertyuiop48/codeforces_script/blob/micronaut_kotlin_/README.md#L32)
- [kotlin/helidon_/codeforces_script/README.md](../kotlin/helidon_/codeforces_script/README.md#L33)
  - Remote (submodule `kotlin/helidon_/codeforces_script` @ branch `helidon_kotlin_`): [kotlin/helidon_/codeforces_script/README.md#L33](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_kotlin_/README.md#L33)

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
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L91](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L91)
- [Python/codeforces_script/test.py](../Python/codeforces_script/test.py#L25) - JShell with classpath
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/test.py#L25](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/test.py#L25)
- [kotlin/java_embed/codeforces_script/java_inMemory_inKotlin1.kt](../kotlin/java_embed/codeforces_script/java_inMemory_inKotlin1.kt#L78) - JShell in Kotlin
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/java_inMemory_inKotlin1.kt#L78](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/java_inMemory_inKotlin1.kt#L78)

### 4.2 Maven Exec with JShell
**Method:** Using Maven exec plugin to run JShell

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L97)
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L97](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L97)
- [kotlin/java_embed/codeforces_script/.github/workflows/main1.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main1.yml#L43-L44)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main1.yml#L43-L44](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main1.yml#L43-L44)

**Example:**
```bash
) | jshell -q -
mvn exec:exec -Dexec.executable=jshell -Dexec.args="-"
jshell --class-path "path/to/classpath" --startup /dev/stdin
```

---

## 5. **IJava Jupyter Kernel**

### 5.1 IJava Notebook Installation & Execution
**Method:** Using the IJava kernel in Jupyter for interactive Java — supports both inline execution via `jupyter-console --kernel=java <<EOF ... EOF` and full notebook execution via `jupyter nbconvert --execute`

**Locations:**
- [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L44-L88) - IJava install, inline `jupyter-console --kernel=java <<EOF` step, and notebook execution via `nbconvert`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main_java.yml#L44-L88](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L44-L88)
- [javascript/java_embed/codeforces_script/README.md](../javascript/java_embed/codeforces_script/README.md#L80-L86)
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/README.md#L80-L86](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/README.md#L80-L86)
- [Python/codeforces_script/execute/notebook_java.ipynb](../Python/codeforces_script/execute/notebook_java.ipynb) - Jupyter notebook with IJava
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook_java.ipynb](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook_java.ipynb)
- [Python/codeforces_script/execute/notebook_java2.ipynb](../Python/codeforces_script/execute/notebook_java2.ipynb)
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook_java2.ipynb](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook_java2.ipynb)

**Example:**
```bash
curl -L https://github.com/SpencerPark/IJava/releases/download/v1.3.0/ijava-1.3.0.zip -o ijava-kernel.zip
unzip -q ijava-kernel.zip -d ijava-kernel && cd ijava-kernel && python3 install.py --sys-prefix

# Inline
jupyter-console --kernel=java <<EOF
System.out.println("Hello from IJava inline!");
System.out.println("Java version: " + System.getProperty("java.version"));
EOF

# Notebook
jupyter nbconvert --to notebook --execute notebook_java.ipynb --output executed_notebook_java.ipynb
```

---

## 6. **Docker Containerization**

### 6.1 Docker Build & Run
**Method:** Building Docker images and running containerized Java applications

**Docker Files with Java Build:**
- [java/vertx_/Dockerfile](../java/vertx_/Dockerfile) - Vertx framework
- [java/quarkus_/Dockerfile](../java/quarkus_/Dockerfile) - Quarkus framework
- [java/micronaut_/codeforces_script/Dockerfile](../java/micronaut_/codeforces_script/Dockerfile) - Micronaut framework
  - Remote (submodule `java/micronaut_/codeforces_script` @ branch `micronaut_java_`): [java/micronaut_/codeforces_script/Dockerfile](https://github.com/aqwertyuiop48/codeforces_script/blob/micronaut_java_/Dockerfile)
- [java/javalin_/Dockerfile](../java/javalin_/Dockerfile) - Javalin framework
- [java/http4j_/http4j_java/Dockerfile](../java/http4j_/http4j_java/Dockerfile) - HTTP4j framework
  - Remote (submodule `java/http4j_/http4j_java` @ branch `main`): [java/http4j_/http4j_java/Dockerfile](https://github.com/aqwertyuiop48/http4j_java/blob/main/Dockerfile)
- [java/helidon/codeforces_script/Dockerfile](../java/helidon/codeforces_script/Dockerfile) - Helidon framework
  - Remote (submodule `java/helidon/codeforces_script` @ branch `helidon_java_`): [java/helidon/codeforces_script/Dockerfile](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_java_/Dockerfile)
- [java/spring_boot_web_apps_gradle/java_applications/Dockerfile](../java/spring_boot_web_apps_gradle/java_applications/Dockerfile) - Spring Boot with Gradle
- [java/spring_boot_rest_API_app_maven/Dockerfile](../java/spring_boot_rest_API_app_maven/Dockerfile) - Spring Boot with Maven
- [java/angular_springboot/spring-boot-angular-15-mysql-example/.devcontainer/Dockerfile](../java/angular_springboot/spring-boot-angular-15-mysql-example/.devcontainer/Dockerfile)
  - Remote (submodule `java/angular_springboot/spring-boot-angular-15-mysql-example` @ branch `main`): [java/angular_springboot/spring-boot-angular-15-mysql-example/.devcontainer/Dockerfile](https://github.com/aqwertyuiop48/spring-boot-angular-15-mysql-example/blob/main/.devcontainer/Dockerfile)
- [java/angular_springboot/Expense_Tracker/Dockerfile](../java/angular_springboot/Expense_Tracker/Dockerfile)
  - Remote (submodule `java/angular_springboot/Expense_Tracker` @ branch `main`): [java/angular_springboot/Expense_Tracker/Dockerfile](https://github.com/aqwertyuiop48/Expense_Tracker/blob/main/Dockerfile)
- [java/algorithms/Java/.devcontainer/Dockerfile](../java/algorithms/Java/.devcontainer/Dockerfile)
  - Remote (submodule `java/algorithms/Java` @ branch `master`): [java/algorithms/Java/.devcontainer/Dockerfile](https://github.com/aqwertyuiop48/Java/blob/master/.devcontainer/Dockerfile)

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

## 7. **JUnit Testing**

### 7.1 JUnit with Maven
**Method:** Running JUnit tests through Maven

**Locations:**
- [AI_nization/bito1__/codeforces_script/.github/workflows/main1.yml](../AI_nization/bito1__/codeforces_script/.github/workflows/main1.yml#L35)
  - Remote (submodule `AI_nization/bito1__/codeforces_script` @ branch `bito1_`): [AI_nization/bito1__/codeforces_script/.github/workflows/main1.yml#L35](https://github.com/aqwertyuiop48/codeforces_script/blob/bito1_/.github/workflows/main1.yml#L35)
- [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L38)
  - Remote (submodule `AI_nization/codeforces_script` @ branch `bito_`): [AI_nization/codeforces_script/.github/workflows/main.yml#L38](https://github.com/aqwertyuiop48/codeforces_script/blob/bito_/.github/workflows/main.yml#L38)

**Example:**
```bash
javac -cp .:junit-platform-console-standalone-1.9.3.jar bito__/Calculator.java bito__/CalculatorTest.java
java -cp .:junit-platform-console-standalone-1.9.3.jar org.junit.platform.console.ConsoleLauncher ...
```

---

## 8. **Android Gradle Build System**

### 8.1 Android Gradle Plugin
**Method:** Building Android applications using Gradle and Android plugin

**Locations:**
- [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L61-L121) - Android testing with Gradle
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/README.md#L61-L121](https://github.com/aqwertyuiop48/testing-samples/blob/main/README.md#L61-L121)
- [java/android_/testing-samples/update_versions.sh](../java/android_/testing-samples/update_versions.sh#L45-L61)
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/update_versions.sh#L45-L61](https://github.com/aqwertyuiop48/testing-samples/blob/main/update_versions.sh#L45-L61)
- [java/android_/testing-samples/common_defs.bzl](../java/android_/testing-samples/common_defs.bzl) - Bazel build definitions
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/common_defs.bzl](https://github.com/aqwertyuiop48/testing-samples/blob/main/common_defs.bzl)

**Example:**
```bash
./gradlew assemble
```

---


## 9. **Maven Test**

### 9.1 mvn test (Run Unit Tests)
**Method:** Maven command to run JUnit tests in the project

**Locations:**
- [.github/workflows/spring_boot_web_apps_gradle_java_applications.yml](../.github/workflows/spring_boot_web_apps_gradle_java_applications.yml#L91)
- [.github/workflows/spring_boot_API_maven_java_applications.yml](../.github/workflows/spring_boot_API_maven_java_applications.yml#L66)
- [AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml](../AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml#L42)
  - Remote (submodule `AI_nization/bito1__/codeforces_script` @ branch `bito1_`): [AI_nization/bito1__/codeforces_script/.github/workflows/maven.yml#L42](https://github.com/aqwertyuiop48/codeforces_script/blob/bito1_/.github/workflows/maven.yml#L42)
- [java/selenium_/.github/workflows/main.yml](../java/selenium_/.github/workflows/main.yml#L46) - With test exclusions
  - Remote (submodule `java/selenium_` @ branch `main`): [java/selenium_/.github/workflows/main.yml#L46](https://github.com/aqwertyuiop48/selenium_java/blob/main/.github/workflows/main.yml#L46)
- [java/cucumber_/codeforces_script/.github/workflows/main.yml](../java/cucumber_/codeforces_script/.github/workflows/main.yml#L48)
  - Remote (submodule `java/cucumber_/codeforces_script` @ branch `java_cucumber`): [java/cucumber_/codeforces_script/.github/workflows/main.yml#L48](https://github.com/aqwertyuiop48/codeforces_script/blob/java_cucumber/.github/workflows/main.yml#L48)
- [java/algorithms/Java/.github/workflows/run.yml](../java/algorithms/Java/.github/workflows/run.yml#L21)
  - Remote (submodule `java/algorithms/Java` @ branch `master`): [java/algorithms/Java/.github/workflows/run.yml#L21](https://github.com/aqwertyuiop48/Java/blob/master/.github/workflows/run.yml#L21)

**Example:**
```bash
mvn test
mvn test -Dtest='!AmazonTest,!AmazeTest'
```

---

## 10. **Java ProcessBuilder (Programmatic Execution)**

### 10.1 ProcessBuilder Class
**Method:** Executing Java code programmatically from within Java using ProcessBuilder API

**Locations:**
- [java/readme.txt](../java/readme.txt#L556-L560) - ProcessBuilder examples
- [java/codeforces_script/src/main/java/com/example/DataStructures.java](../java/codeforces_script/src/main/java/com/example/DataStructures.java#L113-L114) - Executing bash scripts
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/src/main/java/com/example/DataStructures.java#L113-L114](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L113-L114)
- [java/codeforces_script/execute1/rust_in_java.java](../java/codeforces_script/execute1/rust_in_java.java#L39)
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/execute1/rust_in_java.java#L39](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/execute1/rust_in_java.java#L39)
- [java/codeforces_script/execute1/cpp_in_java.java](../java/codeforces_script/execute1/cpp_in_java.java#L50)
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/execute1/cpp_in_java.java#L50](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/execute1/cpp_in_java.java#L50)
- [java/spring_boot_web_apps_gradle/java_applications/src/main/java/com/example/demo/HelloWorldController.java](../java/spring_boot_web_apps_gradle/java_applications/src/main/java/com/example/demo/HelloWorldController.java#L323) - ProcessBuilder in Spring Boot

**Example:**
```java
ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
pb.redirectErrorStream(true);
Process process = pb.start();
```

---

## 11. **Java Runtime.getRuntime() (Legacy Execution)**

### 11.1 Runtime.getRuntime().exec()
**Method:** Executing commands from within Java using the legacy Runtime API

**Locations:**
- [java/readme.txt](../java/readme.txt#L240) - Runtime execution examples
- [java/readme.txt](../java/readme.txt#L298)
- [java/readme.txt](../java/readme.txt#L446)
- [Python/codeforces_script/test.py](../Python/codeforces_script/test.py#L50) - Runtime in Python-embedded Java
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/test.py#L50](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/test.py#L50)

**Example:**
```java
Runtime r = Runtime.getRuntime();
r.exec(new String[] { "python", "-c", command });
```

---

## 12. **JBang (Single-File Java Launcher)**

### 12.1 jbang <file.java>
**Method:** Run a `.java` file directly with [JBang](https://www.jbang.dev) — like the built-in `java <file.java>` launcher but with dependency declarations via `//DEPS` comments and automatic JDK provisioning

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L70) - `jbang Mains.java`
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L70](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L70)

**Example:**
```bash
jbang Mains.java
```

### 12.2 jbang -c (One-Liner)
**Method:** Execute an inline Java expression/statement directly from the shell — the true Java analogue of `kotlin -e`

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L67) - `jbang -c 'System.out.println(System.getProperty("java.version"));'`
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L67](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L67)

**Example:**
```bash
jbang -c 'System.out.println(System.getProperty("java.version"));'
```

### 12.3 jbang <alias>@<org> (Remote / Aliased Scripts)
**Method:** Resolve and run a named Java script from a remote catalog (the source is fetched, compiled, and executed in one command)

**Locations:**
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L54) - `jbang properties@jbangdev`
  - Remote (submodule `javascript/java_embed/codeforces_script` @ branch `java_`): [javascript/java_embed/codeforces_script/.github/workflows/main.yml#L54](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L54)

**Example:**
```bash
jbang properties@jbangdev
```

---

## 13. **Bazel Build System**

### 13.1 bazel test (Android Instrumentation Tests)
**Method:** Use Bazel to build and run Android instrumentation tests written in Java in one command — source `.java` files declared in `BUILD.bazel` targets are compiled and executed against an emulator/device (`bazel test //...`)

**Locations:**
- [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L92) - `bazel test //... --config=headless`
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/README.md#L92](https://github.com/aqwertyuiop48/testing-samples/blob/main/README.md#L92)
- [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L95) - `bazel test //ui/uiautomator/BasicSample:BasicSampleInstrumentationTest_21_x86 --config=headless` (single target)
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/README.md#L95](https://github.com/aqwertyuiop48/testing-samples/blob/main/README.md#L95)
- [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L112-L118) - `--config=gui` and `--config=local_device` variants
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/README.md#L112-L118](https://github.com/aqwertyuiop48/testing-samples/blob/main/README.md#L112-L118)
- [java/android_/testing-samples/ui/uiautomator/BasicSample/BUILD.bazel](../java/android_/testing-samples/ui/uiautomator/BasicSample/BUILD.bazel#L48) - `android_instrumentation_test` target declaration
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/ui/uiautomator/BasicSample/BUILD.bazel#L48](https://github.com/aqwertyuiop48/testing-samples/blob/main/ui/uiautomator/BasicSample/BUILD.bazel#L48)

**Example:**
```bash
cd java/android_/testing-samples
# Edit WORKSPACE to point at your local Android SDK
bazel test //... --config=headless
bazel test //ui/uiautomator/BasicSample:BasicSampleInstrumentationTest_21_x86 --config=headless
bazel query 'kind(android_instrumentation_test, //...)'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| javac + java | Simple compilation & execution | [profiles/.bash_profile](../profiles/.bash_profile#L79)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/profiles/blob/main/.bash_profile#L79) |
| java <file.java> | Single-file source launcher (compile + run in one command) | [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L25)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L25) |
| java --source + zsh `=(cat <<EOF)` | Inline Java in zsh via process substitution | [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L36)<br/>[remote @ `java_`](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L36) |
| java --source shebang | Executable `.sh` with Java body | [javascript/java_embed/codeforces_script/shebang_job.sh](../javascript/java_embed/codeforces_script/shebang_job.sh#L1)<br/>[remote @ `java_`](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/shebang_job.sh#L1) |
| Maven | Dependency management & runtime execution | [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L50) |
| mvn quarkus:dev | Quarkus live-coding mode | [java/quarkus_/README.md](../java/quarkus_/README.md#L36) |
| Gradle | Modern build system | [java/micronaut_/codeforces_script/README.md](../java/micronaut_/codeforces_script/README.md#L24)<br/>[remote @ `micronaut_java_`](https://github.com/aqwertyuiop48/codeforces_script/blob/micronaut_java_/README.md#L24) |
| JShell | Interactive Java execution | [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L91)<br/>[remote @ `java_`](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L91) |
| IJava | Jupyter notebooks | [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L44)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L44) |
| Docker | Containerized execution | [java/vertx_/Dockerfile](../java/vertx_/Dockerfile) |
| JUnit | Testing | [AI_nization/codeforces_script/.github/workflows/main.yml](../AI_nization/codeforces_script/.github/workflows/main.yml#L38)<br/>[remote @ `bito_`](https://github.com/aqwertyuiop48/codeforces_script/blob/bito_/.github/workflows/main.yml#L38) |
| Android Gradle | Mobile app builds | [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L61)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/testing-samples/blob/main/README.md#L61) |
| mvn test | Run unit tests | [.github/workflows/spring_boot_API_maven_java_applications.yml](../.github/workflows/spring_boot_API_maven_java_applications.yml#L66) |
| ProcessBuilder | Programmatic execution | [java/codeforces_script/src/main/java/com/example/DataStructures.java](../java/codeforces_script/src/main/java/com/example/DataStructures.java#L113)<br/>[remote @ `javac_`](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L113) |
| Runtime.exec() | Legacy execution API | [java/readme.txt](../java/readme.txt#L240) |
| JBang | Single-file launcher, one-liner (`jbang -c`), remote aliased scripts | [javascript/java_embed/codeforces_script/.github/workflows/main.yml](../javascript/java_embed/codeforces_script/.github/workflows/main.yml#L67)<br/>[remote @ `java_`](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L67) |
| Bazel | Build + run Android instrumentation tests | [java/android_/testing-samples/README.md](../java/android_/testing-samples/README.md#L92)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/testing-samples/blob/main/README.md#L92) |

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
- **JBang** - Single-file Java launcher with `//DEPS`, one-liner mode (`jbang -c`), and remote/aliased scripts
- **Bazel** - Build system used for Android Java instrumentation tests (`bazel test //...`)

---

**Last Updated:** June 8, 2026
**Repository:** c:\Users\Admin\Desktop\sreedhar\git4_\programming_languages
