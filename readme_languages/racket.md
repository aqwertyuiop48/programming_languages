# Racket Execution Methods in Programming Languages Repository

This document catalogues **all distinct Racket methods** discovered for running Racket code throughout the repository. Racket has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `racket` apt package.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [racket \<file.rkt\> (Direct Interpreter Invocation)](#11-racket-filerkt-direct-interpreter-invocation)

2. **Inline Expression**
   - 2.1 [racket -e "..." (Inline Expression)](#21-racket--e--inline-expression)

---

## 1. **Direct File Execution**

### 1.1 racket \<file.rkt\> (Direct Interpreter Invocation)
**Method:** Invoke the `racket` runtime on a `.rkt` source file. The first line is typically `#lang racket` (or another `#lang` dialect) which selects the language.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L275) — `racket /tmp/hello.rkt`

**Example:**
```bash
racket hello.rkt
```

---

## 2. **Inline Expression**

### 2.1 racket -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted argument as Racket source and exits. Uses the default `racket/base` language unless wrapped in a `module` form.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L284) — `racket -e '(begin (displayln "Hello from racket -e!") (printf "Sum: ~a~n" (apply + (list 1 2 3 4 5))))'`

**Example:**
```bash
racket -e '(displayln "hi from racket!")'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `racket <file.rkt>` | Run a `.rkt` source file | [pytest3_.yml#L275](../.github/workflows/pytest3_.yml#L275) |
| `racket -e "<expr>"` | Inline Racket expression | [pytest3_.yml#L284](../.github/workflows/pytest3_.yml#L284) |
