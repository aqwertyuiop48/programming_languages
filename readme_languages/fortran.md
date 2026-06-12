# Fortran Execution Methods in Programming Languages Repository

This document catalogues **all distinct Fortran methods** discovered for running Fortran code throughout the repository. Fortran has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `gfortran` apt package.

## Table of Contents

1. **Compile + Run (File)**
   - 1.1 [gfortran \<file.f90\> + ./binary (Compile + Run)](#11-gfortran-filef90--binary-compile--run)

2. **Compile + Run (Stdin)**
   - 2.1 [gfortran -x f95 - \<\<EOF (Stdin Heredoc Compile + Run)](#21-gfortran--x-f95----eof-stdin-heredoc-compile--run)

---

## 1. **Compile + Run (File)**

### 1.1 gfortran \<file.f90\> + ./binary (Compile + Run)
**Method:** Compile a `.f90` (free-form Fortran) source file with `gfortran -o <binary>`, then execute the binary. Standard two-step build for Fortran.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L292) — `gfortran /tmp/hello.f90 -o /tmp/hello_f90 && /tmp/hello_f90`

**Example:**
```bash
gfortran hello.f90 -o hello
./hello
```

---

## 2. **Compile + Run (Stdin)**

### 2.1 gfortran -x f95 - \<\<EOF (Stdin Heredoc Compile + Run)
**Method:** `-x f95` tells `gfortran` to treat its input as free-form Fortran 95 regardless of extension; `-` reads source from stdin, so a shell heredoc feeds an inline program. Useful for one-shot snippets.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L309) — `gfortran -x f95 -o /tmp/hello_f95_pipe - <<'EOF' … EOF && /tmp/hello_f95_pipe`

**Example:**
```bash
gfortran -x f95 -o hello - <<'EOF'
program hi
    print *, "hi from fortran stdin!"
end program hi
EOF
./hello
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `gfortran <file.f90>` + `./bin` | Compile a Fortran file + run | [pytest3_.yml#L292](../.github/workflows/pytest3_.yml#L292) |
| `gfortran -x f95 - <<EOF` + `./bin` | Compile stdin heredoc + run | [pytest3_.yml#L309](../.github/workflows/pytest3_.yml#L309) |
