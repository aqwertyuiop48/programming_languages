# Groovy Execution Methods in Programming Languages Repository

This document catalogues **all distinct Groovy methods** discovered for running Groovy code throughout the repository. Groovy has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `groovy` apt package.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [groovy \<file.groovy\> (Direct Interpreter Invocation)](#11-groovy-filegroovy-direct-interpreter-invocation)

2. **Inline / Stdin**
   - 2.1 [groovy -e "..." (Inline Expression)](#21-groovy--e--inline-expression)
   - 2.2 [cat file.groovy \| groovy - (Program from Stdin)](#22-cat-filegroovy--groovy---program-from-stdin)

---

## 1. **Direct File Execution**

### 1.1 groovy \<file.groovy\> (Direct Interpreter Invocation)
**Method:** Invoke the `groovy` launcher on a `.groovy` source file. Groovy compiles it to JVM bytecode on the fly and executes it.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L216) — `groovy /tmp/hello.groovy`

**Example:**
```bash
groovy hello.groovy
```

---

## 2. **Inline / Stdin**

### 2.1 groovy -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted argument as Groovy source and exits. Groovy analog of `python -c` / `node -e`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L225) — `groovy -e 'println "Hello from groovy -e!"; println "Sum: " + [1,2,3,4,5].sum()'`

**Example:**
```bash
groovy -e 'println "hi from groovy -e!"'
```

### 2.2 cat file.groovy \| groovy - (Program from Stdin)
**Method:** Passing `-` as the script argument makes `groovy` read source from stdin. Useful when the program is generated on the fly or piped from another command.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L229) — `cat /tmp/hello.groovy | groovy -`

**Example:**
```bash
echo 'println "hi from stdin"' | groovy -
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `groovy <file.groovy>` | Run a `.groovy` source file | [pytest3_.yml#L216](../.github/workflows/pytest3_.yml#L216) |
| `groovy -e "<expr>"` | Inline expression | [pytest3_.yml#L225](../.github/workflows/pytest3_.yml#L225) |
| `cat <file> \| groovy -` | Program from stdin via pipe | [pytest3_.yml#L229](../.github/workflows/pytest3_.yml#L229) |
