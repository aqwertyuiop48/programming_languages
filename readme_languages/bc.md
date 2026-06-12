# bc Execution Methods in Programming Languages Repository

This document catalogues **all distinct bc methods** discovered for running bc code throughout the repository. `bc` is the POSIX arbitrary-precision calculator language. It has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `bc` apt package.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [bc \<file.bc\> (bc Program from File)](#11-bc-filebc-bc-program-from-file)

2. **Inline / Stdin**
   - 2.1 [echo '...' \| bc (bc Inline Expression via Pipe)](#21-echo---bc-bc-inline-expression-via-pipe)
   - 2.2 [bc \<\<EOF (Heredoc on Stdin)](#22-bc-eof-heredoc-on-stdin)

---

## 1. **Direct File Execution**

### 1.1 bc \<file.bc\> (bc Program from File)
**Method:** Pass a `.bc` source file as the argument to `bc`. `-q` suppresses the welcome banner. The file should end with `quit` to ensure `bc` exits.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L653) — `bc -q /tmp/hello.bc`

**Example:**
```bash
bc -q hello.bc
```

---

## 2. **Inline / Stdin**

### 2.1 echo '...' \| bc (bc Inline Expression via Pipe)
**Method:** Pipe a bc expression via stdin. Use `-l` to enable the math library (provides `s()`, `c()`, `a()`, `l()`, `e()` and a default `scale=20`). One-liner equivalent of `python -c`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L665) — `echo 'scale=10; 4*a(1)' | bc -l` and `echo '"Hello from bc via pipe! Sum: "; 1+2+3+4+5' | bc -q`

**Example:**
```bash
echo '2+2' | bc
echo 'scale=10; 4*a(1)' | bc -l
```

### 2.2 bc \<\<EOF (Heredoc on Stdin)
**Method:** Multi-line variant of §2.1 — feed a shell heredoc to `bc` to embed several statements (variables, loops, prints) inline.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L670) — `bc -q <<'EOF' … EOF`

**Example:**
```bash
bc -q <<'EOF'
s = 0
for (i = 1; i <= 5; i++) s += i
"Sum: "; s
quit
EOF
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `bc <file.bc>` | Run a bc program from file | [pytest4_.yml#L653](../.github/workflows/pytest4_.yml#L653) |
| `echo '...' \| bc` | Inline expression via pipe | [pytest4_.yml#L665](../.github/workflows/pytest4_.yml#L665) |
| `bc <<EOF … EOF` | Multi-line heredoc on stdin | [pytest4_.yml#L670](../.github/workflows/pytest4_.yml#L670) |
