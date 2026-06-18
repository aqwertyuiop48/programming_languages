# R Execution Methods in Programming Languages Repository

This document catalogues **all distinct R-language methods** discovered for running R code throughout the repository. R is interpreted via `Rscript`; each command-line flag combination is documented separately because the flags meaningfully change execution semantics (file vs. inline expression vs. shell-bridge).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [Rscript \<file.R\> (Direct Interpreter Invocation)](#11-rscript-filer-direct-interpreter-invocation)

2. **Inline / One-Liners**
   - 2.1 [Rscript -e "..." (Inline Expression)](#21-rscript--e--inline-expression)
   - 2.2 [Rscript -e "system(...)" (Inline → Shell Bridge)](#22-rscript--e-system--inline--shell-bridge)
   - 2.3 [R -e "..." (Interactive Front-End Inline)](#23-r--e--interactive-front-end-inline)
   - 2.4 [echo '...' | Rscript - (Program from stdin)](#24-echo---rscript----program-from-stdin)
   - 2.5 [R CMD BATCH \<file.R\> (Legacy Batch Mode)](#25-r-cmd-batch-filer-legacy-batch-mode)

---

## 1. **Direct File Execution**

### 1.1 Rscript \<file.R\> (Direct Interpreter Invocation)
**Method:** Invoke the `Rscript` front-end on a `.R` source file. `Rscript` is the headless / non-interactive launcher (as opposed to `R` which drops into the REPL). Standard way to run R scripts in CI.

**Locations:**
- [R__/codeforces_script/hello_world.R](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/hello_world.R) - `print(paste("Hello, R World!", ":", "2"))`
  - Remote (submodule `R__/codeforces_script` @ branch `R_`): [R__/codeforces_script/hello_world.R](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/hello_world.R)

**Workflow yml (executes in CI):**
- [R__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L30) - `Rscript hello_world.R` (after `r-lib/actions/setup-r@v2` with R 4.3.0)
  - Remote: [main.yml#L30](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L30)

Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules that are built,
tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml#L143) — submodule sync that triggers the `R_` branch run

**Example:**
```bash
Rscript hello_world.R
```

---

## 2. **Inline / One-Liners**

### 2.1 Rscript -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted string as R source. Direct R analog of `python -c` / `node -e`. No file required.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [R__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L31) - `Rscript -e "print('Hello, R embedded world!')"`
  - Remote: [main.yml#L31](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L31)

**Example:**
```bash
Rscript -e 'print("Hello, R embedded world!")'
```

### 2.2 Rscript -e "system(...)" (Inline → Shell Bridge)
**Method:** R's built-in `system()` function shells out to an external command. Combined with `-e`, this is the polyglot R-driven shell-call form (R → bash → arbitrary tool).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [R__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L32) - `Rscript -e "system(\"echo 'Hello from the R shell!'\")"`
  - Remote: [main.yml#L32](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L32)

**Example:**
```bash
Rscript -e 'system("echo Hello from the R shell!")'
```

### 2.3 R -e "..." (Interactive Front-End Inline)
**Method:** `R` (the interactive front-end, not `Rscript`) also accepts `-e`. Difference vs. §2.1: `R -e` prints the R startup banner and the expression value alongside its side effects; `Rscript -e` prints only the expression's output. Useful when you want full session metadata in logs.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `R -e 'print("Hello from R -e!")'`

**Example:**
```bash
R -e 'print("Hello from R -e!")'
```

### 2.4 echo '...' | Rscript - (Program from stdin)
**Method:** Passing `-` as the script argument tells `Rscript` to read its program from stdin. Lets you feed R code via a Unix pipe — useful for shell composition (`make | Rscript -`).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `echo 'print("Hello from Rscript via stdin!")' | Rscript -`

**Example:**
```bash
echo 'print("Hello from Rscript via stdin!")' | Rscript -
```

### 2.5 R CMD BATCH \<file.R\> (Legacy Batch Mode)
**Method:** The historical non-interactive runner. `R CMD BATCH input.R output.Rout` runs the script, capturing all output (including echoed commands) into a `.Rout` file. Predates `Rscript`; still supported and occasionally required by older R toolchains.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `R CMD BATCH --no-save /tmp/hello_batch.R /tmp/hello_batch.Rout`

**Example:**
```bash
R CMD BATCH --no-save script.R script.Rout
cat script.Rout
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `Rscript <file.R>` | Run an R script file | [hello_world.R](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/hello_world.R) |
| `Rscript -e "<R expr>"` | Inline R expression | [main.yml#L31](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L31) |
| `Rscript -e "system(...)"` | Inline R → shell bridge | [main.yml#L32](https://github.com/aqwertyuiop48/codeforces_script/blob/R_/.github/workflows/main.yml#L32) |
| `R -e "<R expr>"` | Interactive front-end inline | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `echo '...' \| Rscript -` | Program from stdin | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `R CMD BATCH <file.R> <out.Rout>` | Legacy batch mode | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
