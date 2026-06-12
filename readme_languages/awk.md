# AWK Execution Methods in Programming Languages Repository

This document catalogues **all distinct AWK methods** discovered for running AWK programs throughout the repository. AWK has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `gawk` (GNU awk) apt package.

## Table of Contents

1. **Inline Program**
   - 1.1 [awk 'BEGIN{...}' (Inline Program, No Input)](#11-awk-begin-inline-program-no-input)

2. **Program File**
   - 2.1 [awk -f \<file.awk\> (Program from File)](#21-awk--f-fileawk-program-from-file)

3. **Pipeline Mode**
   - 3.1 [printf … \| awk '...' (Pipeline Mode — Line Processor)](#31-printf---awk--pipeline-mode--line-processor)

---

## 1. **Inline Program**

### 1.1 awk 'BEGIN{...}' (Inline Program, No Input)
**Method:** A `BEGIN` block runs before any input is read. Using only a `BEGIN` block with no input file lets `awk` act as a one-shot expression evaluator — analog to `python -c`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L345) — `awk 'BEGIN { s=0; for (i=1; i<=5; i++) s+=i; printf "Hello from awk BEGIN! Sum: %d\n", s }'`

**Example:**
```bash
awk 'BEGIN { print "hi from awk!" }'
```

---

## 2. **Program File**

### 2.1 awk -f \<file.awk\> (Program from File)
**Method:** `-f` loads the awk program from a file. Used when the program is long enough to deserve its own `.awk` source file.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L349) — `awk -f /tmp/hello.awk`

**Example:**
```bash
awk -f script.awk
```

---

## 3. **Pipeline Mode**

### 3.1 printf … \| awk '...' (Pipeline Mode — Line Processor)
**Method:** Canonical AWK usage — pipe input lines into an awk program that processes each line (the pattern/action body) and optionally summarizes in `END`. AWK's primary historical role.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L360) — `printf 'apple 3\nbanana 5\ncherry 7\n' | awk '{ s += $2; print "got:", $0 } END { print "Total:", s }'`

**Example:**
```bash
printf 'a 1\nb 2\nc 3\n' | awk '{ s += $2 } END { print s }'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `awk 'BEGIN{...}'` | Inline one-shot program | [pytest3_.yml#L345](../.github/workflows/pytest3_.yml#L345) |
| `awk -f <file.awk>` | Program from file | [pytest3_.yml#L349](../.github/workflows/pytest3_.yml#L349) |
| `printf … \| awk '...'` | Pipeline line processor | [pytest3_.yml#L360](../.github/workflows/pytest3_.yml#L360) |
