# Pascal Execution Methods in Programming Languages Repository

This document catalogues **all distinct Pascal methods** discovered for compiling and running Pascal code throughout the repository. Pascal is a compiled language; the canonical workflow is `fpc <file.pas>` (Free Pascal Compiler) followed by executing the produced native binary.

## Table of Contents

1. **Compile and Run**
   - 1.1 [fpc \<file.pas\> + ./binary (Two-Step Compile-and-Execute)](#11-fpc-filepas--binary-two-step-compile-and-execute)

2. **Script-Mode Execution**
   - 2.1 [instantfpc \<file.pas\> (Script-Mode Runner)](#21-instantfpc-filepas-script-mode-runner)

---

## 1. **Compile and Run**

### 1.1 fpc \<file.pas\> + ./binary (Two-Step Compile-and-Execute)
**Method:** Invoke the Free Pascal Compiler (`fpc`) on a `.pas` source file. It produces a native ELF executable named after the source file (no extension). Then execute the binary directly.

**Locations:**
- [pascal/codeforces_script/hello.pas](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/hello.pas) - `program Hello; begin writeln('Hello, Pascal World!') end.`
  - Remote (submodule `pascal/codeforces_script` @ branch `pascal_`): [pascal/codeforces_script/hello.pas](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/hello.pas)

**Workflow yml (executes in CI):**
- [pascal/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/.github/workflows/main.yml#L22-L23) - `sudo apt-get install -y fpc` (install Free Pascal Compiler)
- [pascal/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/.github/workflows/main.yml#L26-L27) - `fpc hello.pas` (compile)
- [pascal/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/.github/workflows/main.yml#L30-L31) - `./hello` (run the produced binary)
  - Remote: [main.yml#L26-L31](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/.github/workflows/main.yml#L26-L31)

Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside the submodule that is
synced and built by:

- [.github/workflows/main.yml](../.github/workflows/main.yml#L138) — submodule sync that triggers the `pascal_` branch run

**Example:**
```bash
fpc hello.pas
./hello
```

---

## 2. **Script-Mode Execution**

### 2.1 instantfpc \<file.pas\> (Script-Mode Runner)
**Method:** `instantfpc` is a Free Pascal helper that combines compile + execute into a single command. It caches the produced binary under `~/.cache/instantfpc/` and re-uses it on subsequent runs. With a `#!/usr/bin/env instantfpc` shebang, a `.pas` file becomes directly executable like any shell script — no manual `fpc` step needed.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `instantfpc /tmp/hello_inst.pas` (best-effort — step prints a skip message if `instantfpc` is not packaged with the runner's `fpc` release)

**Example:**
```pascal
#!/usr/bin/env instantfpc
program Hello;
begin
  writeln('Hello from instantfpc!');
end.
```
```bash
instantfpc hello.pas
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `fpc <file.pas>` + `./<binary>` | Compile with Free Pascal then execute | [hello.pas](https://github.com/aqwertyuiop48/codeforces_script/blob/pascal_/hello.pas) |
| `instantfpc <file.pas>` | Script-mode compile+run, cached binary | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
