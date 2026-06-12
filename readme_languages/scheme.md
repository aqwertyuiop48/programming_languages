# Scheme (GNU Guile) Execution Methods in Programming Languages Repository

This document catalogues **all distinct Scheme methods** discovered for running Scheme code throughout the repository. Scheme has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `guile-3.0` apt package (GNU Guile, an R5RS/R6RS/R7RS Scheme implementation).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [guile \<file.scm\> (Scheme File Execution)](#11-guile-filescm-scheme-file-execution)

2. **Inline / Stdin**
   - 2.1 [guile -c "..." (Scheme Inline Expression)](#21-guile--c--scheme-inline-expression)
   - 2.2 [echo '...' \| guile (Program from Stdin via Pipe)](#22-echo---guile-program-from-stdin-via-pipe)

---

## 1. **Direct File Execution**

### 1.1 guile \<file.scm\> (Scheme File Execution)
**Method:** Invoke the `guile` interpreter on a `.scm` source file. Standard way to run a Scheme program.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L517) — `guile /tmp/hello.scm`

**Example:**
```bash
guile hello.scm
```

---

## 2. **Inline / Stdin**

### 2.1 guile -c "..." (Scheme Inline Expression)
**Method:** `-c` evaluates the quoted Scheme expression and exits. Guile/Scheme analog of `python -c`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L526) — `guile -c '(begin (display "Hello from guile -c!") (newline) (display (apply + (list 1 2 3 4 5))) (newline))'`

**Example:**
```bash
guile -c '(display "hi from guile!") (newline)'
```

### 2.2 echo '...' \| guile (Program from Stdin via Pipe)
**Method:** With no positional argument `guile` reads its program from stdin. Pipe Scheme code via `echo` or any command emitting Scheme on stdout.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L530) — `echo '(display "Hello from guile via pipe!") (newline)' | guile`

**Example:**
```bash
echo '(display "hi") (newline)' | guile
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `guile <file.scm>` | Run a `.scm` source file | [pytest4_.yml#L517](../.github/workflows/pytest4_.yml#L517) |
| `guile -c "<expr>"` | Inline Scheme expression | [pytest4_.yml#L526](../.github/workflows/pytest4_.yml#L526) |
| `echo '...' \| guile` | Program from stdin via pipe | [pytest4_.yml#L530](../.github/workflows/pytest4_.yml#L530) |
