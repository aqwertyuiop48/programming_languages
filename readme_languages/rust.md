# Rust Execution Methods in Programming Languages Repository

This document catalogues **all distinct Rust-only methods** discovered for building, running, and executing Rust code throughout the repository. Each method takes Rust source code as input and produces the program's output. If a single command performs compile + run together (e.g. `cargo run`, `rust-script`), that counts as one method; otherwise the compile and run commands are paired into a single method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [rustc + ./out (Compile + Run Pair)](#11-rustc--out-compile--run-pair)
   - 1.2 [rustc + cat <<EOF | ./out (Stdin to Compiled Binary)](#12-rustc--cat-eof--out-stdin-to-compiled-binary)
   - 1.3 [rustc - <<EOF (Stdin Heredoc Source)](#13-rustc---eof-stdin-heredoc-source)

2. **Cargo Build System**
   - 2.1 [cargo run (Compile + Run)](#21-cargo-run-compile--run)
   - 2.2 [cargo test (Compile + Run Tests)](#22-cargo-test-compile--run-tests)

3. **Single-File Launchers**
   - 3.1 [rust-script \<file.rs\> (Script Mode)](#31-rust-script-filers-script-mode)

4. **Web Framework & Serverless Deployment**
   - 4.1 [Cargo + Vercel (rust-axum)](#41-cargo--vercel-rust-axum)

5. **Polyglot Embedding (Rust ➜ Other)**
   - 5.1 [Rust Command ➜ bash ➜ g++](#51-rust-command--bash--g)
   - 5.2 [Rust Command ➜ python -c](#52-rust-command--python--c)

6. **Polyglot Embedding (Other ➜ Rust)**
   - 6.1 [C++ system() ➜ rustc Heredoc](#61-c-system--rustc-heredoc)

---

## 1. **Direct Compilation & Execution**

### 1.1 rustc + ./out (Compile + Run Pair)
**Method:** Standard `rustc` compile of a `.rs` source file followed by execution of the resulting binary. The two commands are paired into one end-to-end step.

**Locations:**
- [rust/codeforces_script/execute/_1_hello.rs](../rust/codeforces_script/execute/_1_hello.rs), [_4_lists.rs](../rust/codeforces_script/execute/_4_lists.rs), [hello.rs](../rust/codeforces_script/execute/hello.rs) - Source files driven by the loop
- [rust/_3_server.rs](../rust/_3_server.rs) - Standalone TCP server compiled the same way
- [rust/readme.txt](../rust/readme.txt#L2-L5) - Documented pattern

**Workflow yml (executes in CI):**
- [rust/codeforces_script/.github/workflows/main.yml](../rust/codeforces_script/.github/workflows/main.yml#L26-L30) - Loop: `for file in execute/*.rs; do rustc "$file" && ./$(basename "$file" .rs); done`
  - Remote (submodule `rust/codeforces_script` @ branch `rust_`): [rust/codeforces_script/.github/workflows/main.yml#L26-L30](https://github.com/aqwertyuiop48/codeforces_script/blob/rust_/.github/workflows/main.yml#L26-L30)
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L120-L127) - Canonical inline demo

**Example:**
```bash
rustc /tmp/hello.rs -o /tmp/hello && /tmp/hello
# or, in a loop:
for file in execute/*.rs; do
  rustc "$file" && ./$(basename "$file" .rs)
done
```

### 1.2 rustc + cat <<EOF | ./out (Stdin to Compiled Binary)
**Method:** Compile a `.rs` file with `rustc`, then pipe a shell heredoc into the resulting binary's stdin so the program receives multi-line input without an input file. Used for testing Rust programs that read from `io::stdin()`.

**Locations:**
- [rust/codeforces_script/_5_input.rs](../rust/codeforces_script/_5_input.rs) - stdin-reading source

**Workflow yml (executes in CI):**
- [rust/codeforces_script/.github/workflows/main.yml](../rust/codeforces_script/.github/workflows/main.yml#L32-L37) - `rustc _5_input.rs && cat <<EOF | ./_5_input ... EOF`
  - Remote (submodule `rust/codeforces_script` @ branch `rust_`): [rust/codeforces_script/.github/workflows/main.yml#L32-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/rust_/.github/workflows/main.yml#L32-L37)

**Example:**
```bash
rustc _5_input.rs && cat <<EOF | ./_5_input
Hello
World
EOF
```

### 1.3 rustc - <<EOF (Stdin Heredoc Source)
**Method:** Pass `-` as the source argument so `rustc` reads source from stdin, then feed it a shell heredoc. The compiled binary is written via `-o <path>` and then executed. No `.rs` file touches disk.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L129-L136) - `rustc - -o /tmp/hello_rs_heredoc <<'EOF' … EOF` then run

**Example:**
```bash
rustc - -o /tmp/hello_rs <<'EOF'
fn main() { println!("hi"); }
EOF
/tmp/hello_rs
```

---

## 2. **Cargo Build System**

### 2.1 cargo run (Compile + Run)
**Method:** Inside a Cargo project (`Cargo.toml` + `src/main.rs`), `cargo run` resolves dependencies, compiles the binary target, and executes it in a single command. This is the canonical single-command compile-and-run for Rust projects.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L138-L147) - Canonical demo: `cargo init --bin` → write `src/main.rs` → `cargo run --quiet`

**Example:**
```bash
cargo init --name demo --bin --quiet
cat > src/main.rs <<'EOF'
fn main() { println!("hi from cargo run"); }
EOF
cargo run --quiet
```

### 2.2 cargo test (Compile + Run Tests)
**Method:** `cargo test` discovers `#[test]`-annotated functions across the crate, compiles a separate test binary, and executes it — all in one command. End-to-end source → test results.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L149-L163) - Canonical demo with `add` function + `#[test] fn it_adds`

**Example:**
```rust
fn add(a: i32, b: i32) -> i32 { a + b }
#[cfg(test)]
mod tests {
    use super::*;
    #[test] fn it_adds() { assert_eq!(add(2, 3), 5); }
}
```
```bash
cargo test --quiet
```

---

## 3. **Single-File Launchers**

### 3.1 rust-script \<file.rs\> (Script Mode)
**Method:** [`rust-script`](https://rust-script.org) treats a single `.rs` file as a Rust script — it generates a hidden Cargo project, builds, and runs in one command. No manual `cargo init`. Direct Rust analog of `java <file.java>` (single-file launcher).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L165-L176) - `cargo install rust-script` then `rust-script /tmp/hello_script.rs`

**Example:**
```bash
cat > /tmp/hi.rs <<'EOF'
//! demo
fn main() { println!("hi from rust-script"); }
EOF
rust-script /tmp/hi.rs
```

---

## 4. **Web Framework & Serverless Deployment**

### 4.1 Cargo + Vercel (rust-axum)
**Method:** A Cargo project with a `[[bin]]` target wired to Vercel's `vercel_runtime` adapter. Vercel detects `Cargo.toml`, runs `cargo build --release` server-side, and routes HTTP traffic to the compiled binary. Local development uses `vc dev`. End-to-end source → live HTTP responses via one deploy.

**Locations:**
- [rust/rust-axum/Cargo.toml](../rust/rust-axum/Cargo.toml) - `[[bin]] name = "vercel-rust-axum" path = "api/axum.rs"`
- [rust/rust-axum/api/axum.rs](../rust/rust-axum/api/axum.rs) - Axum + tokio + vercel_runtime handler
- [rust/rust-axum/vercel.json](../rust/rust-axum/vercel.json) - Rewrites all requests to the Rust handler
- [rust/rust-axum/README.md](../rust/rust-axum/README.md) - Setup + `vc dev` instructions

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](.github/workflows/main.yml) _(rule R2)_ — covers `rust/rust-axum/Cargo.toml`; `rust/rust-axum/README.md`; `rust/rust-axum/api/axum.rs`; `rust/rust-axum/vercel.json`

**Example:**
```bash
# Local
vc dev
# Production
vercel --prod
```

---

## 5. **Polyglot Embedding (Rust ➜ Other)**

### 5.1 Rust Command ➜ bash ➜ g++
**Method:** Rust uses `std::process::Command::new("bash").arg("-c").arg("echo '<cpp>' | g++ -x c++ -o a - && ./a")` to compile + run C++ source built as a Rust `&str`. Rust captures the binary's stdout via `Command::output()`. See [cpp.md §5.2](cpp.md#52-rust-command--bash--g) for the C++-side perspective.

**Locations:**
- [rust/codeforces_script/execute/cpp_.rs](../rust/codeforces_script/execute/cpp_.rs#L1-L30) - Basic embedded C++
- [rust/codeforces_script/execute/cpp_1.rs](../rust/codeforces_script/execute/cpp_1.rs#L1-L50) - With file IO + parameter substitution
  - Remote (submodule `rust/codeforces_script` @ branch `rust_`): [rust/codeforces_script/execute/cpp_.rs](https://github.com/aqwertyuiop48/codeforces_script/blob/rust_/execute/cpp_.rs)

**Workflow yml (executes in CI):**
- [rust/codeforces_script/.github/workflows/main.yml](../rust/codeforces_script/.github/workflows/main.yml#L17-L30) - CI workflow: installs `g++` (L17-L18) then loop `for file in execute/*.rs; do rustc "$file" && ./$(basename "$file" .rs); done` (L26-L30) which exercises `cpp_.rs` / `cpp_1.rs` and the embedded g++ bridge end-to-end
  - Remote: [rust/codeforces_script/.github/workflows/main.yml#L17-L30](https://github.com/aqwertyuiop48/codeforces_script/blob/rust_/.github/workflows/main.yml#L17-L30)

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

### 5.2 Rust Command ➜ python -c
**Method:** Rust uses `std::process::Command::new("python").args(&["-c", "<py code>"])` to spawn Python with an inline expression. Rust captures stdout via `.output()` or pipes via `Stdio::piped()`.

**Locations:**
- [rust/_2_child_process_input.rs](../rust/_2_child_process_input.rs#L1-L45) - Multiple `python -c` invocations with `Stdio::piped`

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L302-L317) - inline shim writes a minimal `/tmp/r2py.rs` whose `main()` does `Command::new("python3").args(&["-c", "print('hi from python-via-rust', 2+2)"]).stdout(Stdio::piped()).output()`, then `rustc -O /tmp/r2py.rs -o /tmp/r2py && /tmp/r2py`, asserted with `grep -F 'hi from python-via-rust 4'`. Rust (`rustc`/`cargo`) and `python3` are both preinstalled on `ubuntu-latest`, so no setup action is needed. The `rust/_2_child_process_input.rs` file in Locations also reads from stdin (`Enter the number of apples`) so it can't be invoked directly in non-interactive CI, but the §5.2 method itself — `Command::new("python").args(&["-c", …]).stdout(Stdio::piped()).output()` — is exercised end-to-end.

**Example:**
```rust
use std::process::{Command, Stdio};
let out = Command::new("python")
    .args(&["-c", "print(2+2)"])
    .stdout(Stdio::piped())
    .output()
    .expect("failed");
println!("{}", String::from_utf8_lossy(&out.stdout));
```

---

## 6. **Polyglot Embedding (Other ➜ Rust)**

### 6.1 C++ system() ➜ rustc Heredoc
**Method:** A C++ program builds a Rust source string, then runs `system("echo '<rust>' | rustc -o /tmp/h - && /tmp/h")` followed by cleanup. The compiled Rust binary's stdout becomes the C++ program's stdout. See [cpp.md §4.3](cpp.md#43-c-system--rustc-heredoc) for the C++-side perspective.

**Locations:**
- [CPP/codeforces_script/cpp_/rust_in_cpp.cpp](../CPP/codeforces_script/cpp_/rust_in_cpp.cpp#L1-L45) - Full pattern with stdin + file IO Rust example
  - Remote (submodule `CPP/codeforces_script` @ branch `cpp_`): [CPP/codeforces_script/cpp_/rust_in_cpp.cpp](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/rust_in_cpp.cpp)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L125-L140) - "rustc - <<EOF (stdin heredoc → compile + run)" step pipes a multi-line Rust program into `rustc` via shell heredoc and runs the produced binary — the rustc-heredoc execution path used by the C++ wrapper. Demonstration coverage added this session; the C++-driven `system("... | rustc ...")` chain in [rust_in_cpp.cpp](../CPP/codeforces_script/cpp_/rust_in_cpp.cpp) is not yet wired into a workflow.

**Example:**
```cpp
std::string cmd = "echo '" + rust_code + "' | rustc -o /tmp/h - && /tmp/h";
system(cmd.c_str());
system("rm -f /tmp/h");
```
