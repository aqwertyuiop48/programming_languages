# Scala 2 Execution Methods in Programming Languages Repository

This document catalogues **all distinct Scala 2 execution methods** discovered for building, running, and executing Scala 2.13.x code throughout the repository. Scala 3 methods are catalogued separately in [scala3.md](scala3.md) because the implementations live in separate folders (`interviews/scala2_/` vs `interviews/scala3_/`) and rely on different toolchain provisioning (e.g. Scala 2 is installable via `apt`, Scala 3 is not).

Each method takes Scala 2 source code (`.scala` or `.sc`) as input and produces the program's output. Intermediate steps (toolchain installation alone, dependency-only fetches, run-only steps on a pre-built artifact) are not listed as separate methods; if a single command performs compile + run together, that counts as one method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [scalac + scala (System-Installed Toolchain)](#11-scalac--scala-system-installed-toolchain)
   - 1.2 [scalac + java (Coursier-Fetched Classpath)](#12-scalac--java-coursier-fetched-classpath)

2. **Interpreted / Inline Execution**
   - 2.1 [scala <file.scala> (Interpreted Source)](#21-scala-filescala-interpreted-source)
   - 2.2 [scala -e (Inline Expression)](#22-scala--e-inline-expression)
   - 2.3 [echo '...' | scala -Dscala.repl.no-tty=true (Piped REPL)](#23-echo---scala--dscalareplno-ttytrue-piped-repl)

3. **Scala Scripts (`.sc`)**
   - 3.1 [scala <file.sc> (Coursier-Installed Launcher)](#31-scala-filesc-coursier-installed-launcher)

4. **scala-cli (Modern Single-File Runner)**
   - 4.1 [scala-cli run <file.scala> / <file.sc>](#41-scala-cli-run-filescala--filesc)
   - 4.2 [scala-cli repl <<EOF (Heredoc REPL)](#42-scala-cli-repl-eof-heredoc-repl)
   - 4.3 [scala-cli --scala -e (Inline Expression)](#43-scala-cli---scala--e-inline-expression)

5. **Ammonite**
   - 5.1 [amm <file.sc>](#51-amm-filesc)
   - 5.2 [amm -c '...' (Batch/REPL Mode)](#52-amm--c--batchrepl-mode)

6. **sbt**
   - 6.1 [sbt run](#61-sbt-run)
   - 6.2 [sbt test](#62-sbt-test)
   - 6.3 [sbt console <<< (Piped Heredoc)](#63-sbt-console--piped-heredoc)

7. **Gradle (scala Plugin)**
   - 7.1 [./gradlew build && ./gradlew run](#71-gradlew-build--gradlew-run)

8. **Maven (scala-maven-plugin)**
   - 8.1 [mvn clean compile + mvn exec:java](#81-mvn-clean-compile--mvn-execjava)

9. **GitHub Action Setups (Toolchain Provisioning Wrappers)**
   - 9.1 [VirtusLab/scala-cli-setup@v1](#91-virtuslabscala-cli-setupv1)
   - 9.2 [coursier/setup-action@v1 + cs install](#92-coursiersetup-actionv1--cs-install)
   - 9.3 [olafurpg/setup-scala@v12](#93-olafurpgsetup-scalav12)
   - 9.4 [sdk install scala (SDKMAN)](#94-sdk-install-scala-sdkman)
   - 9.5 [sudo apt install -y scala](#95-sudo-apt-install--y-scala)

---

## 1. **Direct Compilation & Execution**

### 1.1 scalac + scala (System-Installed Toolchain)
**Method:** Compile a `.scala` source to JVM `.class` files with `scalac`, then run the resulting object/class with `scala`. The Scala 2 toolchain can be obtained via `apt`, `sdk install scala 2.13.x`, or `cs install scala:2.13.x` (see §9 for provisioning).

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L36-L37) - `apt`-installed Scala 2 then `scalac Hello1.scala && scala Hello1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L36-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L36-L37)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L65-L66) - SDKMAN-installed Scala 2.13.12 then `scalac Hello1.scala && scala Hello1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L65-L66](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L65-L66)

**Example:**
```bash
sudo apt install -y scala            # OR  sdk install scala 2.13.12  OR  cs install scala:2.13.12
scalac Hello1.scala && scala Hello1
scalac Hello2.scala && scala Hello2
```

### 1.2 scalac + java (Coursier-Fetched Classpath)
**Method:** Compile with `scalac` and run with plain `java -cp`, providing the Scala 2 standard library JARs explicitly via Coursier's `cs fetch`. Avoids depending on a `scala` launcher being on the PATH.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L45-L48) - `JARS=$(cs fetch org.scala-lang:scala-library:2.13.12 | tr '\n' ':')` then `scalac Hello1.scala && java -cp ".:${JARS}" Hello1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L45-L48](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L45-L48)

**Example:**
```bash
JARS=$(cs fetch org.scala-lang:scala-library:2.13.12 | tr '\n' ':')
scalac Hello1.scala && java -cp ".:${JARS}" Hello1
```

---

## 2. **Interpreted / Inline Execution**

### 2.1 scala <file.scala> (Interpreted Source)
**Method:** Pass a `.scala` source file directly to the `scala` launcher; it compiles and runs in one step without producing a persistent `.class`.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L40-L41) - apt-installed Scala 2: `scala Hello1.scala`, `scala Hello2.scala`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L40-L41](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L40-L41)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L69-L70) - SDKMAN-installed Scala 2.13.12
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L69-L70](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L69-L70)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L56-L57)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L56-L57](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L56-L57)

**Example:**
```bash
scala Hello1.scala
scala Hello2.scala
```

### 2.2 scala -e (Inline Expression)
**Method:** Evaluate a Scala expression supplied as a shell argument.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L42) - apt-installed Scala 2: `scala -e "println(22)"`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L42](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L42)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L71) - SDKMAN
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L71](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L71)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L59)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L59](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L59)

**Example:**
```bash
scala -e "println(22)"
```

### 2.3 echo '...' | scala -Dscala.repl.no-tty=true (Piped REPL)
**Method:** Pipe Scala source on stdin to the REPL with `-Dscala.repl.no-tty=true` so it runs unattended.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L72-L74) - `echo 'println(...)' | scala -Dscala.repl.no-tty=true`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L72-L74](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L72-L74)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L61-L63)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L61-L63](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L61-L63)

**Example:**
```bash
echo 'println("""Hello,
Scala from echo of sdk!
This is a multiline string!""")' | scala -Dscala.repl.no-tty=true
```

---

## 3. **Scala Scripts (`.sc`)**

### 3.1 scala <file.sc> (Coursier-Installed Launcher)
**Method:** Run a `.sc` worksheet/script directly with the Coursier-installed `scala` launcher. Differs from `.scala` files in that `.sc` files have script-style top-level statements (no enclosing `object`/`class`).

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L43-L51) - heredoc-generated `hello_scala.sc` + `vars.sc`, then `scala hello_scala.sc` / `scala vars.sc`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L43-L51](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L43-L51)

