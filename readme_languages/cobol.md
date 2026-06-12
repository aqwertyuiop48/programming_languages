# COBOL (GnuCOBOL) Execution Methods in Programming Languages Repository

This document catalogues **all distinct COBOL methods** discovered for running COBOL code throughout the repository. COBOL has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `gnucobol` apt package (which provides the `cobc` compiler).

## Table of Contents

1. **Compile + Run**
   - 1.1 [cobc -x \<file.cob\> + ./binary (Compile + Run)](#11-cobc--x-filecob--binary-compile--run)

---

## 1. **Compile + Run**

### 1.1 cobc -x \<file.cob\> + ./binary (Compile + Run)
**Method:** `cobc -x` compiles a `.cob` source file to a standalone native executable (rather than a shared library). Use `-o <path>` to control the output location, then execute the resulting binary.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L573) — `cobc -x -o /tmp/hello_cobol /tmp/hello.cob && /tmp/hello_cobol`

**Example:**
```bash
cobc -x -o hello hello.cob
./hello
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `cobc -x <file.cob>` + `./bin` | Compile COBOL → native executable + run | [pytest4_.yml#L573](../.github/workflows/pytest4_.yml#L573) |
