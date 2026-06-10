# Clojure Execution Methods in Programming Languages Repository

This document catalogues **all distinct Clojure (JVM) execution methods** discovered for compiling, running, and executing `.clj` code throughout the repository. ClojureScript methods are catalogued separately in [clojurescript.md](clojurescript.md) because the implementations live in separate folders (`clojure_/codeforces_script/` vs `clojure_/clojure_script_/codeforces_script/`), target different runtimes (JVM vs Node.js / browser), and use disjoint toolchains (Clojure CLI / Leiningen vs nbb / shadow-cljs / lumo).

Each method takes Clojure source as input and produces the program's output. Intermediate steps (toolchain installation alone, dependency-only fetches, run-only steps on a pre-built artifact) are not listed as separate methods; if a single command performs compile + run together, that counts as one method.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [clojure <file.clj> (Clojure CLI / tools.deps)](#11-clojure-fileclj-clojure-cli--toolsdeps)

2. **Inline / REPL Execution**
   - 2.1 [clojure -e "..." (Inline Expression)](#21-clojure--e--inline-expression)
   - 2.2 [clojure - <<EOF (Stdin Heredoc)](#22-clojure---eof-stdin-heredoc)

3. **Clojure CLI Aliases (deps.edn)**
   - 3.1 [clojure -M:alias (Named Main Alias)](#31-clojure--malias-named-main-alias)
   - 3.2 [clojure -Spath (Classpath Export)](#32-clojure--spath-classpath-export)

4. **Leiningen (`lein`)**
   - 4.1 [lein run / lein compile (compile + run)](#41-lein-run--lein-compile-compile--run)
   - 4.2 [lein <alias> (project.clj aliases)](#42-lein-alias-projectclj-aliases)

5. **Gradle (clojurephant plugin)**
   - 5.1 [./gradlew build run](#51-gradlew-build-run)

6. **Maven (clojure-maven-plugin)**
   - 6.1 [mvn clean compile exec:java](#61-mvn-clean-compile-execjava)

7. **Embedded / Polyglot Execution**
   - 7.1 [Python subprocess → clojure -e](#71-python-subprocess--clojure--e)
   - 7.2 [Clojure → shell → Python → Clojure (nested polyglot)](#72-clojure--shell--python--clojure-nested-polyglot)

8. **GitHub Action Setups (Toolchain Provisioning)**
   - 8.1 [DeLaGuardo/setup-clojure@master](#81-delaguardosetup-clojuremaster)

---

## 1. **Direct File Execution**

### 1.1 clojure <file.clj> (Clojure CLI / tools.deps)
**Method:** Pass a `.clj` source file directly to the `clojure` launcher (from the [Clojure CLI tools](https://clojure.org/guides/install_clojure)). It resolves dependencies via `deps.edn`, builds a classpath, and runs the script in one command. Stdin can be piped to the script via shell redirection.

**Locations:**
- [Python/codeforces_script/direct/run.sh](../Python/codeforces_script/direct/run.sh#L48-L49) - `clojure "$script" < "$f" | tee "clj_output/${testname}.txt"` (pipes test input on stdin)
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/direct/run.sh#L48-L49](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L48-L49)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L43) - `clojure my-clojure-app/src/my_clojure_app/cc.clj`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L43](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L43)
- [Python/codeforces_script/.github/workflows/main.yml](../Python/codeforces_script/.github/workflows/main.yml#L101-L106) - matrix-driven `./run.sh ${{ matrix.problem }}.clj`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L101-L106](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L101-L106)

**Example:**
```bash
clojure my-clojure-app/src/my_clojure_app/cc.clj
clojure 1A.clj < test1.in
```

---

## 2. **Inline / REPL Execution**

### 2.1 clojure -e "..." (Inline Expression)
**Method:** Evaluate a Clojure form supplied as a single shell argument.

**Locations:**
- [clojure_/codeforces_script/testing.py](../clojure_/codeforces_script/testing.py#L17) - `subprocess.run(['clojure', '-e', inner_code])` (Clojure invoked from Python)
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/testing.py#L17](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.py#L17)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L36) - `clojure -e "(println \"Hello from -e!\")"`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L36](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L36)

**Example:**
```bash
clojure -e "(println \"Hello from -e!\")"
```

### 2.2 clojure - <<EOF (Stdin Heredoc)
**Method:** Pass `-` as the source argument so `clojure` reads program text from stdin; supply a heredoc to inline a multi-line script.

**Locations:**
- [clojure_/codeforces_script/testing.sh](../clojure_/codeforces_script/testing.sh#L44-L47) - `clojure - <<'EOF' ... EOF`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/testing.sh#L44-L47](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.sh#L44-L47)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L34-L39) - "Run Clojure -e example and Python" step runs `chmod +x testing.sh && ./testing.sh`, which executes [testing.sh#L44-L47](../clojure_/codeforces_script/testing.sh#L44-L47) (`clojure - <<'EOF' ... EOF`) — transitive coverage via shell script.
  - Remote: [clojure_/codeforces_script/.github/workflows/main.yml#L34-L39](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L34-L39)

**Example:**
```bash
clojure - <<'EOF'
(println "Multiline raw Clojure!")
(println "No need to escape here.")
EOF
```

---

## 3. **Clojure CLI Aliases (deps.edn)**

### 3.1 clojure -M:alias (Named Main Alias)
**Method:** `deps.edn` defines named `:aliases` that bundle `:main-opts`. `clojure -M:alias` resolves dependencies for the alias and runs the configured main namespace. The repo's `deps.edn` defines `:run-my` (runs `my-clojure-app.core`) and `:run-gradle` (runs `clojuregradle.core`).

**Locations:**
- [clojure_/codeforces_script/my-clojure-app/deps.edn](../clojure_/codeforces_script/my-clojure-app/deps.edn#L11-L18) - `:run-my` / `:run-gradle` alias definitions
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/deps.edn#L11-L18](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/deps.edn#L11-L18)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L60-L61) - `clojure -M:run-my` and `clojure -M:run-gradle`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L60-L61](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L60-L61)

**Example:**
```bash
clojure -M:run-my        # runs my-clojure-app.core
clojure -M:run-gradle    # runs clojuregradle.core
```

### 3.2 clojure -Spath (Classpath Export)
**Method:** `clojure -Spath` prints the resolved classpath; used in this repo to feed `kotlinc -classpath "$(clojure -Spath)"` so Kotlin sources compile against the same dependency set Clojure sees.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L57) - `kotlinc src/main/kotlin -d target/kotlin -classpath "$(clojure -Spath)"`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L57](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L57)

**Example:**
```bash
kotlinc src/main/kotlin -d target/kotlin -classpath "$(clojure -Spath)"
```

---

## 4. **Leiningen (`lein`)**

### 4.1 lein run / lein compile (compile + run)
**Method:** [Leiningen](https://leiningen.org/) is the classic Clojure build tool, configured via `project.clj`. `lein run` resolves dependencies, compiles as needed, and invokes the `:main` namespace in one step. `lein compile` only AOT-compiles namespaces to `.class` files under `target/` — on its own it produces no program output, so it is paired with `lein run` here as one complete compile + run flow.

**Locations:**
- [clojure_/codeforces_script/my-clojure-app/project.clj](../clojure_/codeforces_script/my-clojure-app/project.clj#L11) - `:main ^:skip-aot my-clojure-app.core`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/project.clj#L11](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/project.clj#L11)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L73) - `lein run` (inside `cd my-clojure-app`)
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L73](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L73)
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L77) - `lein compile`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L77](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L77)

**Example:**
```bash
cd my-clojure-app
lein compile   # optional AOT step
lein run       # runs :main, produces program output
```

### 4.2 lein <alias> (project.clj aliases)
**Method:** `project.clj` `:aliases` map shortcuts to `run -m <ns>` invocations. The repo defines `run-my` and `run-gradle`.

**Locations:**
- [clojure_/codeforces_script/my-clojure-app/project.clj](../clojure_/codeforces_script/my-clojure-app/project.clj#L12-L13) - `:aliases {"run-gradle" [...] "run-my" [...]}`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/project.clj#L12-L13](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/project.clj#L12-L13)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L73) - `lein run && lein run-my && lein run-gradle`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L73](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L73)

**Example:**
```bash
lein run-my
lein run-gradle
```

---

## 5. **Gradle (clojurephant plugin)**

### 5.1 ./gradlew build run
**Method:** Use Gradle's `application` plugin together with [`dev.clojurephant.clojure`](https://github.com/clojurephant/clojurephant) to compile Clojure (with AOT), Kotlin, Groovy, and Java in one polyglot build, then run the configured `mainClass`.

**Locations:**
- [clojure_/codeforces_script/my-clojure-app/build.gradle](../clojure_/codeforces_script/my-clojure-app/build.gradle#L6) - `id 'dev.clojurephant.clojure' version '0.8.0'`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/build.gradle#L6](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/build.gradle#L6)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L87-L89) - `gradle wrapper` then `./gradlew clean compileKotlin` then `./gradlew build run`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L87-L89](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L87-L89)

**Example:**
```bash
cd my-clojure-app
gradle wrapper
./gradlew clean compileKotlin
./gradlew build run
```

---

## 6. **Maven (clojure-maven-plugin)**

### 6.1 mvn clean compile exec:java
**Method:** [`clojure-maven-plugin`](https://github.com/talios/clojure-maven-plugin) (group `com.theoryinpractise`) compiles declared Clojure namespaces; `exec-maven-plugin` then runs the configured main class.

**Locations:**
- [clojure_/codeforces_script/my-clojure-app/pom.xml](../clojure_/codeforces_script/my-clojure-app/pom.xml#L96-L116) - `clojure-maven-plugin` configuration
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/pom.xml#L96-L116](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/pom.xml#L96-L116)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L94-L95) - `cd my-clojure-app` then `mvn clean compile exec:java -Dexec.mainClass=clojuregradle.core`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L94-L95](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L94-L95)

**Example:**
```bash
cd my-clojure-app
mvn clean compile exec:java -Dexec.mainClass=clojuregradle.core
```

---

## 7. **Embedded / Polyglot Execution**

### 7.1 Python subprocess → clojure -e
**Method:** Python (`subprocess.run`) invokes `clojure -e` with a dynamically constructed source string, embedding Clojure programs inside Python scripts.

**Locations:**
- [clojure_/codeforces_script/testing.py](../clojure_/codeforces_script/testing.py#L17-L18) - `subprocess.run(['clojure', '-e', inner_code])`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/testing.py#L17-L18](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.py#L17-L18)
- [clojure_/codeforces_script/testing.sh](../clojure_/codeforces_script/testing.sh#L5-L41) - shell → python heredoc → `subprocess.run(['clojure', '-e', inner_code])`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/testing.sh#L5-L41](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.sh#L5-L41)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L34-L39) - step runs `python testing.py` (which does `subprocess.run(['clojure','-e',inner_code])` at [testing.py#L17](../clojure_/codeforces_script/testing.py#L17)) and `./testing.sh` (same pattern at [testing.sh#L5-L41](../clojure_/codeforces_script/testing.sh#L5-L41)) — transitive coverage.
  - Remote: [clojure_/codeforces_script/.github/workflows/main.yml#L34-L39](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L34-L39)

**Example:**
```python
import subprocess
inner_code = '(println "Hello from embedded Clojure!")'
subprocess.run(['clojure', '-e', inner_code])
```

### 7.2 Clojure → shell → Python → Clojure (nested polyglot)
**Method:** A Clojure program uses `clojure.java.shell/sh` to spawn `python3 -c <generated-python>`, which in turn spawns another `clojure -e ...` — chaining `clojure → shell → python → clojure`. Used in this repo as a stress-test of multi-language string-escaping.

**Locations:**
- [clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj](../clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L1-L40) - `(shell/sh "python3" "-c" python-code)` containing nested `subprocess.run(['clojure', '-e', ...])`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L1-L40](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/src/my_clojure_app/cc.clj#L1-L40)

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L34-L39) - runs `./testing.sh`, whose embedded python heredoc spawns `clojure -e "..."` whose `clojure.java.shell/sh` invocation re-enters `python3 -c "..."` — the full nested polyglot chain runs end-to-end in CI.
  - Remote: [clojure_/codeforces_script/.github/workflows/main.yml#L34-L39](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L34-L39)

**Example:**
```clojure
(require '[clojure.java.shell :as shell])
(def python-code "import subprocess; subprocess.run(['clojure','-e',\"(println (+ 1 2))\"])")
(shell/sh "python3" "-c" python-code)
```

---

## 8. **GitHub Action Setups (Toolchain Provisioning)**

### 8.1 DeLaGuardo/setup-clojure@master
**Method:** GitHub Action that installs the Clojure CLI (`clojure`, `clj`) plus optionally Leiningen / boot. Enables §1, §2, §3.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L25) - `uses: DeLaGuardo/setup-clojure@master` with `cli: 1.11.1.1403`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/.github/workflows/main.yml#L25](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L25)
- [Python/codeforces_script/.github/workflows/main.yml](../Python/codeforces_script/.github/workflows/main.yml#L37-L40) - same action used by python_ branch CI
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L37-L40](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L37-L40)
> Leiningen is installed manually via curl (no action used) — see [clojure_/codeforces_script/.github/workflows/main.yml#L66-L69](../clojure_/codeforces_script/.github/workflows/main.yml#L66-L69).

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| clojure &lt;file.clj&gt; | Run a .clj script directly | [clojure_/codeforces_script/.github/workflows/main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L43)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L43) |
| clojure -e | Inline expression | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L36)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L36) |
| clojure - <<EOF | Stdin heredoc | [testing.sh](../clojure_/codeforces_script/testing.sh#L44)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.sh#L44) |
| clojure -M:alias | deps.edn named main | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L60)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L60) |
| clojure -Spath | Classpath export | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L57)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L57) |
| lein run / lein compile | Leiningen compile + run | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L73)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L73) |
| lein <alias> | project.clj alias | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L73)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L73) |
| ./gradlew build run | Polyglot Gradle (clojurephant) | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L89)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L89) |
| mvn clean compile exec:java | clojure-maven-plugin + exec | [main.yml](../clojure_/codeforces_script/.github/workflows/main.yml#L95)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/.github/workflows/main.yml#L95) |
| Python subprocess → clojure -e | Embedded polyglot | [testing.py](../clojure_/codeforces_script/testing.py#L17)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.py#L17) |
| Clojure → shell → python → clojure | Nested polyglot | [cc.clj](../clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/src/my_clojure_app/cc.clj) |

---

## Key Frameworks & Tools Integrated

- **Clojure CLI (tools.deps)** - Reference launcher; `deps.edn`-driven dependency resolution
- **Leiningen** - Classic Clojure build tool; `project.clj` + aliases
- **Gradle (`dev.clojurephant.clojure` plugin)** - Polyglot Gradle build (Clojure + Kotlin + Groovy + Java)
- **Maven (`clojure-maven-plugin` + `exec-maven-plugin`)** - JVM-shop Clojure compile + run
- **Kotlin / Groovy / Java interop** - Compiled to bytecode and added to Clojure's classpath
- **Python `subprocess`** - Embedded Clojure execution from Python (and vice versa via `clojure.java.shell`)
- **GitHub Actions** - `DeLaGuardo/setup-clojure@master`, `actions/setup-java@v2`, SDKMAN for Kotlin
- **APT** - `groovy` installed via `sudo apt-get install -y groovy`

---