**Example:**
```bash
echo 'println("Hello from scala .sc!")' > hello_scala.sc
scala hello_scala.sc
```

---

## 4. **scala-cli (Modern Single-File Runner)**

### 4.1 scala-cli run <file.scala> / <file.sc>
**Method:** [Scala CLI](https://scala-cli.virtuslab.org) compiles and runs `.scala` / `.sc` sources in a single command, with `--scala <version>` to pin a Scala version.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L29-L30) - `scala-cli run Hello1.scala`, `scala-cli run Hello2.scala` (Scala 2.13.12 path)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L29-L30](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L29-L30)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L67-L68) - `scala-cli run --scala 2.13.12 Hello1.scala`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L67-L68](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L67-L68)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L55-L60) - `.sc` files: `scala-cli run --scala 2.13.12 hello_scala.sc`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L55-L60](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L55-L60)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L79-L80)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L79-L80](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L79-L80)

**Example:**
```bash
scala-cli run Hello1.scala
scala-cli run --scala 2.13.12 hello_scala.sc
```

### 4.2 scala-cli repl <<EOF (Heredoc REPL)
**Method:** Drive the Scala CLI REPL non-interactively by feeding a heredoc of Scala statements on stdin.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L31-L38)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L31-L38](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L31-L38)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L70-L76) - `scala-cli repl --scala 2.13.12 <<EOF ... EOF`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L70-L76](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L70-L76)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L62-L70) - `scala-cli repl --scala 2.13.12 <<'EOF' ... EOF`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L62-L70](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L62-L70)

**Example:**
```bash
scala-cli repl --scala 2.13.12 <<EOF
val greeting = "Hello, REPL!"
println(greeting)
val numbers = List(1, 2, 3)
println(s"Sum: \${numbers.sum}")
EOF
```

### 4.3 scala-cli --scala -e (Inline Expression)
**Method:** Evaluate a one-liner with a pinned Scala version. The cleanest cross-version analogue of `scala -e`.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L39) - `scala-cli --scala 2.13.12 -e "println(42 + 1)"`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L39](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L39)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L89)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L89](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L89)

**Example:**
```bash
scala-cli --scala 2.13.12 -e "println(42 + 1)"
```

