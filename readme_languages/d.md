# D Execution Methods in Programming Languages Repository

This document catalogues **all distinct D methods** discovered for running D code throughout the repository. D has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `gdc` apt package (GCC-based D compiler).

## Table of Contents

1. **Compile + Run**
   - 1.1 [gdc \<file.d\> + ./binary (Compile + Run)](#11-gdc-filed--binary-compile--run)

---

## 1. **Compile + Run**

### 1.1 gdc \<file.d\> + ./binary (Compile + Run)
**Method:** Compile a `.d` source file with `gdc -o <binary>`, then execute the binary. `gdc` is the GCC-based D front-end (alternative to `dmd` / `ldc`).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L420) — `gdc /tmp/hello.d -o /tmp/hello_d && /tmp/hello_d`

**Example:**
```bash
gdc hello.d -o hello
./hello
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `gdc <file.d>` + `./bin` | Compile + run a `.d` source file | [pytest4_.yml#L420](../.github/workflows/pytest4_.yml#L420) |
