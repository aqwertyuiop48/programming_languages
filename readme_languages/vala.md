# Vala Execution Methods in Programming Languages Repository

This document catalogues **all distinct Vala methods** discovered for running Vala code throughout the repository. Vala has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `valac` apt package (Vala compiler — translates Vala to C, then to a native binary via the system C compiler).

## Table of Contents

1. **Compile + Run**
   - 1.1 [valac \<file.vala\> + ./binary (Compile + Run)](#11-valac-filevala--binary-compile--run)

---

## 1. **Compile + Run**

### 1.1 valac \<file.vala\> + ./binary (Compile + Run)
**Method:** `valac` compiles a `.vala` source file to C, then invokes the system C compiler to produce a native binary. Use `-o <path>` to control the output location, then run the resulting binary directly.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L556) — `valac -o /tmp/hello_vala /tmp/hello.vala && /tmp/hello_vala`

**Example:**
```bash
valac -o hello hello.vala
./hello
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `valac <file.vala>` + `./bin` | Compile (Vala → C → native) + run | [pytest4_.yml#L556](../.github/workflows/pytest4_.yml#L556) |