---

## 5. **Ammonite**

### 5.1 amm <file.sc>
**Method:** [Ammonite](https://ammonite.io) is a Scala REPL/script runner. `amm` runs a `.sc` file directly, with dependency declarations via `import $ivy` and caching.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L72-L75) - `amm hello_scala.sc`, `amm vars.sc`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L72-L75](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L72-L75)

**Example:**
```bash
amm hello_scala.sc
amm vars.sc
```

### 5.2 amm -c '...' (Batch/REPL Mode)
**Method:** Execute an inline Scala snippet via Ammonite's batch mode (`-c`).

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L77-L83) - `amm -c '...'` with multi-line snippet
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L77-L83](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L77-L83)

**Example:**
```bash
amm -c '
println("Hello from Ammonite repl!")
val numbers = List(1, 2, 3)
println(s"Sum: ${numbers.sum}")
'
```

---

## 6. **sbt**

### 6.1 sbt run
**Method:** Build and execute the project's main class via sbt — the canonical Scala build tool. Scala version is set in `build.sbt` (`scalaVersion := "2.13.x"`).

**Locations:**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L48) - `sbt run` (matrix includes `2.13.8`)
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L48](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L48)
- [scala/codeforces_script/build.sbt](../scala/codeforces_script/build.sbt#L4) - `scalaVersion := "2.13.8"`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/build.sbt#L4](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/build.sbt#L4)

**Example:**
```bash
sbt run
```

### 6.2 sbt test
**Method:** Run the project's ScalaTest suite via sbt.

**Locations:**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L45) - `sbt test`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L45](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L45)

**Example:**
```bash
sbt test
```

### 6.3 sbt console <<< (Piped Heredoc)
**Method:** Pipe Scala source into the `sbt console` REPL via shell heredoc/here-string; the REPL has the project classpath loaded.

**Locations:**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L50-L60) - `sbt console <<< 'println(...)'` then a variant filtered with `awk`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L50-L60](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L50-L60)

**Example:**
```bash
sbt console <<< 'println("""Hello,
Scala from sbt console!""" + """2""" + """
This is a multiline string!""")' | tail -n +8
```

---

## 7. **Gradle (scala Plugin)**

### 7.1 ./gradlew build && ./gradlew run
**Method:** Use Gradle's `scala` + `application` plugins to compile and run a Scala main class. `-PscalaVersion=2.13.x` selects the Scala 2 dependency in `build.gradle`.

**Locations:**
- [scala/codeforces_script/.github/workflows/main_gradle.yml](../scala/codeforces_script/.github/workflows/main_gradle.yml#L34-L37) - `./gradlew build -PscalaVersion=2.13.11` then `./gradlew run -PscalaVersion=2.13.11`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main_gradle.yml#L34-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_gradle.yml#L34-L37)
- [scala/codeforces_script/build.gradle](../scala/codeforces_script/build.gradle#L10-L22) - `scalaVersion` toggle picks `scala-library` (Scala 2) when version doesn't start with `3`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/build.gradle#L10-L22](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/build.gradle#L10-L22)

**Example:**
```bash
./gradlew build -PscalaVersion=2.13.11
./gradlew run -PscalaVersion=2.13.11
```

---

## 8. **Maven (scala-maven-plugin)**

### 8.1 mvn clean compile + mvn exec:java
**Method:** Use [scala-maven-plugin](https://davidb.github.io/scala-maven-plugin/) for compilation and `exec-maven-plugin` for running. Profile `scala-2` (default) selects `scala-library` + `scalatest_2.13`.

**Locations:**
- [scala/codeforces_script/.github/workflows/main_maven.yml](../scala/codeforces_script/.github/workflows/main_maven.yml#L31-L34) - `mvn -B clean compile -Dscala.version=2.13.11 -Pscala-2` then `mvn exec:java -Pscala-2 -Dexec.mainClass=Main`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main_maven.yml#L31-L34](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_maven.yml#L31-L34)
- [scala/codeforces_script/pom.xml](../scala/codeforces_script/pom.xml#L98-L104) - `scala-2` profile (default), `scala-library` + `scalatest_2.13`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/pom.xml#L98-L104](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/pom.xml#L98-L104)

**Example:**
```bash
mvn -B clean compile -Dscala.version=2.13.11 -Pscala-2
mvn exec:java -Dscala.version=2.13.11 -Pscala-2 -Dexec.mainClass=Main
```

---

## 9. **GitHub Action Setups (Toolchain Provisioning Wrappers)**

These do not execute Scala code by themselves, but each is the in-CI provisioning mechanism that enables one of the methods above. Recorded here so the catalogue is exhaustive on *how* the toolchain reaches the runner.

### 9.1 VirtusLab/scala-cli-setup@v1
**Method:** GitHub Action that installs [scala-cli](https://scala-cli.virtuslab.org), enabling §4 methods.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L17) - `uses: VirtusLab/scala-cli-setup@v1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L17](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L17)

### 9.2 coursier/setup-action@v1 + cs install
**Method:** Coursier sets up a JVM and the `cs` launcher; `cs install scala:2.13.x scalac:2.13.x scala-cli ammonite:2.5.9` provisions §1, §3, §4, §5.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L17-L24) - `coursier/setup-action@v1` + `cs install scala:2.13.12 scalac:2.13.12 scala-cli ammonite:2.5.9`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L17-L24](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L17-L24)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L24-L34)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L24-L34](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L24-L34)

### 9.3 olafurpg/setup-scala@v12
**Method:** Action that provisions a Scala 2 JDK + sbt for §6.

**Locations:**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L31-L34) - `uses: olafurpg/setup-scala@v12` with `scala-version: ${{ matrix.scala-version }}` (matrix `[2.13.8, 3.3.1]`)
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L31-L34](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L31-L34)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L26-L29) - `scala-version: '2.13.12'`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L26-L29](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L26-L29)

