# Prolog (SWI-Prolog) Execution Methods in Programming Languages Repository

This document catalogues **all distinct Prolog methods** discovered for running Prolog code throughout the repository. Prolog has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest4_.yml` using the `swi-prolog` apt package.

## Table of Contents

1. **File + Goal**
   - 1.1 [swipl -g main -t halt \<file.pl\> (Consult + Run Goal)](#11-swipl--g-main--t-halt-filepl-consult--run-goal)

2. **Inline Goal**
   - 2.1 [swipl -g "..." -t halt (Prolog Inline Goal)](#21-swipl--g---t-halt-prolog-inline-goal)

---

## 1. **File + Goal**

### 1.1 swipl -g main -t halt \<file.pl\> (Consult + Run Goal)
**Method:** Consult a `.pl` source file and then call the goal given by `-g` (here `main`). `-t halt` sets the toplevel goal to `halt` so SWI exits after `main` succeeds. `-q` silences the banner.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L496) — `swipl -q -g main -t halt /tmp/hello.pl`

**Example:**
```bash
swipl -q -g main -t halt hello.pl
```

---

## 2. **Inline Goal**

### 2.1 swipl -g "..." -t halt (Prolog Inline Goal)
**Method:** Pass the Prolog goal directly to `-g` instead of putting it in a file. `-t halt` ensures SWI exits after the goal completes.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L509) — `swipl -q -g 'write("Hello from swipl -g inline!"), nl, S is 1+2+3+4+5, format("Sum: ~w~n",[S])' -t halt`

**Example:**
```bash
swipl -q -g 'write("hi from prolog!"), nl' -t halt
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `swipl -g main -t halt <file.pl>` | Consult file + run `main/0` | [pytest4_.yml#L496](../.github/workflows/pytest4_.yml#L496) |
| `swipl -g "<goal>" -t halt` | Inline Prolog goal | [pytest4_.yml#L509](../.github/workflows/pytest4_.yml#L509) |
