# C++ Execution Methods in Programming Languages Repository

This document catalogues **all distinct C++-only methods** discovered for building, running, and executing C++ code throughout the repository. Pure C++ only — pure C, Objective-C++, and assembly variants are excluded (they have their own dedicated documents). Each method takes C++ source code as input and produces the program's output. If a single command performs compile + run together, that counts as one method; otherwise the compile and run commands are paired into a single method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [g++ + ./out (Compile + Run Pair)](#11-g--out-compile--run-pair)
   - 1.2 [g++ -x c++ - <<EOF (Stdin Heredoc)](#12-g--x-c---eof-stdin-heredoc)
   - 1.3 [echo '...' | g++ -x c++ - (Piped Stdin)](#13-echo---g--x-c---piped-stdin)
   - 1.4 [clang++ + ./out (Alternate Compiler)](#14-clang--out-alternate-compiler)

2. **CI/CD Workflows**
   - 2.1 [GitHub Actions Loop: g++ + Boost + Execute](#21-github-actions-loop-g--boost--execute)
   - 2.2 [GitHub Actions Combined C/C++ Build](#22-github-actions-combined-cc-build)

3. **Build Systems**
   - 3.1 [CMake + Android Gradle externalNativeBuild](#31-cmake--android-gradle-externalnativebuild)

4. **Polyglot Embedding (C++ ➜ Other)**
   - 4.1 [C++ system() ➜ python3 -c](#41-c-system--python3--c)
   - 4.2 [C++ system() ➜ node -e](#42-c-system--node--e)
   - 4.3 [C++ system() ➜ rustc Heredoc](#43-c-system--rustc-heredoc)

5. **Polyglot Embedding (Other ➜ C++)**
   - 5.1 [Java ProcessBuilder + bash + g++](#51-java-processbuilder--bash--g)
   - 5.2 [Rust Command + bash + g++](#52-rust-command--bash--g)

---

## 1. **Direct Compilation & Execution**

### 1.1 g++ + ./out (Compile + Run Pair)
**Method:** Standard `g++` compile of a `.cpp` file followed by execution of the resulting binary, paired as a single end-to-end step (chained with `&&` or run sequentially in CI).

**Locations:**
- [CPP/codeforces_script/.github/workflows/main.yml](../CPP/codeforces_script/.github/workflows/main.yml#L32-L48) - Loop: `g++ "$file" -o "${file%.cpp}" -lboost_*` then loop runs each `./"$exe"`
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/.github/workflows/main.yml#L32-L48](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/.github/workflows/main.yml#L32-L48)
- [CPP/codeforces_script/cpp_/](../CPP/codeforces_script/cpp_/) - hello.cpp, hello1.cpp, stl_vectors.cpp, boost_.cpp, trial.cpp, c_.cpp, rust_in_cpp.cpp driven by the above loop
- [CPP/readme.txt](../CPP/readme.txt) - Documented `g++ hello.cpp -o hello && ./hello` pattern
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L76-L85) - Canonical inline demo

**Example:**
```bash
g++ /tmp/hello.cpp -o /tmp/hello_cpp && /tmp/hello_cpp
# or, in a loop with Boost:
g++ "$file" -o "${file%.cpp}" -lboost_system -lboost_filesystem -lboost_regex
```

### 1.2 g++ -x c++ - <<EOF (Stdin Heredoc)
**Method:** Pipe C++ source directly into `g++` via a shell heredoc with `-x c++` (force language) and `-` (read source from stdin). Compiles to a named output binary, which is then executed. No source file written to disk.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L87-L96) - `g++ -x c++ -o /tmp/hello_cpp_heredoc - <<'EOF' … EOF` then run

**Example:**
```bash
g++ -x c++ -o /tmp/a - <<'EOF'
#include <iostream>
int main(){ std::cout << "hi\n"; }
EOF
/tmp/a
```

### 1.3 echo '...' | g++ -x c++ - (Piped Stdin)
**Method:** One-liner pipe of C++ source from `echo` into `g++ -x c++ -`, chained with `&&` to run the binary. The form used by polyglot embedders (Java/Rust) but also valid as a direct CI step.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L98-L102) - Direct demo
- [java/codeforces_script/execute1/cpp_in_java.java](../java/codeforces_script/execute1/cpp_in_java.java#L51) - Used via ProcessBuilder (see §5.1)
- [rust/codeforces_script/execute/cpp_.rs](../rust/codeforces_script/execute/cpp_.rs#L15-L25) - Used via Rust Command (see §5.2)

**Example:**
```bash
echo '#include <iostream>
int main(){std::cout<<"hi\n";}' | g++ -x c++ -o /tmp/a - && /tmp/a
```

### 1.4 clang++ + ./out (Alternate Compiler)
**Method:** Same compile-and-run pair as §1.1 but using LLVM's `clang++` instead of GCC. Useful when targeting alternate ABI behaviour, sanitizers (`-fsanitize=…`), or macOS/Frameworks linkage. The Objective-C++ workflow uses the same `clang++` driver via `.mm` files with `-framework Foundation` for native macOS APIs.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L104-L111) - `clang++ /tmp/hello_clangpp.cpp -o /tmp/hello_clangpp && /tmp/hello_clangpp`
- [objective_c_cpp/codeforces_script/.github/workflows/main.yml](../objective_c_cpp/codeforces_script/.github/workflows/main.yml#L37-L39) - `clang++ -std=c++11 -framework Foundation -o run_python_node hello.mm && ./run_python_node` (Objective-C++ uses the same compiler driver)
  - Remote (submodule `objective_c_cpp/codeforces_script` @ branch `objective_c_`): [objective_c_cpp/codeforces_script/.github/workflows/main.yml#L37-L39](https://github.com/aqwertyuiop48/codeforces_script/blob/objective_c_/.github/workflows/main.yml#L37-L39)

**Example:**
```bash
clang++ -std=c++17 hello.cpp -o hello && ./hello
```

---

## 2. **CI/CD Workflows**

### 2.1 GitHub Actions Loop: g++ + Boost + Execute
**Method:** GitHub Actions step that installs `libboost-all-dev`, then iterates every `*.cpp` in a directory, links each against multiple Boost components (`-lboost_system -lboost_filesystem -lboost_regex`), then a second step iterates the resulting executables and runs them.

**Locations:**
- [CPP/codeforces_script/.github/workflows/main.yml](../CPP/codeforces_script/.github/workflows/main.yml#L24-L48) - Install Boost + compile loop + run loop
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/.github/workflows/main.yml#L24-L48](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/.github/workflows/main.yml#L24-L48)

**Example:**
```yaml
- name: Compile C++ programs
  run: |
    cd cpp_
    for file in *.cpp; do
      g++ "$file" -o "${file%.cpp}" -lboost_system -lboost_filesystem -lboost_regex
    done
- name: Run C++ programs
  run: |
    cd cpp_
    for exe in *; do
      [[ -x "$exe" ]] && ./"$exe"
    done
```

### 2.2 GitHub Actions Combined C/C++ Build
**Method:** GitHub Actions step that compiles a single source file twice — once as C (with `-DBUILD_AS_C -xc … -c`) and once as C++ — then links both objects with `g++` into a single executable that's run in a follow-up step. Demonstrates the C/C++ linkage boundary in one workflow.

**Locations:**
- [CPP/codeforces_script/.github/workflows/builds.yml](../CPP/codeforces_script/.github/workflows/builds.yml#L1-L24) - `combo.cpp` compiled twice + linked + executed
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/.github/workflows/builds.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/.github/workflows/builds.yml)
- [CPP/codeforces_script/combo.cpp](../CPP/codeforces_script/combo.cpp) - Source file with `BUILD_AS_C` ifdef guards

**Example:**
```bash
gcc -DBUILD_AS_C -xc combo.cpp -o combo_c.o -c
g++ combo.cpp combo_c.o -o runme
./runme
```

---

## 3. **Build Systems**

### 3.1 CMake + Android Gradle externalNativeBuild
**Method:** Android Gradle Plugin's `externalNativeBuild` block points at a `CMakeLists.txt`; the AGP invokes CMake (which in turn invokes the NDK's `clang++`) during `./gradlew build` / `connectedAndroidTest`. The compiled native library is executed on-device via JNI when the instrumented Java tests run. Source-to-output is integrated through a single Gradle invocation.

**Locations:**
- [java/android_/testing-samples/unit/BasicNativeAndroidTest/app/src/main/cpp/CMakeLists.txt](../java/android_/testing-samples/unit/BasicNativeAndroidTest/app/src/main/cpp/CMakeLists.txt) - `add_library(adder SHARED src/adder.cpp)` + Google Test linkage
- [java/android_/testing-samples/unit/BasicNativeAndroidTest/app/build.gradle](../java/android_/testing-samples/unit/BasicNativeAndroidTest/app/build.gradle#L18-L27) - `externalNativeBuild { cmake { path "src/main/cpp/CMakeLists.txt" } }`
- [java/android_/testing-samples/unit/BasicNativeAndroidTest/app/src/main/cpp/src/adder.cpp](../java/android_/testing-samples/unit/BasicNativeAndroidTest/app/src/main/cpp/src/adder.cpp), [test/adder_test.cpp](../java/android_/testing-samples/unit/BasicNativeAndroidTest/app/src/main/cpp/test/adder_test.cpp) - Source under test

**Example:**
```bash
./gradlew connectedAndroidTest
# AGP runs CMake → NDK clang++ → JNI .so → on-device JUnit + GTest
```

---

## 4. **Polyglot Embedding (C++ ➜ Other)**

### 4.1 C++ system() ➜ python3 -c
**Method:** C++ program builds a Python one-liner as a `std::string`, then calls `system(cmd.c_str())` to spawn `python3 -c "<code>"`. Output appears on the C++ process's stdout. Inverse of [the Java/Rust embedders in §5](#5-polyglot-embedding-other--c).

**Locations:**
- [CPP/codeforces_script/cpp_/trial.cpp](../CPP/codeforces_script/cpp_/trial.cpp#L7-L20) - Live example
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/cpp_/trial.cpp#L7-L20](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/trial.cpp#L7-L20)
- [CPP/readme.txt](../CPP/readme.txt#L50-L80) - Documented embedding pattern

**Example:**
```cpp
#include <cstdlib>
#include <string>
int main() {
    std::string cmd = "python3 -c \"print('hi from python'); print(2+4)\"";
    return system(cmd.c_str());
}
```

### 4.2 C++ system() ➜ node -e
**Method:** Same pattern as §4.1 but the target subprocess is `node -e "<JS code>"`. C++ → Node.js polyglot bridge.

**Locations:**
- [CPP/codeforces_script/cpp_/trial.cpp](../CPP/codeforces_script/cpp_/trial.cpp#L29-L37) - Live example
- [CPP/readme.txt](../CPP/readme.txt#L80-L110) - Documented embedding pattern

**Example:**
```cpp
std::string cmd = "node -e \"console.log(2+3+' from nodejs');\"";
system(cmd.c_str());
```

### 4.3 C++ system() ➜ rustc Heredoc
**Method:** C++ builds a Rust source string, escapes it into a shell command of the form `echo '<rust>' | rustc -o <bin> - && ./<bin>`, runs it through `system()`, then optionally `system("rm -f <bin>")` for cleanup. C++ → Rust polyglot bridge with on-the-fly compilation.

**Locations:**
- [CPP/codeforces_script/cpp_/rust_in_cpp.cpp](../CPP/codeforces_script/cpp_/rust_in_cpp.cpp#L1-L45) - Full pattern with stdin + file IO Rust example
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/cpp_/rust_in_cpp.cpp](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/rust_in_cpp.cpp)

**Example:**
```cpp
std::string cmd = "echo '" + rust_code + "' | rustc -o /tmp/h - && /tmp/h";
system(cmd.c_str());
system("rm -f /tmp/h");
```

---

## 5. **Polyglot Embedding (Other ➜ C++)**

### 5.1 Java ProcessBuilder + bash + g++
**Method:** A Java program uses `ProcessBuilder("bash", "-c", "echo '<cpp>' | g++ -x c++ -o a - && ./a")` to compile + run C++ source built as a Java `String`. The Java caller reads the binary's stdout through the process pipes.

**Locations:**
- [java/codeforces_script/execute1/cpp_in_java.java](../java/codeforces_script/execute1/cpp_in_java.java#L50-L65) - Full implementation including file IO integration
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/execute1/cpp_in_java.java#L50-L65](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/execute1/cpp_in_java.java#L50-L65)

**Example:**
```java
ProcessBuilder pb = new ProcessBuilder("bash", "-c",
    "echo '#include <iostream>\nint main(){std::cout<<\"hi\\n\";}' | g++ -x c++ -o a - && ./a");
pb.redirectErrorStream(true);
pb.start().waitFor();
```

### 5.2 Rust Command + bash + g++
**Method:** A Rust program uses `std::process::Command::new("bash").arg("-c").arg("echo '<cpp>' | g++ -x c++ -o a - && ./a")` to compile + run C++ source embedded as a Rust `&str`. Rust captures the binary's stdout via `Command::output()`.

**Locations:**
- [rust/codeforces_script/execute/cpp_.rs](../rust/codeforces_script/execute/cpp_.rs#L1-L30) - Basic embedded C++
- [rust/codeforces_script/execute/cpp_1.rs](../rust/codeforces_script/execute/cpp_1.rs#L1-L50) - Variant with file IO + parameter substitution
  - Remote (submodule `rust/codeforces_script` @ branch `rust_`): [rust/codeforces_script/execute/cpp_.rs](https://github.com/aqwertyuiop48/codeforces_script/blob/rust_/execute/cpp_.rs)

**Example:**
```rust
use std::process::Command;
let output = Command::new("bash")
    .arg("-c")
    .arg("echo '#include <iostream>\nint main(){std::cout<<\"hi\\n\";}' | g++ -x c++ -o a - && ./a")
    .output()
    .expect("failed");
println!("{}", String::from_utf8_lossy(&output.stdout));
```