### 9.4 sdk install scala (SDKMAN)
**Method:** Install a specific Scala 2 release via SDKMAN.

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L60-L62) - `sdk install scala 2.13.12` + `sdk use scala 2.13.12`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L60-L62](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L60-L62)

### 9.5 sudo apt install -y scala
**Method:** Install Scala 2 from the Ubuntu APT repository. **Only available for Scala 2** — Scala 3 has no APT package in the workflow (explicit comment in `main_scala.yml#L34`).

**Locations:**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L35) - `sudo apt install -y scala` (used only in the Scala 2 block)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L35](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L35)

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| scalac + scala | Compile + run via toolchain launcher | [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L36)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L36) |
| scalac + java | Run on plain JVM with Coursier-fetched classpath | [main_scala2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala2_coursier.yml#L47)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala2_coursier.yml#L47) |
| scala &lt;file.scala&gt; | Interpreted source | [main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L40)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L40) |
| scala -e | Inline expression | [main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L42)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L42) |
| echo \| scala -Dscala.repl.no-tty=true | Piped REPL | [main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L72)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L72) |
| scala &lt;file.sc&gt; | Run `.sc` worksheet | [main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L50)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L50) |
| scala-cli run | Modern single-file runner | [scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L29)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L29) |
| scala-cli repl &lt;&lt;EOF | Heredoc-driven REPL | [scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L31)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L31) |
| scala-cli --scala -e | Inline expression with pinned version | [scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L39)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L39) |
| amm | Ammonite script runner | [main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L72)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L72) |
| amm -c | Ammonite batch one-liner | [main_sc2_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc2_coursier.yml#L77)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc2_coursier.yml#L77) |
| sbt run | Canonical Scala build | [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L48)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L48) |
| sbt test | ScalaTest suite | [main.yml](../scala/codeforces_script/.github/workflows/main.yml#L45)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L45) |
| sbt console <<< | Piped REPL | [main.yml](../scala/codeforces_script/.github/workflows/main.yml#L52)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L52) |
| ./gradlew run | Gradle scala+application plugins | [main_gradle.yml](../scala/codeforces_script/.github/workflows/main_gradle.yml#L37)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_gradle.yml#L37) |
| mvn compile + mvn exec:java | scala-maven-plugin + exec | [main_maven.yml](../scala/codeforces_script/.github/workflows/main_maven.yml#L34)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_maven.yml#L34) |

---

## Key Frameworks & Tools Integrated

- **sbt** - Canonical Scala build tool
- **Gradle (`scala` plugin)** - General-purpose build with Scala support
- **Maven (`scala-maven-plugin` + `exec-maven-plugin`)** - JVM-shop build with Scala support
- **scala-cli (VirtusLab)** - Modern all-in-one single-file runner, REPL, compiler
- **Coursier (`cs`)** - JVM/Scala artifact fetcher and toolchain installer
- **Ammonite** - Enhanced Scala REPL and `.sc` script runner
- **SDKMAN** - SDK version manager (used to install Scala 2.13.x)
- **APT (`sudo apt install scala`)** - System-package install of Scala 2 (Scala 3 has no APT package)
- **ScalaTest** - Test framework (`scalatest_2.13`)
- **GitHub Actions** - `olafurpg/setup-scala@v12`, `coursier/setup-action@v1`, `VirtusLab/scala-cli-setup@v1`

---
