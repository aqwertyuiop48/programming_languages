# Ada Execution Methods in Programming Languages Repository

This document catalogues **all distinct Ada methods** discovered for running Ada code throughout the repository. Ada has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `gnat` apt package (GNU Ada compiler suite).

## Table of Contents

1. **Compile + Run**
   - 1.1 [gnatmake \<file.adb\> + ./binary (Compile + Run)](#11-gnatmake-fileadb--binary-compile--run)

---

## 1. **Compile + Run**

### 1.1 gnatmake \<file.adb\> + ./binary (Compile + Run)
**Method:** `gnatmake` is GNAT's all-in-one driver — it compiles the named `.adb` (Ada body) source plus any dependencies and links the result into a native executable named after the procedure. The binary is then run directly.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L323) — `gnatmake hello.adb && ./hello`

**Example:**
```bash
gnatmake hello.adb
./hello
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `gnatmake <file.adb>` + `./bin` | Compile + link + run Ada source | [pytest3_.yml#L323](../.github/workflows/pytest3_.yml#L323) |
