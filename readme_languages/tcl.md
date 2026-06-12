# Tcl Execution Methods in Programming Languages Repository

This document catalogues **all distinct Tcl methods** discovered for running Tcl code throughout the repository. Tcl has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `tcl` apt package (which provides `tclsh`).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [tclsh \<file.tcl\> (Direct Interpreter Invocation)](#11-tclsh-filetcl-direct-interpreter-invocation)

2. **Inline / Stdin**
   - 2.1 [echo '...' \| tclsh (Program from Stdin via Pipe)](#21-echo---tclsh-program-from-stdin-via-pipe)

---

## 1. **Direct File Execution**

### 1.1 tclsh \<file.tcl\> (Direct Interpreter Invocation)
**Method:** Invoke the `tclsh` shell on a `.tcl` source file. Simplest form; the file is interpreted top-to-bottom.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L237) — `tclsh /tmp/hello.tcl`

**Example:**
```bash
tclsh hello.tcl
```

---

## 2. **Inline / Stdin**

### 2.1 echo '...' \| tclsh (Program from Stdin via Pipe)
**Method:** With no positional argument, `tclsh` reads its program from stdin. Pipe a one-liner via `echo` or any command emitting Tcl on stdout.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L248) — `echo 'puts "Hello from tclsh via pipe!"' | tclsh`

**Example:**
```bash
echo 'puts "hi from tcl!"' | tclsh
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `tclsh <file.tcl>` | Run a `.tcl` source file | [pytest3_.yml#L237](../.github/workflows/pytest3_.yml#L237) |
| `echo '...' \| tclsh` | Program from stdin via pipe | [pytest3_.yml#L248](../.github/workflows/pytest3_.yml#L248) |
