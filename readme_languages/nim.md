# Nim Execution Methods in Programming Languages Repository

This document catalogues **all distinct Nim methods** discovered for running Nim code throughout the repository. Nim has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `nim` apt package.

## Table of Contents

1. **Compile + Run (Single Step)**
   - 1.1 [nim r \<file.nim\> (Compile + Run in One Step)](#11-nim-r-filenim-compile--run-in-one-step)

2. **Compile + Run (Paired)**
   - 2.1 [nim c \<file.nim\> + ./binary (Compile to Native + Run)](#21-nim-c-filenim--binary-compile-to-native--run)

3. **Inline / NimScript**
   - 3.1 [nim e - (NimScript Inline Expression via Stdin)](#31-nim-e---nimscript-inline-expression-via-stdin)

---

## 1. **Compile + Run (Single Step)**

### 1.1 nim r \<file.nim\> (Compile + Run in One Step)
**Method:** `nim r` compiles and immediately runs a `.nim` file. Equivalent to `go run`. Use `--hints:off --verbosity:0` to silence the compiler output.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L438) — `nim r --hints:off --verbosity:0 /tmp/hello.nim`

**Example:**
```bash
nim r hello.nim
```

---

## 2. **Compile + Run (Paired)**

### 2.1 nim c \<file.nim\> + ./binary (Compile to Native + Run)
**Method:** `nim c` produces a persistent native binary. Use `-o:<path>` to control the output location, then invoke the binary directly. Two-step pipeline ideal for deployable artifacts.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L449) — `nim c --hints:off --verbosity:0 -o:/tmp/hello_nim /tmp/hello.nim && /tmp/hello_nim`

**Example:**
```bash
nim c -o:hello hello.nim
./hello
```

---

## 3. **Inline / NimScript**

### 3.1 nim e - (NimScript Inline Expression via Stdin)
**Method:** `nim e` runs a NimScript program. With `-` as the file argument it reads the script from stdin — convenient for one-shot inline expressions.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L454) — `echo 'echo "Hello from nim e! Sum: ", (1+2+3+4+5)' | nim e -`

**Example:**
```bash
echo 'echo "hi from nim e!"' | nim e -
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `nim r <file.nim>` | Single-step compile + run | [pytest4_.yml#L438](../.github/workflows/pytest4_.yml#L438) |
| `nim c <file.nim>` + `./bin` | Compile to native + run | [pytest4_.yml#L449](../.github/workflows/pytest4_.yml#L449) |
| `echo … \| nim e -` | NimScript inline expression | [pytest4_.yml#L454](../.github/workflows/pytest4_.yml#L454) |
