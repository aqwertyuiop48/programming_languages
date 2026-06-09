# Scala 3 Execution Methods in Programming Languages Repository

This document catalogues **all distinct Scala 3 execution methods** discovered for building, running, and executing Scala 3.x code throughout the repository. Scala 2 methods are catalogued separately in [scala2.md](scala2.md) because the implementations live in separate folders (`interviews/scala3_/` vs `interviews/scala2_/`) and rely on different toolchain provisioning (Scala 3 has no APT package; Scala 3 uses the `scala3-library_3` Maven artifact; Scala 3 supports the `@main` top-level main syntax not present in Scala 2).

Each method takes Scala 3 source code (`.scala` or `.sc`) as input and produces the program's output. Intermediate steps (toolchain installation alone, dependency-only fetches, run-only steps on a pre-built artifact) are not listed as separate methods; if a single command performs compile + run together, that counts as one method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [scalac + scala (System-Installed Toolchain)](#11-scalac--scala-system-installed-toolchain)
   - 1.2 [scalac + java (Coursier-Fetched Classpath)](#12-scalac--java-coursier-fetched-classpath)

2. **Interpreted / Inline Execution**
   - 2.1 [scala <file.scala> (Interpreted Source)](#21-scala-filescala-interpreted-source)
   - 2.2 [scala -e (Inline Expression)](#22-scala--e-inline-expression)
   - 2.3 [echo '...' | scala -Dscala.repl.no-tty=true (Piped REPL)](#23-echo---scala--dscalareplno-ttytrue-piped-repl)

3. **Scala 3 `.sc` Scripts (Top-Level Expressions)**
   - 3.1 [scala-cli run --scala 3.3.1 <file.sc>](#31-scala-cli-run---scala-331-filesc)
   - 3.2 [cat <<EOF | scala-cli run --scala 3.3.1 - (stdin streaming)](#32-cat-eof--scala-cli-run---scala-331---stdin-streaming)

4. **scala-cli (Modern Single-File Runner)**
   - 4.1 [scala-cli run <file.scala>](#41-scala-cli-run-filescala)
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
   - 9.4 [sdk install scala 3.3.1 (SDKMAN)](#94-sdk-install-scala-331-sdkman)

## Language Feature Note: Scala 3 `@main`

Scala 3 supports top-level `@main` annotated functions as a lightweight entry-point syntax, distinct from Scala 2's `object Hello { def main(args: Array[String]) = ... }` style. The repository's Scala 3 sources include both forms.

**Locations:**
- [kotlin/java_embed/codeforces_script/interviews/scala3_/Hello.scala](../kotlin/java_embed/codeforces_script/interviews/scala3_/Hello.scala) - `@main def first(): Unit = println("Hello from Scala 3!")`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/interviews/scala3_/Hello.scala](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/interviews/scala3_/Hello.scala)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

---

## 1. **Direct Compilation & Execution**

### 1.1 scalac + scala (System-Installed Toolchain)
**Method:** Compile a `.scala` source to JVM `.class` files with `scalac`, then run the resulting object/class with `scala`. The Scala 3 toolchain comes from `sdk install scala 3.3.1` or `cs install scala:3.3.1` (Scala 3 has no APT package — see §9).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L127-L128) - SDKMAN-installed Scala 3.3.1: `scalac Hello1.scala && scala Hello1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L127-L128](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L127-L128)

**Example:**
```bash
sdk install scala 3.3.1
sdk use scala 3.3.1
scalac Hello1.scala && scala Hello1
scalac Hello2.scala && scala Hello2
```

### 1.2 scalac + java (Coursier-Fetched Classpath)
**Method:** Compile with `scalac` and run with plain `java -cp`, providing both the Scala 3 standard library (`scala3-library_3`) and the underlying Scala 2 stdlib (`scala-library`) JARs via Coursier's `cs fetch`. The Scala 3 compiler emits bytecode that depends on both libraries.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L38-L41) - `JARS=$(cs fetch org.scala-lang:scala3-library_3:3.3.1 org.scala-lang:scala-library:2.13.12 | tr '\n' ':')` then `scalac Hello1.scala && java -cp ".:${JARS}" Hello1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L38-L41](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L38-L41)

**Example:**
```bash
JARS=$(cs fetch org.scala-lang:scala3-library_3:3.3.1 \
                org.scala-lang:scala-library:2.13.12 | tr '\n' ':')
scalac Hello1.scala && java -cp ".:${JARS}" Hello1
```

---

## 2. **Interpreted / Inline Execution**

### 2.1 scala <file.scala> (Interpreted Source)
**Method:** Pass a `.scala` source directly to the `scala` launcher; it compiles and runs in one step. With Scala 3 this also accepts `@main`-annotated sources.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L131-L133) - SDKMAN Scala 3.3.1: `scala Hello.scala`, `scala Hello1.scala`, `scala Hello2.scala`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L131-L133](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L131-L133)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L48-L50) - Coursier-installed Scala 3
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L48-L50](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L48-L50)

**Example:**
```bash
scala Hello.scala   # uses Scala 3 @main
scala Hello1.scala
```

### 2.2 scala -e (Inline Expression)
**Method:** Evaluate a Scala 3 expression supplied as a shell argument.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L135) - SDKMAN: `scala -e "println(22)"`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L135](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L135)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L51)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L51](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L51)

