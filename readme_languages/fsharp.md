# F# Execution Methods in Programming Languages Repository

This document catalogues **all distinct F# methods** discovered for running F# code throughout the repository. F# has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the .NET SDK (set up via `actions/setup-dotnet@v3`) which ships `dotnet fsi` (F# Interactive).

## Table of Contents

1. **F# Interactive (fsi) on Script Files**
   - 1.1 [dotnet fsi \<file.fsx\> (F# Interactive on a Script File)](#11-dotnet-fsi-filefsx-f-interactive-on-a-script-file)

2. **F# Interactive from Stdin / Inline**
   - 2.1 [dotnet fsi /dev/stdin \<\<EOF (Heredoc, Materialized to Temp File)](#21-dotnet-fsi-devstdin-eof-heredoc-materialized-to-temp-file)
   - 2.2 [dotnet fsi -e (Inline Expression, Emulated via Temp File)](#22-dotnet-fsi--e-inline-expression-emulated-via-temp-file)

---

## 1. **F# Interactive (fsi) on Script Files**

### 1.1 dotnet fsi \<file.fsx\> (F# Interactive on a Script File)
**Method:** Invoke `dotnet fsi` (F# Interactive) on a `.fsx` script file. The script runs top-to-bottom without a `Main` entry point. Standard way to run an F# script.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L160) — `dotnet fsi /tmp/hello.fsx`

**Example:**
```bash
dotnet fsi hello.fsx
```

---

## 2. **F# Interactive from Stdin / Inline**

### 2.1 dotnet fsi /dev/stdin \<\<EOF (Heredoc, Materialized to Temp File)
**Method:** `dotnet fsi` requires a seekable source, so piping a heredoc to `/dev/stdin` fails with `FS0193 ("Stream does not support seeking")`. The workaround used here writes the heredoc to a temp `.fsx` file first, then runs it.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L169) — heredoc → `/tmp/stdin.fsx` → `dotnet fsi /tmp/stdin.fsx`

**Example:**
```bash
cat > /tmp/inline.fsx <<'EOF'
printfn "Hello from fsi heredoc!"
EOF
dotnet fsi /tmp/inline.fsx
```

### 2.2 dotnet fsi -e (Inline Expression, Emulated via Temp File)
**Method:** F# Interactive does not natively support a `-e`/`-c` style inline flag — the documented workaround is to `echo` an expression into a temp `.fsx` file and run that file. Documented as `dotnet fsi -e` for parity with other languages' inline modes.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L181) — `echo '...' > /tmp/inline.fsx && dotnet fsi /tmp/inline.fsx`

**Example:**
```bash
echo 'printfn "hi!"' > /tmp/inline.fsx
dotnet fsi /tmp/inline.fsx
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `dotnet fsi <file.fsx>` | Run an F# script file | [pytest3_.yml#L160](../.github/workflows/pytest3_.yml#L160) |
| `dotnet fsi <stdin-as-temp.fsx>` | Heredoc-on-stdin (seek workaround) | [pytest3_.yml#L169](../.github/workflows/pytest3_.yml#L169) |
| `echo … > /tmp/inline.fsx; dotnet fsi …` | Inline expression (no native `-e`) | [pytest3_.yml#L181](../.github/workflows/pytest3_.yml#L181) |
