# Kotlin Execution Methods in Programming Languages Repository

This document catalogues **all distinct Kotlin-only methods** discovered for building, running, and executing Kotlin code throughout the repository. Non-Kotlin methods (pure Java, Scala, etc.) are excluded to focus on pure Kotlin execution techniques with an emphasis on end-to-end execution rather than compilation-only steps.

Each method takes Kotlin source code as input and produces the program's output. Intermediate steps (e.g., running a pre-built JAR, executing already-compiled `.class` files, dependency pre-fetching, bytecode decompilation) are not listed as separate methods; if a single command performs compile + run together, that counts as one method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [kotlinc + java -jar (Self-Contained JAR)](#11-kotlinc--java--jar-self-contained-jar)
   - 1.2 [kotlinc + kotlin -cp (Classpath Execution)](#12-kotlinc--kotlin--cp-classpath-execution)

2. **Kotlin Scripts (`.kts`)**
   - 2.1 [kotlinc -script](#21-kotlinc--script)
   - 2.2 [kotlin <file.kts>](#22-kotlin-filekts)
   - 2.3 [kscript (Shebang + Dependency-Aware Script Runner)](#23-kscript-shebang--dependency-aware-script-runner)

3. **Inline / REPL Execution**
   - 3.1 [kotlin -e (One-Liner)](#31-kotlin--e-one-liner)
   - 3.2 [kotlin -Xrepl / kotlinc -Xrepl (Piped REPL)](#32-kotlin--xrepl--kotlinc--xrepl-piped-repl)

4. **Kotlin Native**
   - 4.1 [kotlinc-native](#41-kotlinc-native)

5. **Gradle Build System**
   - 5.1 [Gradle Build & Run](#51-gradle-build--run)
   - 5.2 [Gradle Kotlin/JS (browserProductionWebpack)](#52-gradle-kotlinjs-browserproductionwebpack)

6. **Maven Build System**
   - 6.1 [Maven Clean Package + java -jar](#61-maven-clean-package--java--jar)
   - 6.2 [Maven Exec Plugin](#62-maven-exec-plugin)
   - 6.3 [Maven Quarkus Dev Mode (`mvn quarkus:dev`)](#63-maven-quarkus-dev-mode-mvn-quarkusdev)

7. **Docker Containerization**
   - 7.1 [Multi-Stage Docker Build & Run](#71-multi-stage-docker-build--run)

8. **Android Gradle Build System**
   - 8.1 [Android Gradle Plugin with Kotlin](#81-android-gradle-plugin-with-kotlin)

9. **Database-Connected Kotlin**
   - 9.1 [kotlinc + kotlin with MySQL Connector](#91-kotlinc--kotlin-with-mysql-connector)

10. **Cross-Language Interop**
    - 10.1 [Clojure ↔ Kotlin via Gradle](#101-clojure--kotlin-via-gradle)

11. **IKotlin Jupyter Kernel**
    - 11.1 [kotlin-jupyter-kernel (Inline + Notebook Execution)](#111-kotlin-jupyter-kernel-inline--notebook-execution)

---

## 1. **Direct Compilation & Execution**

### 1.1 kotlinc + java -jar (Self-Contained JAR)
**Method:** Compile `.kt` files into a runnable fat JAR with `-include-runtime`, then execute with `java -jar`

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L56-L73) - `kotlinc src/main/kotlin/com/example/Main.kt -include-runtime -d Main.jar` → `java -jar Main.jar`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L56-L73](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L56-L73)
- [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml](../kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L22-L29) - Multiple `kotlinc … -include-runtime` + `java -jar` patterns
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L22-L29](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/preinstalled.yml#L22-L29)
- [kotlin/java_embed/codeforces_script/.github/workflows/main1.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main1.yml#L83) - `kotlinc java_inMemory_inKotlin1.kt -include-runtime -d java_inMemory_inKotlin1.jar`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main1.yml#L83](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main1.yml#L83)

**Example:**
```bash
kotlinc src/main/kotlin/com/example/Main.kt -include-runtime -d Main.jar
java -jar Main.jar
```

### 1.2 kotlinc + kotlin -cp (Classpath Execution)
**Method:** Compile `.kt` sources to `.class` files, then run with `kotlin -cp` against the output directory

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/codeforces_script/.github/workflows/main.yml](../kotlin/codeforces_script/.github/workflows/main.yml#L44-L51) - `kotlinc -cp out/classes -d out/classes "$file" Utils.kt` → `kotlin -cp out/classes com.execute."$base_name"Kt`
  - Remote (submodule `kotlin/codeforces_script` @ branch `kotlin1_`): [kotlin/codeforces_script/.github/workflows/main.yml#L44-L51](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin1_/.github/workflows/main.yml#L44-L51)

**Example:**
```bash
kotlinc -cp out/classes -d out/classes MyFile.kt Utils.kt
kotlin -cp out/classes com.execute.MyFileKt
```

---

## 2. **Kotlin Scripts (`.kts`)**

### 2.1 kotlinc -script
**Method:** Execute a `.kts` script via the Kotlin compiler script mode

**Locations:**
- [kotlin/java_embed/codeforces_script/interviews/kotlin_/execution.sh](../kotlin/java_embed/codeforces_script/interviews/kotlin_/execution.sh#L1-L10) - `kotlinc -script intro.kts`, `kotlinc -script basics.kts`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/interviews/kotlin_/execution.sh#L1-L10](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/interviews/kotlin_/execution.sh#L1-L10)

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L67) - `kotlinc -script leetcode.kts`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L67](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L67)
- [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml](../kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L32) - `kotlinc -script leetcode.kts`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L32](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/preinstalled.yml#L32)

**Example:**
```bash
kotlinc -script leetcode.kts
kotlinc -script intro.kts
```

### 2.2 kotlin <file.kts>
**Method:** Run a `.kts` script directly via the `kotlin` runner

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml](../kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L52) - `kotlin leetcode.kts`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L52](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/preinstalled.yml#L52)
- [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L26-L37) - `kotlin Main.kts`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main_java.yml#L26-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L26-L37)

**Example:**
```bash
kotlin Main.kts
kotlin leetcode.kts
```

### 2.3 kscript (Shebang + Dependency-Aware Script Runner)
**Method:** Run a `.kts` file via [kscript](https://github.com/kscripting/kscript), which adds shebang support, dependency declarations (`@file:DependsOn(...)`), and caching on top of plain `kotlin`/`kotlinc -script`

**Locations:**
- [kotlin/java_embed/codeforces_script/hello.kts](../kotlin/java_embed/codeforces_script/hello.kts#L1) - `#!/usr/bin/env kscript` shebang with `println("Hello, KScript!")`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/hello.kts#L1](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/hello.kts#L1)

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L48) - `sdk install kscript`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L48](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L48)

**Example:**
```bash
sdk install kscript
chmod +x hello.kts
./hello.kts            # uses #!/usr/bin/env kscript shebang
# or invoke explicitly:
kscript hello.kts
```

---

## 3. **Inline / REPL Execution**

### 3.1 kotlin -e (One-Liner)
**Method:** Execute a single Kotlin expression inline with the `-e` flag

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml](../kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L47) - `kotlin -e "print(245400)"`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L47](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/preinstalled.yml#L47)
- [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L109) - `kotlin -cp "../../../commons-lang3-3.20.0.jar" -e 'import org.apache.commons.lang3.StringUtils; println(StringUtils.capitalize("hello kotlin"))'`

**Example:**
```bash
kotlin -e "print(245400)"
kotlin -cp "lib.jar" -e 'import org.apache.commons.lang3.StringUtils; println(StringUtils.capitalize("hi"))'
```

### 3.2 kotlin -Xrepl / kotlinc -Xrepl (Piped REPL)
**Method:** Pipe Kotlin code into the REPL via stdin using `-Xrepl`

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L80-L81) - `echo 'print(245)' | kotlin -Xrepl` and `echo 'print(2454)' | kotlinc -Xrepl`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L80-L81](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L80-L81)
- [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml](../kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L45-L46) - Same pipe pattern with both `kotlin` and `kotlinc`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/preinstalled.yml#L45-L46](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/preinstalled.yml#L45-L46)
- [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L165) - `… | kotlinc -Xrepl -cp "$jars" -d target/classes &`

**Example:**
```bash
echo 'print(245)' | kotlin -Xrepl
echo 'print(2454)' | kotlinc -Xrepl
```

---

## 4. **Kotlin Native**

### 4.1 kotlinc-native
**Method:** Compile Kotlin source to a native `.kexe` executable with `kotlinc-native`, then run directly

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L90-L96) - `kotlinc-native -version`, `kotlinc-native native.kt -o native-app`, then `./native-app.kexe`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L90-L96](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L90-L96)

**Example:**
```bash
kotlinc-native native.kt -o native-app
./native-app.kexe
```

---

## 5. **Gradle Build System**

### 5.1 Gradle Build & Run
**Method:** Build and execute Kotlin applications via the Gradle wrapper or system `gradle`

**Locations:**
- [kotlin/KotlinProject/build.gradle.kts](../kotlin/KotlinProject/build.gradle.kts#L30) - `mainClass.set("org.example.MainKt")` (run target for `./gradlew run`)
- [kotlin/javalin_/build.gradle.kts](../kotlin/javalin_/build.gradle.kts#L19-L20) - `mainClass.set("MainKt")` (Javalin run target)

**Workflow yml (executes in CI):**
- [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L103) - `./gradlew clean build && ./gradlew run` (`kotlin/KotlinProject`)
- [kotlin/algorithms/Kotlin/.github/workflows/build.yml](../kotlin/algorithms/Kotlin/.github/workflows/build.yml#L20) - `./gradlew build` (Kotlin algorithms)
  - Remote (submodule `kotlin/algorithms/Kotlin` @ branch `master`): [kotlin/algorithms/Kotlin/.github/workflows/build.yml#L20](https://github.com/aqwertyuiop48/Kotlin/blob/master/.github/workflows/build.yml#L20)
- [kotlin/micronaut_/codeforces_script/.github/workflows/main.yml](../kotlin/micronaut_/codeforces_script/.github/workflows/main.yml) - Micronaut Kotlin `./gradlew run`
  - Remote (submodule `kotlin/micronaut_/codeforces_script` @ branch `micronaut_kotlin_`): [kotlin/micronaut_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/micronaut_kotlin_/.github/workflows/main.yml)

**Example:**
```bash
./gradlew clean build
./gradlew run
gradle build && gradle run
```

### 5.2 Gradle Kotlin/JS (browserProductionWebpack)
**Method:** Build a Kotlin/JS project to browser-ready JavaScript bundles via the Gradle Kotlin/JS plugin

**Locations:**
- [kotlin/kotlin_js/build.gradle.kts](../kotlin/kotlin_js/build.gradle.kts) - Kotlin/JS Gradle DSL configuration

**Workflow yml (executes in CI):**
- [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L33) - `gradle browserProductionWebpack`

**Example:**
```bash
cd kotlin/kotlin_js
gradle browserProductionWebpack
mv build/js/packages/kotlin_js/kotlin/kotlin_js.js .
```

---

## 6. **Maven Build System**

### 6.1 Maven Clean Package + java -jar
**Method:** Build a Kotlin project with Maven (`kotlin-maven-plugin`) then execute the produced JAR

**Locations:**
- [kotlin/spring_boot/Dockerfile](../kotlin/spring_boot/Dockerfile#L13) - `RUN mvn clean package -DskipTests`
- [kotlin/ktor_/Dockerfile](../kotlin/ktor_/Dockerfile#L18) - `RUN mvn clean package -DskipTests`
- [kotlin/helidon_/codeforces_script/Dockerfile](../kotlin/helidon_/codeforces_script/Dockerfile#L9) - `RUN mvn package -DskipTests`
  - Remote (submodule `kotlin/helidon_/codeforces_script` @ branch `helidon_kotlin_`): [kotlin/helidon_/codeforces_script/Dockerfile#L9](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_kotlin_/Dockerfile#L9)
- [kotlin/quarkus_/Dockerfile](../kotlin/quarkus_/Dockerfile#L4) - `RUN mvn clean package -DskipTests`
- [kotlin/spring_boot/pom.xml](../kotlin/spring_boot/pom.xml#L104) - `kotlin-maven-plugin` compile config

**Workflow yml (executes in CI):**
- [.github/workflows/sprint_boot_API_maven_kotlin.yml](../.github/workflows/sprint_boot_API_maven_kotlin.yml#L42-L48) - `mvn clean package` → `java -jar target/*.jar`

**Example:**
```bash
cd kotlin/spring_boot
mvn clean package
java -jar target/*.jar
```

### 6.2 Maven Exec Plugin
**Method:** Run a Kotlin main class via Maven's `exec` plugin (Kt-suffixed class)

**Locations:**
- [kotlin/http4j_/http4j_java/pom.xml](../kotlin/http4j_/http4j_java/pom.xml#L67) - HTTP4j Kotlin exec config
  - Remote (submodule `kotlin/http4j_/http4j_java` @ branch `kotlin_`): [kotlin/http4j_/http4j_java/pom.xml#L67](https://github.com/aqwertyuiop48/http4j_java/blob/kotlin_/pom.xml#L67)
- [kotlin/helidon_/codeforces_script/pom.xml](../kotlin/helidon_/codeforces_script/pom.xml#L67) - Helidon Kotlin exec config
  - Remote (submodule `kotlin/helidon_/codeforces_script` @ branch `helidon_kotlin_`): [kotlin/helidon_/codeforces_script/pom.xml#L67](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_kotlin_/pom.xml#L67)
- [kotlin/vertx_/pom.xml](../kotlin/vertx_/pom.xml#L48) - Vertx Kotlin compile/exec config
- [kotlin/ktor_/pom.xml](../kotlin/ktor_/pom.xml#L52) - Ktor Kotlin compile/exec config
- [kotlin/quarkus_/pom.xml](../kotlin/quarkus_/pom.xml#L63) - Quarkus Kotlin compile/exec config

**Workflow yml (executes in CI):**
- [.github/workflows/vertx_.yml](../.github/workflows/vertx_.yml#L50) - Kotlin Verticle execution via `mvn exec:java -Dexec.mainClass="com.example.MainVerticleKt"`

**Example:**
```bash
mvn exec:java -Dexec.mainClass="com.example.MainVerticleKt"
```

### 6.3 Maven Quarkus Dev Mode (`mvn quarkus:dev`)
**Method:** Quarkus live-coding mode: Maven compiles Kotlin sources and runs the application in the foreground with hot-reload on source changes (single command = source-in, running app + output)

**Locations:**
- [kotlin/quarkus_/README.md](../kotlin/quarkus_/README.md#L36) - `./mvnw quarkus:dev`
- [quarkus_/README.md](../quarkus_/README.md#L36) - Same instructions (top-level Quarkus README)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/docker2.yml](.github/workflows/docker2.yml) _(rule R2)_ — covers `kotlin/quarkus_/README.md`

**Example:**
```bash
cd kotlin/quarkus_
./mvnw quarkus:dev
# Edits to .kt files under src/main/kotlin are picked up on the next request
```

---

## 7. **Docker Containerization**

### 7.1 Multi-Stage Docker Build & Run
**Method:** Build Kotlin applications inside a Docker image (Maven or Gradle stage) and run the resulting JAR

**Dockerfiles with Kotlin Build:**
- [kotlin/vertx_/Dockerfile](../kotlin/vertx_/Dockerfile) - Vertx Kotlin via Maven; `ENTRYPOINT ["java", "-jar", "/app/app.jar"]`
- [kotlin/spring_boot/Dockerfile](../kotlin/spring_boot/Dockerfile) - Spring Boot Kotlin via Maven
- [kotlin/ktor_/Dockerfile](../kotlin/ktor_/Dockerfile) - Ktor via Maven
- [kotlin/javalin_/Dockerfile](../kotlin/javalin_/Dockerfile) - Javalin Kotlin via Gradle shadow JAR
- [kotlin/quarkus_/Dockerfile](../kotlin/quarkus_/Dockerfile) - Quarkus Kotlin via Maven
- [kotlin/micronaut_/codeforces_script/Dockerfile](../kotlin/micronaut_/codeforces_script/Dockerfile) - Micronaut Kotlin via Gradle shadow JAR
  - Remote (submodule `kotlin/micronaut_/codeforces_script` @ branch `micronaut_kotlin_`): [kotlin/micronaut_/codeforces_script/Dockerfile](https://github.com/aqwertyuiop48/codeforces_script/blob/micronaut_kotlin_/Dockerfile)
- [kotlin/helidon_/codeforces_script/Dockerfile](../kotlin/helidon_/codeforces_script/Dockerfile) - Helidon Kotlin via Maven; `ENTRYPOINT ["java", "-jar", "app.jar"]`
  - Remote (submodule `kotlin/helidon_/codeforces_script` @ branch `helidon_kotlin_`): [kotlin/helidon_/codeforces_script/Dockerfile](https://github.com/aqwertyuiop48/codeforces_script/blob/helidon_kotlin_/Dockerfile)
- [kotlin/http4j_/http4j_java/Dockerfile](../kotlin/http4j_/http4j_java/Dockerfile) - HTTP4j Kotlin via Maven
  - Remote (submodule `kotlin/http4j_/http4j_java` @ branch `kotlin_`): [kotlin/http4j_/http4j_java/Dockerfile](https://github.com/aqwertyuiop48/http4j_java/blob/kotlin_/Dockerfile)

**Workflow yml (executes in CI):**
- [.github/workflows/docker_.yml](../.github/workflows/docker_.yml) — `docker build` for `kotlin/spring_boot/Dockerfile` (L29-L31), `kotlin/vertx_/Dockerfile` (L37-L39), `kotlin/ktor_/Dockerfile` (L45-L47)
- [.github/workflows/docker2.yml](../.github/workflows/docker2.yml) — `docker build` for `kotlin/javalin_/Dockerfile` (L29-L31), `kotlin/quarkus_/Dockerfile` (L38-L40)
- Submodule own-CI (R1): [kotlin/micronaut_/codeforces_script/.github/workflows/](../kotlin/micronaut_/codeforces_script/.github/workflows/) and [kotlin/http4j_/http4j_java/.github/workflows/](../kotlin/http4j_/http4j_java/.github/workflows/) — exercise their respective Dockerfiles on every push.
- [.github/workflows/main.yml](../.github/workflows/main.yml) — root bulk-sync workflow does `cd kotlin/helidon_/codeforces_script && git pull` (sync only; no `docker build`).

**Example:**
```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

---

## 8. **Android Gradle Build System**

### 8.1 Android Gradle Plugin with Kotlin
**Method:** Build Android applications written in Kotlin with the Android Gradle Plugin + `kotlin-gradle-plugin`

**Locations:**
- [java/android_/testing-samples/ui/espresso/BasicSample/build.gradle](../java/android_/testing-samples/ui/espresso/BasicSample/build.gradle#L13) - Uses `kotlin-gradle-plugin`
  - Remote (submodule `java/android_/testing-samples` @ branch `main`): [java/android_/testing-samples/ui/espresso/BasicSample/build.gradle#L13](https://github.com/aqwertyuiop48/testing-samples/blob/main/ui/espresso/BasicSample/build.gradle#L13)

**Workflow yml (executes in CI):**
- [java/android_/automated-build-android-app-with-github-action/.github/workflows/android-ci.yml](../java/android_/automated-build-android-app-with-github-action/.github/workflows/android-ci.yml#L48) - `./gradlew test` (Kotlin Android app CI)
  - Remote (submodule `java/android_/automated-build-android-app-with-github-action` @ branch `main`): [java/android_/automated-build-android-app-with-github-action/.github/workflows/android-ci.yml#L48](https://github.com/aqwertyuiop48/automated-build-android-app-with-github-action/blob/main/.github/workflows/android-ci.yml#L48)

**Example:**
```bash
./gradlew assemble
./gradlew test
```

---

## 9. **Database-Connected Kotlin**

### 9.1 kotlinc + kotlin with MySQL Connector
**Method:** Build a Kotlin program that links against an external JDBC driver, then run with the driver on the classpath

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L106-L109) - `kotlinc -cp ".:mysql-connector-j-9.3.0.jar" kotlinMysql.kt -include-runtime -d kotlinMysql.jar` then `kotlin -cp "kotlinMysql.jar:mysql-connector-j-9.3.0.jar" KotlinMysqlKt`

**Example:**
```bash
kotlinc -cp ".:mysql-connector-j-9.3.0.jar" kotlinMysql.kt -include-runtime -d kotlinMysql.jar
kotlin -cp "kotlinMysql.jar:mysql-connector-j-9.3.0.jar" KotlinMysqlKt
```

---

## 10. **Cross-Language Interop**

### 10.1 Clojure ↔ Kotlin via Gradle
**Method:** Compile Kotlin sources and consume them from Clojure (or vice versa) via Gradle build

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L57) - `kotlinc src/main/kotlin -d target/kotlin -classpath "$(clojure -Spath)"`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L57](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L57)
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L88-L89) - `./gradlew clean compileKotlin` then `./gradlew build run`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L88-L89](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L88-L89)

**Example:**
```bash
kotlinc src/main/kotlin -d target/kotlin -classpath "$(clojure -Spath)"
./gradlew clean compileKotlin
./gradlew build run
```

---

## 11. **IKotlin Jupyter Kernel**

### 11.1 kotlin-jupyter-kernel (Inline + Notebook Execution)
**Method:** Use the official Kotlin Jupyter kernel (`kotlin-jupyter-kernel`) either inline via `jupyter-console --kernel=kotlin <<EOF ... EOF` or by executing a `.ipynb` notebook with `jupyter nbconvert --execute --ExecutePreprocessor.kernel_name=kotlin`

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [Python/codeforces_script/.github/workflows/main_kotlin.yml](../Python/codeforces_script/.github/workflows/main_kotlin.yml#L14-L46) - `pip install kotlin-jupyter-kernel`, inline `jupyter-console --kernel=kotlin <<EOF` step, plus `jupyter nbconvert --execute notebook.ipynb`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main_kotlin.yml#L14-L46](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_kotlin.yml#L14-L46)

**Example:**
```bash
pip install --upgrade jupyter nbconvert kotlin-jupyter-kernel

# Inline
jupyter-console --kernel=kotlin <<EOF
println("Hello from IKotlin inline!")
println("Java version: " + System.getProperty("java.version"))
EOF

# Notebook
jupyter nbconvert --to notebook --execute notebook.ipynb \
  --ExecutePreprocessor.kernel_name=kotlin --output executed.ipynb
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| kotlinc + java -jar | Simple compilation & execution of `.kt` | [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L56)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L56) |
| kotlinc + kotlin -cp | Classpath execution after compilation | [kotlin/codeforces_script/.github/workflows/main.yml](../kotlin/codeforces_script/.github/workflows/main.yml#L44)<br/>[remote @ `kotlin1_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin1_/.github/workflows/main.yml#L44) |
| kotlinc -script | Run `.kts` script via compiler | [kotlin/java_embed/codeforces_script/interviews/kotlin_/execution.sh](../kotlin/java_embed/codeforces_script/interviews/kotlin_/execution.sh#L1)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/interviews/kotlin_/execution.sh#L1) |
| kotlin <file.kts> | Run `.kts` script directly | [Python/codeforces_script/.github/workflows/main_java.yml](../Python/codeforces_script/.github/workflows/main_java.yml#L26)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_java.yml#L26) |
| kscript | Shebang + dependency-aware `.kts` runner | [kotlin/java_embed/codeforces_script/hello.kts](../kotlin/java_embed/codeforces_script/hello.kts#L1)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/hello.kts#L1) |
| kotlin -e | Inline one-liner expression | [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L109) |
| kotlin -Xrepl | Piped REPL via stdin | [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L80)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L80) |
| kotlinc-native | Native binary compilation | [kotlin/java_embed/codeforces_script/.github/workflows/main.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main.yml#L90)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main.yml#L90) |
| Gradle build/run | Modern build system | [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L103) |
| Gradle Kotlin/JS | Browser bundle build | [.github/workflows/kotlin_js.yml](../.github/workflows/kotlin_js.yml#L33) |
| Maven clean package | Maven build + JAR | [.github/workflows/sprint_boot_API_maven_kotlin.yml](../.github/workflows/sprint_boot_API_maven_kotlin.yml#L42) |
| Maven exec plugin | Run Kotlin main class | [kotlin/http4j_/http4j_java/pom.xml](../kotlin/http4j_/http4j_java/pom.xml#L67)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/http4j_java/blob/kotlin_/pom.xml#L67) |
| mvn quarkus:dev | Quarkus live-coding mode | [kotlin/quarkus_/README.md](../kotlin/quarkus_/README.md#L36) |
| Docker multi-stage | Containerized execution | [kotlin/spring_boot/Dockerfile](../kotlin/spring_boot/Dockerfile) |
| Android Gradle | Mobile (Kotlin) builds | [java/android_/testing-samples/ui/espresso/BasicSample/build.gradle](../java/android_/testing-samples/ui/espresso/BasicSample/build.gradle#L13)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/testing-samples/blob/main/ui/espresso/BasicSample/build.gradle#L13) |
| MySQL connector | DB-connected Kotlin | [.github/workflows/java_mysql.yml](../.github/workflows/java_mysql.yml#L106) |
| Clojure interop | Cross-language Gradle build | [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L88)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L88) |
| IKotlin (Jupyter) | Inline + notebook execution via `kotlin-jupyter-kernel` | [Python/codeforces_script/.github/workflows/main_kotlin.yml](../Python/codeforces_script/.github/workflows/main_kotlin.yml#L14)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_kotlin.yml#L14) |

---

## Key Frameworks & Tools Integrated

- **Spring Boot (Kotlin)** - REST APIs and web applications (`kotlin/spring_boot/`)
- **Quarkus (Kotlin)** - High-performance Kotlin framework (`kotlin/quarkus_/`)
- **Vertx (Kotlin)** - Event-driven reactive framework (`kotlin/vertx_/`)
- **Micronaut (Kotlin)** - Lightweight Kotlin framework (`kotlin/micronaut_/codeforces_script/`)
- **Javalin (Kotlin)** - Simple web framework with Kotlin DSL (`kotlin/javalin_/`)
- **Helidon (Kotlin)** - Cloud-native Kotlin framework (`kotlin/helidon_/codeforces_script/`)
- **Ktor** - Kotlin-native asynchronous web framework (`kotlin/ktor_/`)
- **HTTP4j (Kotlin)** - Functional HTTP library (`kotlin/http4j_/http4j_java/`)
- **Kotlin/JS** - Compile Kotlin to browser-ready JavaScript (`kotlin/kotlin_js/`)
- **Kotlin Native** - Compile Kotlin to native executables (`kotlin/java_embed/codeforces_script/`)
- **Android SDK + Kotlin** - Mobile development (`java/android_/`)
- **kscript** - Shebang/dependency-aware `.kts` runner (`kotlin/java_embed/codeforces_script/hello.kts`)
- **kotlin-jupyter-kernel (IKotlin)** - Jupyter kernel for inline and notebook Kotlin execution (`Python/codeforces_script/.github/workflows/main_kotlin.yml`)

---

**Last Updated:** June 8, 2026
**Repository:** /workspaces/programming_languages
