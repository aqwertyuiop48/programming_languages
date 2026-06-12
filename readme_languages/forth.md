# Forth (gforth) Execution Methods in Programming Languages Repository

This document catalogues **all distinct Forth methods** discovered for running Forth code throughout the repository. Forth has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `gforth` apt package (GNU Forth).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [gforth \<file.fs\> (Forth File Execution)](#11-gforth-filefs-forth-file-execution)

2. **Inline Expression**
   - 2.1 [gforth -e "..." (Forth Inline Expression)](#21-gforth--e--forth-inline-expression)

---

## 1. **Direct File Execution**

### 1.1 gforth \<file.fs\> (Forth File Execution)
**Method:** Invoke `gforth` on a `.fs` source file. End the script with `bye` to terminate the interpreter; otherwise it drops to an interactive prompt after loading.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L538) — `gforth /tmp/hello.fs`

**Example:**
```bash
gforth hello.fs
```

---

## 2. **Inline Expression**

### 2.1 gforth -e "..." (Forth Inline Expression)
**Method:** `-e` evaluates the quoted Forth source and continues. End with `bye` to exit. Forth analog of `python -c`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L548) — `gforth -e '." Hello from gforth -e!" cr 1 2 + 3 + 4 + 5 + . cr bye'`

**Example:**
```bash
gforth -e '." hi from forth!" cr bye'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `gforth <file.fs>` | Run a Forth source file | [pytest4_.yml#L538](../.github/workflows/pytest4_.yml#L538) |
| `gforth -e "<expr>"` | Inline Forth expression | [pytest4_.yml#L548](../.github/workflows/pytest4_.yml#L548) |