**Example:**
```bash
scala -e "println(22)"
```

### 2.3 echo '...' | scala -Dscala.repl.no-tty=true (Piped REPL)
**Method:** Pipe Scala source on stdin to the REPL with `-Dscala.repl.no-tty=true` so it runs unattended.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L52-L54)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L52-L54](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L52-L54)

**Example:**
```bash
echo 'println("""Hello,
Scala 3 from echo!
This is a multiline string!""")' | scala -Dscala.repl.no-tty=true
```

---

## 3. **Scala 3 `.sc` Scripts (Top-Level Expressions)**

Scala 3 `.sc` scripts use top-level expressions natively — no enclosing `object` needed, and no `@main` either.

### 3.1 scala-cli run --scala 3.3.1 <file.sc>
**Method:** Run a Scala 3 `.sc` script via scala-cli with the version pinned to 3.3.1.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L56-L60) - `scala-cli run --scala ${ver} hello_scala.sc` / `vars.sc`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L56-L60](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L56-L60)

**Example:**
```bash
echo 'println("Hello from Scala 3 .sc!")' > hello_scala.sc
scala-cli run --scala 3.3.1 hello_scala.sc
```

### 3.2 cat <<EOF | scala-cli run --scala 3.3.1 - (stdin streaming)
**Method:** Pipe a complete Scala 3 source on stdin (`-` as the source name) to scala-cli for compile+run, without creating any source file on disk.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L63-L69) - `cat <<'EOF' | scala-cli run --scala 3.3.1 -`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L63-L69](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L63-L69)

**Example:**
```bash
cat <<'EOF' | scala-cli run --scala 3.3.1 -
val msg = "Hello from stdin Scala 3!"
println(msg)
EOF
```

---

## 4. **scala-cli (Modern Single-File Runner)**

### 4.1 scala-cli run <file.scala>
**Method:** [Scala CLI](https://scala-cli.virtuslab.org) compiles and runs `.scala` sources in a single command, with `--scala 3.3.1` to pin Scala 3.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L47-L49) - `scala-cli run Hello.scala`, `Hello1.scala`, `Hello2.scala` (Scala 3.3.1 path)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L47-L49](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L47-L49)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L56-L58)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L56-L58](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L56-L58)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L149-L151)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L149-L151](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L149-L151)

**Example:**
```bash
scala-cli run Hello.scala            # uses @main
scala-cli run --scala 3.3.1 Hello1.scala
```

### 4.2 scala-cli repl <<EOF (Heredoc REPL)
**Method:** Drive the Scala CLI REPL non-interactively by feeding a heredoc of Scala 3 statements on stdin.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L51-L58)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L51-L58](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L51-L58)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L59-L65) - `scala-cli repl --scala 3.3.1 <<EOF ... EOF`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L59-L65](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L59-L65)

**Example:**
```bash
scala-cli repl --scala 3.3.1 <<EOF
val greeting = "Hello, Scala 3 REPL!"
println(greeting)
val numbers = List(1, 2, 3)
println(s"Sum: \${numbers.sum}")
EOF
```

### 4.3 scala-cli --scala -e (Inline Expression)
**Method:** Evaluate a one-liner with Scala 3 pinned via `--scala`. Cross-version analogue of `scala -e`.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L59) - `scala-cli --scala 3.3.1 -e "println(42 + 1)"`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L59](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L59)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L161)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L161](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L161)

**Example:**
```bash
scala-cli --scala 3.3.1 -e "println(42 + 1)"
```

---

## 5. **Ammonite**

### 5.1 amm <file.sc>
**Method:** [Ammonite](https://ammonite.io) runs a `.sc` file directly. The Scala 3 Coursier setup installs `ammonite` (no pinned version) for compatibility with Scala 3.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L72-L76) - `amm hello_scala.sc`, `amm vars.sc`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L72-L76](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L72-L76)

**Example:**
```bash
amm hello_scala.sc
amm vars.sc
```

### 5.2 amm -c '...' (Batch/REPL Mode)
**Method:** Execute an inline Scala snippet via Ammonite's batch mode.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L78-L85) - `amm -c '...'` multi-line snippet
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L78-L85](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L78-L85)

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
**Method:** Build and execute the project's main class via sbt. Scala version is set in `build.sbt` and overridden in CI matrix to `3.3.1`.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L48) - `sbt run` (matrix includes `3.3.1`)
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L48](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L48)
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L16) - matrix `scala-version: [2.13.8, 3.3.1]`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L16](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L16)

