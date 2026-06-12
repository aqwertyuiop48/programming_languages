# Smalltalk (Pharo) Execution Methods in Programming Languages Repository

This document catalogues **all distinct Smalltalk methods** discovered for running Smalltalk code throughout the repository. Smalltalk has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using **Pharo** (a modern Smalltalk).

> **Note:** Smalltalk is installed via the Pharo one-liner (`curl https://get.pharo.org/64/ | bash`), **NOT** apt — the `gnu-smalltalk` package was dropped from Ubuntu 24.04 (noble). The installer drops a `pharo` launcher plus `Pharo.image` / `Pharo.changes` into the current directory.

## Table of Contents

1. **Inline Expression**
   - 1.1 [pharo Pharo.image eval "..." (Smalltalk Inline Expression)](#11-pharo-pharoimage-eval--smalltalk-inline-expression)

2. **File Execution**
   - 2.1 [pharo Pharo.image st \<file.st\> (Smalltalk File Execution)](#21-pharo-pharoimage-st-filest-smalltalk-file-execution)

---

## 1. **Inline Expression**

### 1.1 pharo Pharo.image eval "..." (Smalltalk Inline Expression)
**Method:** `./pharo --headless Pharo.image eval --save '<smalltalk-expr>'` evaluates the given Smalltalk expression against the Pharo image. `--headless` disables the GUI; `--save` persists any image changes.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L470) — `./pharo --headless Pharo.image eval --save "'Hello from Pharo eval! Sum: ', ((1 to: 5) inject: 0 into: [:a :b | a + b]) printString"`

**Example:**
```bash
./pharo --headless Pharo.image eval --save "'hi from pharo!'"
```

---

## 2. **File Execution**

### 2.1 pharo Pharo.image st \<file.st\> (Smalltalk File Execution)
**Method:** `./pharo --headless Pharo.image st --quit <file.st>` loads and runs a Smalltalk source file in CHUNK format. `--quit` exits after the file finishes.

> **Format quirk:** Pharo's `st` command reads `.st` files in CHUNK format where `!` is the chunk terminator. To embed a literal `!` inside a string, double it (`!!`). Otherwise the string is silently truncated at the first `!` and the parser reports `Unmatched '`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L476) — `./pharo --headless Pharo.image st --quit /tmp/hello.st`

**Example:**
```bash
./pharo --headless Pharo.image st --quit hello.st
```

---

## Install (referenced by the workflow)

**Pharo install step (workflow):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L463) — `curl -fsSL https://get.pharo.org/64/ | bash` into `/tmp/pharo`

```bash
mkdir -p /tmp/pharo && cd /tmp/pharo
curl -fsSL https://get.pharo.org/64/ | bash
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `pharo Pharo.image eval "<expr>"` | Inline Smalltalk expression | [pytest4_.yml#L470](../.github/workflows/pytest4_.yml#L470) |
| `pharo Pharo.image st <file.st>` | Run a `.st` source file (CHUNK format) | [pytest4_.yml#L476](../.github/workflows/pytest4_.yml#L476) |
