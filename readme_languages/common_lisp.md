# Common Lisp (SBCL) Execution Methods in Programming Languages Repository

This document catalogues **all distinct Common Lisp methods** discovered for running Common Lisp code throughout the repository. Common Lisp has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `sbcl` (Steel Bank Common Lisp) apt package.

## Table of Contents

1. **Script Mode**
   - 1.1 [sbcl --script \<file.lisp\> (Lisp Script Mode)](#11-sbcl---script-filelisp-lisp-script-mode)

2. **Inline Expression**
   - 2.1 [sbcl --noinform --non-interactive --eval "..." (Inline Expression)](#21-sbcl---noinform---non-interactive---eval--inline-expression)

---

## 1. **Script Mode**

### 1.1 sbcl --script \<file.lisp\> (Lisp Script Mode)
**Method:** `--script` runs SBCL non-interactively on a `.lisp` source file, loading it as a script (no banner, no REPL). SBCL exits when the file finishes loading.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L256) — `sbcl --script /tmp/hello.lisp`

**Example:**
```bash
sbcl --script hello.lisp
```

---

## 2. **Inline Expression**

### 2.1 sbcl --noinform --non-interactive --eval "..." (Inline Expression)
**Method:** Chain one or more `--eval` flags to evaluate forms on startup; `--non-interactive` exits after the forms run, `--noinform` suppresses the SBCL banner. Multiple `--eval` flags are evaluated in order. End with `--quit` to be explicit.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L264) — `sbcl --noinform --non-interactive --eval '(format t "...")' --eval '(format t "Sum: ~a~%" ...)' --quit`

**Example:**
```bash
sbcl --noinform --non-interactive \
  --eval '(format t "Hello!~%")' \
  --quit
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `sbcl --script <file.lisp>` | Run a `.lisp` source file as a script | [pytest3_.yml#L256](../.github/workflows/pytest3_.yml#L256) |
| `sbcl --noinform --non-interactive --eval "<form>"` | Inline Lisp form(s) | [pytest3_.yml#L264](../.github/workflows/pytest3_.yml#L264) |
