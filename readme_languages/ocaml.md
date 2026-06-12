# OCaml Execution Methods in Programming Languages Repository

This document catalogues **all distinct OCaml methods** discovered for running OCaml code throughout the repository. OCaml has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `ocaml` apt package.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [ocaml \<file.ml\> (Direct Interpreter Invocation)](#11-ocaml-fileml-direct-interpreter-invocation)

2. **Inline / Stdin**
   - 2.1 [echo '...' \| ocaml (Program from Stdin via Pipe)](#21-echo---ocaml-program-from-stdin-via-pipe)
   - 2.2 [ocaml \<\<EOF (Heredoc on Stdin)](#22-ocaml-eof-heredoc-on-stdin)

---

## 1. **Direct File Execution**

### 1.1 ocaml \<file.ml\> (Direct Interpreter Invocation)
**Method:** Invoke the `ocaml` toplevel on a `.ml` source file. It runs as an interpreter — top-level `let () = …` blocks execute in order. No separate compile step.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L190) — `ocaml /tmp/hello.ml`

**Example:**
```bash
ocaml hello.ml
```

---

## 2. **Inline / Stdin**

### 2.1 echo '...' \| ocaml (Program from Stdin via Pipe)
**Method:** With no file argument the `ocaml` toplevel reads from stdin. Pipe a one-liner ending in `;;` to evaluate and exit.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L200) — `printf 'print_endline "..."' | ocaml`

**Example:**
```bash
echo 'print_endline "hi from ocaml!";;' | ocaml
```

### 2.2 ocaml \<\<EOF (Heredoc on Stdin)
**Method:** Multi-line variant of §2.1. Use a shell heredoc to feed several `;;`-terminated OCaml phrases into the toplevel.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L204) — `ocaml <<'EOF' … EOF`

**Example:**
```bash
ocaml <<'EOF'
print_endline "hello!";;
let nums = [1; 2; 3; 4; 5] in
Printf.printf "Sum: %d\n" (List.fold_left (+) 0 nums);;
EOF
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `ocaml <file.ml>` | Run an OCaml file via the toplevel | [pytest3_.yml#L190](../.github/workflows/pytest3_.yml#L190) |
| `echo '...' \| ocaml` | One-liner via stdin pipe | [pytest3_.yml#L200](../.github/workflows/pytest3_.yml#L200) |
| `ocaml <<EOF … EOF` | Multi-line heredoc on stdin | [pytest3_.yml#L204](../.github/workflows/pytest3_.yml#L204) |
