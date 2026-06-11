# Julia Execution Methods in Programming Languages Repository

This document catalogues **all distinct Julia methods** discovered for running Julia code throughout the repository. Julia ships a single `julia` interpreter binary that handles file execution, inline `-e` expressions, and the REPL.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [julia \<file.jl\> (Direct Interpreter Invocation)](#11-julia-filejl-direct-interpreter-invocation)

2. **Inline / One-Liners**
   - 2.1 [julia -e "..." (Inline Expression)](#21-julia--e--inline-expression)
   - 2.2 [julia -E "..." (Print Expression Value)](#22-julia--e--print-expression-value)
   - 2.3 [julia <<EOF (Heredoc on stdin)](#23-julia-eof-heredoc-on-stdin)
   - 2.4 [echo '...' | julia (Program from stdin)](#24-echo---julia-program-from-stdin)

---

## 1. **Direct File Execution**

### 1.1 julia \<file.jl\> (Direct Interpreter Invocation)
**Method:** Invoke the `julia` interpreter on a `.jl` source file. The workflow iterates the `execute/` directory and runs every Julia source it finds.

**Locations:**
- [julia_/codeforces_script/execute/hello.jl](../julia_/codeforces_script/execute/hello.jl) - `println("Hello, Julia World!")`
  - Remote (submodule `julia_/codeforces_script` @ branch `julia_`): [julia_/codeforces_script/execute/hello.jl](https://github.com/aqwertyuiop48/codeforces_script/blob/julia_/execute/hello.jl)

**Workflow yml (executes in CI):**
- [julia_/codeforces_script/.github/workflows/main.yml](../julia_/codeforces_script/.github/workflows/main.yml#L19-L23) - installs Julia via `julia-actions/setup-julia@v1` (version `1.11`), then `for file in execute/*.jl; do julia "$file"; done`
  - Remote: [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/julia_/.github/workflows/main.yml)

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L120) — submodule sync that triggers the `julia_` branch run

**Example:**
```bash
julia execute/hello.jl
```

---

## 2. **Inline / One-Liners**

### 2.1 julia -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted string as Julia source and exits. Julia analog of `python -c` / `node -e`. No file required.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `julia -e 'println("Hello from julia -e!"); println("Julia ", VERSION)'`

**Example:**
```bash
julia -e 'println("Hello from julia -e!")'
```

### 2.2 julia -E "..." (Print Expression Value)
**Method:** Like `-e`, but additionally prints the value returned by the final expression (similar to `node -p`). Useful for shell substitution.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `julia -E 'string("julia -E :: ", VERSION)'`

**Example:**
```bash
julia -E 'string("julia -E :: ", VERSION)'
```

### 2.3 julia <<EOF (Heredoc on stdin)
**Method:** With no positional argument, `julia` reads its program from stdin. A bash heredoc lets you embed a multi-line Julia program inline in a shell script.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml)

**Example:**
```bash
julia <<'EOF'
println("Hello from julia heredoc!")
nums = [1, 2, 3, 4, 5]
println("Sum: ", sum(nums))
EOF
```

### 2.4 echo '...' | julia (Program from stdin)
**Method:** Pipe a single-line Julia program into `julia` via stdin. Smaller form of §2.3.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `echo 'println("Hello from julia via pipe!")' | julia`

**Example:**
```bash
echo 'println("Hello from julia via pipe!")' | julia
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `julia <file.jl>` | Run a Julia source file end-to-end | [hello.jl](../julia_/codeforces_script/execute/hello.jl) |
| `julia -e "<julia expr>"` | Inline Julia expression | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `julia -E "<julia expr>"` | Inline expression + print value | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `julia <<EOF … EOF` | Multi-line heredoc on stdin | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `echo '...' \| julia` | Program from stdin via pipe | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
