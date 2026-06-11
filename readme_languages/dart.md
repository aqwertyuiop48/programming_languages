# Dart Execution Methods in Programming Languages Repository

This document catalogues **all distinct Dart methods** discovered for running Dart code throughout the repository. Dart 3.x ships a single `dart` CLI that handles dependency management, file execution, global tool install, and `dart pub` workflows. The CI also exercises an in-process Dart evaluator (`dart_eval`) and a Docker + tmpfs runtime variant.

## Table of Contents

1. **Project / File Execution**
   - 1.1 [dart pub get (Resolve Dependencies)](#11-dart-pub-get-resolve-dependencies)
   - 1.2 [dart run \<file.dart\> (Direct File Execution)](#12-dart-run-filedart-direct-file-execution)
   - 1.3 [dart pub global activate \<package\> (Install Global Dart Tool)](#13-dart-pub-global-activate-package-install-global-dart-tool)

2. **In-Process Evaluation**
   - 2.1 [dart_eval — runtime eval() inside a Dart program](#21-dart_eval--runtime-eval-inside-a-dart-program)

3. **Container-Based Workflows**
   - 3.1 [docker run --mount type=tmpfs … dart \<script\> (tmpfs Runtime)](#31-docker-run---mount-typetmpfs--dart-script-tmpfs-runtime)

4. **Alternate Compile Targets / Tooling**
   - 4.1 [dart compile exe \<file.dart\> + ./binary (AOT Native Binary)](#41-dart-compile-exe-filedart--binary-aot-native-binary)
   - 4.2 [dart compile js \<file.dart\> + node out.js (Dart → JavaScript)](#42-dart-compile-js-filedart--node-outjs-dart--javascript)
   - 4.3 [dart analyze (Static Analysis)](#43-dart-analyze-static-analysis)

---

## 1. **Project / File Execution**

### 1.1 dart pub get (Resolve Dependencies)
**Method:** Reads `pubspec.yaml`, fetches all declared packages (e.g. `dart_eval`, `interactive`), and writes `pubspec.lock`. Mandatory first step before `dart run` for any project that has dependencies.

**Locations:**
- [dart/codeforces_script/pubspec.yaml](../dart/codeforces_script/pubspec.yaml) - declares deps including `dart_eval`
  - Remote (submodule `dart/codeforces_script` @ branch `dart_`): [pubspec.yaml](https://github.com/aqwertyuiop48/codeforces_script/blob/dart_/pubspec.yaml)

**Workflow yml (executes in CI):**
- [dart/codeforces_script/.github/workflows/main.yml](../dart/codeforces_script/.github/workflows/main.yml#L14-L17) - `dart-lang/setup-dart@v1` then `dart pub get`

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L125) — submodule sync that triggers the `dart_` branch run

**Example:**
```bash
dart pub get
```

### 1.2 dart run \<file.dart\> (Direct File Execution)
**Method:** Compiles the `.dart` source on the fly with the Dart VM JIT and executes its `main()`. Standard way to run a Dart program from source.

**Locations:**
- [dart/codeforces_script/execute/hello.dart](../dart/codeforces_script/execute/hello.dart) - `print('Hello, Dart World!');`
- [dart/codeforces_script/execute/stub.dart](../dart/codeforces_script/execute/stub.dart) - exercises `dart_eval` (see §2.1)
  - Remote: [execute/](https://github.com/aqwertyuiop48/codeforces_script/tree/dart_/execute)

**Workflow yml (executes in CI):**
- [dart/codeforces_script/.github/workflows/main.yml](../dart/codeforces_script/.github/workflows/main.yml#L20-L21) - `dart run execute/hello.dart`
- [dart/codeforces_script/.github/workflows/main.yml](../dart/codeforces_script/.github/workflows/main.yml#L24) - `dart run execute/stub.dart`

**Example:**
```bash
dart run execute/hello.dart
```

### 1.3 dart pub global activate \<package\> (Install Global Dart Tool)
**Method:** Downloads a pub.dev package and exposes its executables on `$PATH` via `~/.pub-cache/bin`. Used here to install the `interactive` REPL-style tool.

**Workflow yml (executes in CI):**
- [dart/codeforces_script/.github/workflows/main.yml](../dart/codeforces_script/.github/workflows/main.yml#L22) - `dart pub global activate interactive`

**Example:**
```bash
dart pub global activate interactive
```

---

## 2. **In-Process Evaluation**

### 2.1 dart_eval — runtime eval() inside a Dart program
**Method:** The `dart_eval` package compiles arbitrary Dart source strings at runtime and executes them inside the host Dart program — analogous to `eval()` in JavaScript. Allows dynamic generation and evaluation of Dart code without invoking a separate compiler.

**Locations:**
- [dart/codeforces_script/execute/stub.dart](../dart/codeforces_script/execute/stub.dart) - imports `package:dart_eval/dart_eval.dart`, calls `eval('2 + 2')` and `eval(program, function: 'main')`

**Workflow yml (executes in CI):**
- [dart/codeforces_script/.github/workflows/main.yml](../dart/codeforces_script/.github/workflows/main.yml#L24) - the `dart run execute/stub.dart` step exercises the `eval(...)` calls

**Example:**
```dart
import 'package:dart_eval/dart_eval.dart';

void main() {
  print(eval('2 + 2'));                         // → 4
  print(eval('void main() => "Hi";', function: 'main'));
}
```

---

## 3. **Container-Based Workflows**

### 3.1 docker run --mount type=tmpfs … dart \<script\> (tmpfs Runtime)
**Method:** Pull the official `dart:stable` image, mount an in-memory `tmpfs` at `/tmp/my_tmpfs`, write a Dart source file into the tmpfs from the host, then invoke `dart /tmp/my_tmpfs/myscript.dart` inside the container. Everything beyond `dart:stable` is ephemeral.

**Workflow yml (executes in CI):**
- [dart/codeforces_script/.github/workflows/main.yml](../dart/codeforces_script/.github/workflows/main.yml#L27-L45) - `docker pull dart:stable`, then `docker run --rm --mount type=tmpfs,dst=/tmp/my_tmpfs dart:stable bash -c "<heredoc'd Dart source>"` and `dart /tmp/my_tmpfs/myscript.dart`

**Example:**
```bash
docker run --rm --mount type=tmpfs,dst=/tmp/my_tmpfs dart:stable bash -c '
  cat > /tmp/my_tmpfs/myscript.dart << "EOF"
void main() { print("Hello from Dart in tmpfs!"); }
EOF
  dart /tmp/my_tmpfs/myscript.dart
'
```

---

## 4. **Alternate Compile Targets / Tooling**

### 4.1 dart compile exe \<file.dart\> + ./binary (AOT Native Binary)
**Method:** `dart compile exe` ahead-of-time-compiles a Dart source file to a self-contained native ELF binary (no Dart SDK required at runtime). Fundamentally different runtime model than `dart run` (which uses the JIT VM).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `dart compile exe /tmp/hello_aot.dart -o /tmp/hello_aot_bin && /tmp/hello_aot_bin`

**Example:**
```bash
dart compile exe hello.dart -o hello_bin
./hello_bin
```

### 4.2 dart compile js \<file.dart\> + node out.js (Dart → JavaScript)
**Method:** `dart compile js` transpiles a Dart source file to a single self-contained `.js` bundle that runs under Node.js (or any JS host). Targets browser / Node deployments without a Dart runtime.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `dart compile js /tmp/hello_djs.dart -o /tmp/hello_djs.js && node /tmp/hello_djs.js`

**Example:**
```bash
dart compile js hello.dart -o hello.js
node hello.js
```

### 4.3 dart analyze (Static Analysis)
**Method:** Runs the Dart analyzer over the current package, surfacing lints / type errors / dead code without executing anything. Standard pre-commit and CI gate for Dart projects.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml)

**Example:**
```bash
cd path/to/dart_project
dart pub get
dart analyze
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `dart pub get` | Resolve `pubspec.yaml` deps | [pubspec.yaml](../dart/codeforces_script/pubspec.yaml) |
| `dart run <file.dart>` | Run a Dart program (JIT) | [hello.dart](../dart/codeforces_script/execute/hello.dart) |
| `dart pub global activate <pkg>` | Install a global Dart tool | [main.yml#L22](../dart/codeforces_script/.github/workflows/main.yml#L22) |
| `dart_eval` runtime `eval()` | In-process Dart string evaluation | [stub.dart](../dart/codeforces_script/execute/stub.dart) |
| `docker run --mount type=tmpfs … dart` | Ephemeral tmpfs runtime | [main.yml#L27-L45](../dart/codeforces_script/.github/workflows/main.yml#L27-L45) |
| `dart compile exe <file.dart>` + `./bin` | AOT native binary | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `dart compile js <file.dart>` + `node out.js` | Dart → JavaScript transpile | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `dart analyze` | Static analysis | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
