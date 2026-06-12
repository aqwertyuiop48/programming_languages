# CoffeeScript Execution Methods in Programming Languages Repository

This document catalogues **all distinct CoffeeScript methods** discovered for running CoffeeScript code throughout the repository. CoffeeScript has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `coffeescript` npm package installed globally (`npm install -g coffeescript`).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [coffee \<file.coffee\> (Direct File Execution)](#11-coffee-filecoffee-direct-file-execution)

2. **Inline / Transpile**
   - 2.1 [coffee -e "..." (Inline Expression)](#21-coffee--e--inline-expression)
   - 2.2 [coffee -p -e "..." (Compile to JS Without Executing)](#22-coffee--p--e--compile-to-js-without-executing)

---

## 1. **Direct File Execution**

### 1.1 coffee \<file.coffee\> (Direct File Execution)
**Method:** Invoke the `coffee` CLI on a `.coffee` source file. It transpiles the file to JavaScript on the fly and runs the result via Node.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L597) — `coffee /tmp/hello.coffee`

**Example:**
```bash
coffee hello.coffee
```

---

## 2. **Inline / Transpile**

### 2.1 coffee -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted argument as CoffeeScript and executes the resulting JS. Analog of `node -e`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L607) — `coffee -e 'console.log "Hello from coffee -e!"; console.log "Sum: " + [1,2,3,4,5].reduce ((a,b)->a+b), 0'`

**Example:**
```bash
coffee -e 'console.log "hi from coffee -e!"'
```

### 2.2 coffee -p -e "..." (Compile to JS Without Executing)
**Method:** `-p` prints the compiled JavaScript to stdout instead of executing it. Combined with `-e` it transpiles an inline CoffeeScript expression without running it — useful for inspecting the generated JS.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L611) — `coffee -p -e 'console.log "Hello from coffee -p -e!"'`

**Example:**
```bash
coffee -p -e 'console.log "hi"'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `coffee <file.coffee>` | Run a `.coffee` source file | [pytest4_.yml#L597](../.github/workflows/pytest4_.yml#L597) |
| `coffee -e "<expr>"` | Inline expression (compile + run) | [pytest4_.yml#L607](../.github/workflows/pytest4_.yml#L607) |
| `coffee -p -e "<expr>"` | Inline expression (print compiled JS) | [pytest4_.yml#L611](../.github/workflows/pytest4_.yml#L611) |
