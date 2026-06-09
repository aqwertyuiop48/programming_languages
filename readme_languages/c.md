# C Execution Methods in Programming Languages Repository

This document catalogues **all distinct C-only methods** discovered for building, running, and executing C code throughout the repository. Pure C only — Objective-C, C++, and assembly variants are excluded (they have their own dedicated documents). Each method takes C source code as input and produces the program's output. If a single command performs compile + run together, that counts as one method; otherwise the compile and run commands are paired into a single method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [gcc + ./out (Compile + Run Pair)](#11-gcc--out-compile--run-pair)
   - 1.2 [gcc -x c - <<EOF (Stdin Heredoc)](#12-gcc--x-c---eof-stdin-heredoc)
   - 1.3 [echo '...' | gcc -x c - (Piped Stdin)](#13-echo---gcc--x-c---piped-stdin)
   - 1.4 [tcc -run \<file.c\> (TinyCC Interpret Mode)](#14-tcc--run-filec-tinycc-interpret-mode)
   - 1.5 [tcc -run - <<EOF (TinyCC Stdin)](#15-tcc--run---eof-tinycc-stdin)

2. **Polyglot Embedding**
   - 2.1 [Java ProcessBuilder + bash + gcc](#21-java-processbuilder--bash--gcc)
   - 2.2 [Rust Command + bash + gcc](#22-rust-command--bash--gcc)

---

## 1. **Direct Compilation & Execution**

### 1.1 gcc + ./out (Compile + Run Pair)
**Method:** Standard `gcc` compilation of a `.c` source file followed by execution of the resulting binary. The two commands are paired into one end-to-end step (chained with `&&` or run sequentially in CI).

**Locations:**
- [CPP/codeforces_script/.github/workflows/main.yml](../CPP/codeforces_script/.github/workflows/main.yml#L50-L63) - Loop: `gcc "$file" -o "${file%.c}"` then loop runs each `./"$exe"`
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/.github/workflows/main.yml#L50-L63](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/.github/workflows/main.yml#L50-L63)
- [CPP/codeforces_script/c_/helloc.c](../CPP/codeforces_script/c_/helloc.c) and [advanced.c](../CPP/codeforces_script/c_/advanced.c) - Source files driven by the above loop
  - Remote: [helloc.c](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/c_/helloc.c), [advanced.c](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/c_/advanced.c)
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L26-L35) - Canonical inline demo (file written via heredoc, then `gcc … && ./out`)

**Example:**
```bash
gcc /tmp/hello_c.c -o /tmp/hello_c && /tmp/hello_c
# or, in a loop:
for file in *.c; do
  gcc "$file" -o "${file%.c}"
done
for exe in *; do
  [[ -x "$exe" ]] && ./"$exe"
done
```

### 1.2 gcc -x c - <<EOF (Stdin Heredoc)
**Method:** Pipe C source code directly into `gcc` via a shell heredoc with the `-x c` flag (force language) and `-` (read source from stdin). Compiles to a named output file, which is then executed. Avoids writing a source file to disk.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L37-L46) - `gcc -x c -o /tmp/hello_c_heredoc - <<'EOF' … EOF` then `/tmp/hello_c_heredoc`

**Example:**
```bash
gcc -x c -o /tmp/hello_c_heredoc - <<'EOF'
#include <stdio.h>
int main(void) { printf("Hello!\n"); return 0; }
EOF
/tmp/hello_c_heredoc
```

### 1.3 echo '...' | gcc -x c - (Piped Stdin)
**Method:** One-liner pipe of C source from `echo` (or any command emitting C source on stdout) into `gcc -x c -`, chained with `&&` to run the produced binary. Direct C analog of [python -c](python.md#21-python--c--inline-expression) / [node -e](javascript.md#12-node--e--inline-expression).

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L48-L52) - `echo '<C source>' | gcc -x c -o /tmp/hello_c_pipe - && /tmp/hello_c_pipe`

**Example:**
```bash
echo '#include <stdio.h>
int main(void){printf("hi\n");return 0;}' \
  | gcc -x c -o /tmp/a - && /tmp/a
```

### 1.4 tcc -run \<file.c\> (TinyCC Interpret Mode)
**Method:** TinyCC's single-step `-run` mode compiles and immediately executes the file in memory — no intermediate `.o` or binary on disk. Direct C analog of `java <file.java>` (single-file launcher).

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L54-L63) - `tcc -run /tmp/hello_tcc.c`

**Example:**
```bash
tcc -run /tmp/hello.c
```

### 1.5 tcc -run - <<EOF (TinyCC Stdin)
**Method:** Combine TinyCC's in-memory `-run` with `-` (stdin) and a shell heredoc — neither source file nor binary touches disk. The most minimal "C as a scripting language" form available.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L65-L69) - `tcc -run - <<'EOF' … EOF`

**Example:**
```bash
tcc -run - <<'EOF'
#include <stdio.h>
int main(void){printf("hi\n");return 0;}
EOF
```

---

## 2. **Polyglot Embedding**

### 2.1 Java ProcessBuilder + bash + gcc
**Method:** A Java program uses `ProcessBuilder` to spawn `bash -c`, which pipes C source (built up as a Java `String`) into `gcc -x c -` for in-place compile + run. The Java caller reads the binary's stdout back through the process pipes. Effectively makes C a runtime-callable scripting language from inside the JVM.

**Locations:**
- [java/codeforces_script/execute1/cpp_in_java.java](../java/codeforces_script/execute1/cpp_in_java.java#L50-L65) - C/C++ both supported via the same bash bridge
  - Remote (submodule `java/codeforces_script` @ branch `javac_`): [java/codeforces_script/execute1/cpp_in_java.java#L50-L65](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/execute1/cpp_in_java.java#L50-L65)

**Example:**
```java
ProcessBuilder pb = new ProcessBuilder("bash", "-c",
    "echo '#include <stdio.h>\nint main(){printf(\"hi\\n\");}' | gcc -x c -o a - && ./a");
pb.redirectErrorStream(true);
pb.start().waitFor();
```

### 2.2 Rust Command + bash + gcc
**Method:** A Rust program uses `std::process::Command` to spawn `bash -c`, embedding C source as a Rust string literal that is echoed into `gcc -x c -` and executed. Rust captures the binary's stdout via the command's output struct.

**Locations:**
- [rust/codeforces_script/execute/cpp_.rs](../rust/codeforces_script/execute/cpp_.rs#L1-L30) - Same bridge pattern used for C and C++ (file is named `cpp_.rs` but the technique is identical for plain C with `-x c`)
  - Remote (submodule `rust/codeforces_script` @ branch `rust_`): [rust/codeforces_script/execute/cpp_.rs#L1-L30](https://github.com/aqwertyuiop48/codeforces_script/blob/rust_/execute/cpp_.rs#L1-L30)

**Example:**
```rust
use std::process::Command;
let output = Command::new("bash")
    .arg("-c")
    .arg("echo '#include <stdio.h>\nint main(){printf(\"hi\\n\");}' | gcc -x c -o a - && ./a")
    .output()
    .expect("failed");
println!("{}", String::from_utf8_lossy(&output.stdout));
```