**Example:**
```bash
sbt run
```

### 6.2 sbt test
**Method:** Run the project's ScalaTest suite via sbt; the test framework dependency is selected by Maven profile / sbt config.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L45) - `sbt test`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L45](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L45)

**Example:**
```bash
sbt test
```

### 6.3 sbt console <<< (Piped Heredoc)
**Method:** Pipe Scala source into the `sbt console` REPL via shell here-string; the REPL has the project classpath loaded. With Scala 3 the matrix entry uses Scala 3.3.1.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L50-L60) - `sbt console <<< 'println(...)'` then awk-filtered variant
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L50-L60](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L50-L60)

**Example:**
```bash
sbt console <<< 'println("""Hello,
Scala 3 from sbt console!""" + """3""" + """
This is a multiline string!""")' | tail -n +8
```

---

## 7. **Gradle (scala Plugin)**

### 7.1 ./gradlew build && ./gradlew run
**Method:** Use Gradle's `scala` + `application` plugins. `-PscalaVersion=3.3.1` triggers the `scala3-library_3` dependency branch in `build.gradle`.

**Locations:**
- [scala/codeforces_script/build.gradle](../scala/codeforces_script/build.gradle#L10-L22) - dynamic dependency: `scala3-library_3` when `scalaVersion` starts with `3`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/build.gradle#L10-L22](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/build.gradle#L10-L22)

**Workflow yml (executes in CI):**
- [scala/codeforces_script/.github/workflows/main_gradle.yml](../scala/codeforces_script/.github/workflows/main_gradle.yml#L34-L37) - `./gradlew build -PscalaVersion=3.3.1` then `./gradlew run -PscalaVersion=3.3.1`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main_gradle.yml#L34-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_gradle.yml#L34-L37)

**Example:**
```bash
./gradlew build -PscalaVersion=3.3.1
./gradlew run   -PscalaVersion=3.3.1
```

---

## 8. **Maven (scala-maven-plugin)**

### 8.1 mvn clean compile + mvn exec:java
**Method:** Use [scala-maven-plugin](https://davidb.github.io/scala-maven-plugin/) for compilation and `exec-maven-plugin` for execution. Profile `scala-3` selects `scala3-library_3` + `scalatest_3` dependencies.

**Locations:**
- [scala/codeforces_script/pom.xml](../scala/codeforces_script/pom.xml#L107-L113) - `scala-3` profile: `scala3-library_3` + `scalatest_3`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/pom.xml#L107-L113](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/pom.xml#L107-L113)

**Workflow yml (executes in CI):**
- [scala/codeforces_script/.github/workflows/main_maven.yml](../scala/codeforces_script/.github/workflows/main_maven.yml#L31-L34) - `mvn -B clean compile -Dscala.version=3.3.1 -Pscala-3` then `mvn exec:java -Pscala-3 -Dexec.mainClass=Main`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main_maven.yml#L31-L34](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_maven.yml#L31-L34)

**Example:**
```bash
mvn -B clean compile -Dscala.version=3.3.1 -Pscala-3
mvn exec:java -Dscala.version=3.3.1 -Pscala-3 -Dexec.mainClass=Main
```

---

## 9. **GitHub Action Setups (Toolchain Provisioning Wrappers)**

These do not execute Scala 3 code by themselves, but each is the in-CI provisioning mechanism that enables one of the methods above. Recorded here so the catalogue is exhaustive on *how* the toolchain reaches the runner.

> **Note:** Unlike Scala 2, Scala 3 has **no APT package** in the workflows — APT-based provisioning (which the Scala 2 catalogue lists) is not applicable here.

### 9.1 VirtusLab/scala-cli-setup@v1
**Method:** GitHub Action that installs [scala-cli](https://scala-cli.virtuslab.org), enabling §4 methods. scala-cli auto-fetches Scala 3 toolchain when `--scala 3.x` is requested.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L17) - `uses: VirtusLab/scala-cli-setup@v1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L17](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L17)

### 9.2 coursier/setup-action@v1 + cs install
**Method:** Coursier sets up a JVM and the `cs` launcher; `cs install scala scalac ammonite scala-cli` provisions §1, §3, §4, §5 (versions resolve to Scala 3 latest).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L17-L24) - `coursier/setup-action@v1` + `cs install scala scalac ammonite scala-cli`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L17-L24](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L17-L24)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L24-L34)
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L24-L34](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L24-L34)

### 9.3 olafurpg/setup-scala@v12
**Method:** Action that provisions a Scala JDK + sbt; `scala-version: '3.3.1'` (or matrix value) selects the Scala 3 toolchain for §6.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [scala/codeforces_script/.github/workflows/main.yml](../scala/codeforces_script/.github/workflows/main.yml#L31-L34) - `uses: olafurpg/setup-scala@v12` with matrix `[2.13.8, 3.3.1]`
  - Remote (submodule `scala/codeforces_script` @ branch `scala_`): [scala/codeforces_script/.github/workflows/main.yml#L31-L34](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L31-L34)
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L105-L108) - `scala-version: '3.3.1'`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L105-L108](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L105-L108)

### 9.4 sdk install scala 3.3.1 (SDKMAN)
**Method:** Install Scala 3.3.1 via SDKMAN.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L121-L123) - `sdk install scala 3.3.1` + `sdk use scala 3.3.1`
  - Remote (submodule `kotlin/java_embed/codeforces_script` @ branch `kotlin_`): [kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L121-L123](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L121-L123)

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| scalac + scala | Compile + run via toolchain launcher | [main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L127)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L127) |
| scalac + java | Run on plain JVM with Coursier-fetched scala3-library_3 | [main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L41)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L41) |
| scala &lt;file.scala&gt; | Interpreted source (supports `@main`) | [main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L131)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L131) |
| scala -e | Inline expression | [main_scala.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala.yml#L135)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala.yml#L135) |
| echo \| scala -Dscala.repl.no-tty=true | Piped REPL | [main_scala3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_scala3_coursier.yml#L52)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_scala3_coursier.yml#L52) |
| scala-cli run --scala 3.3.1 &lt;file.sc&gt; | Scala 3 `.sc` script | [main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L56)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L56) |
| cat \| scala-cli run --scala 3.3.1 - | stdin-streamed Scala 3 source | [main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L63)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L63) |
| scala-cli run | Modern single-file runner | [scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L47)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L47) |
| scala-cli repl &lt;&lt;EOF | Heredoc-driven REPL | [scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L51)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L51) |
| scala-cli --scala 3.3.1 -e | Inline expression with pinned version | [scala_.yml](../kotlin/java_embed/codeforces_script/.github/workflows/scala_.yml#L59)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/scala_.yml#L59) |
| amm | Ammonite script runner | [main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L72)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L72) |
| amm -c | Ammonite batch one-liner | [main_sc3_coursier.yml](../kotlin/java_embed/codeforces_script/.github/workflows/main_sc3_coursier.yml#L78)<br/>[remote @ `kotlin_`](https://github.com/aqwertyuiop48/codeforces_script/blob/kotlin_/.github/workflows/main_sc3_coursier.yml#L78) |
| sbt run | Canonical Scala build | [main.yml](../scala/codeforces_script/.github/workflows/main.yml#L48)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L48) |
| sbt test | ScalaTest suite | [main.yml](../scala/codeforces_script/.github/workflows/main.yml#L45)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L45) |
| sbt console <<< | Piped REPL | [main.yml](../scala/codeforces_script/.github/workflows/main.yml#L52)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main.yml#L52) |
| ./gradlew run | Gradle scala+application plugins | [main_gradle.yml](../scala/codeforces_script/.github/workflows/main_gradle.yml#L37)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_gradle.yml#L37) |
| mvn compile + mvn exec:java | scala-maven-plugin + exec, profile scala-3 | [main_maven.yml](../scala/codeforces_script/.github/workflows/main_maven.yml#L34)<br/>[remote @ `scala_`](https://github.com/aqwertyuiop48/codeforces_script/blob/scala_/.github/workflows/main_maven.yml#L34) |

---

## Key Frameworks & Tools Integrated

- **sbt** - Canonical Scala build tool (Scala 3 supported as of sbt 1.5+)
- **Gradle (`scala` plugin)** - General-purpose build with Scala 3 support via `scala3-library_3`
- **Maven (`scala-maven-plugin` + `exec-maven-plugin`)** - JVM-shop build with `scala-3` profile selecting `scala3-library_3` + `scalatest_3`
- **scala-cli (VirtusLab)** - Modern all-in-one runner, REPL, compiler; preferred Scala 3 entry point
- **Coursier (`cs`)** - JVM/Scala artifact fetcher; required to fetch `scala3-library_3`
- **Ammonite** - Enhanced REPL and `.sc` script runner (Scala 3 compatible build)
- **SDKMAN** - SDK version manager (used to install Scala 3.3.1)
- **ScalaTest** - Test framework (`scalatest_3` artifact)
- **GitHub Actions** - `olafurpg/setup-scala@v12`, `coursier/setup-action@v1`, `VirtusLab/scala-cli-setup@v1`

---
