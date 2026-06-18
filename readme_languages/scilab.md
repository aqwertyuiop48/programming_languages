# Scilab Execution Methods in Programming Languages Repository

This document catalogues **all distinct Scilab methods** discovered for running Scilab code throughout the repository. Scilab is a numerical-computation language similar to MATLAB; the headless `scilab-cli` binary is used in CI. Each flag combination is documented separately because flags change execution semantics (file vs. inline vs. shell-bridge).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [scilab-cli -nb -f \<file.sci\> (Batch File Execution)](#11-scilab-cli--nb--f-filesci-batch-file-execution)

2. **Inline / One-Liners**
   - 2.1 [scilab-cli -nb -e "..." (Inline Expression)](#21-scilab-cli--nb--e--inline-expression)

3. **Polyglot Embedding (Scilab ➜ Other)**
   - 3.1 [unix('python3 -c …') / unix('shell cmd') (Shell Bridge)](#31-unixpython3--c--unixshell-cmd-shell-bridge)

4. **Other Front-Ends (Documented Only — Not Yet in CI)**
   - 4.1 [echo '…' | scilab-cli -nb (Program from stdin)](#41-echo---scilab-cli--nb-program-from-stdin)

---

## 1. **Direct File Execution**

### 1.1 scilab-cli -nb -f \<file.sci\> (Batch File Execution)
**Method:** `scilab-cli` is the no-display CLI front-end; `-nb` suppresses the banner; `-f <file>` runs the named `.sci` script and exits. Equivalent to "headless batch mode" for Scilab.

**Locations:**
- [scilab__/codeforces_script/files/hello.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/hello.sci) - Factorial computation
- [scilab__/codeforces_script/files/shell_.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/shell_.sci) - Shell-bridge via `unix(...)` (see §3.1)
- [scilab__/codeforces_script/run_all_scilab_files.sh](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/run_all_scilab_files.sh#L11) - Bash wrapper that loops `for scilab_file in files/*.sci` invoking `scilab-cli -nb -f "$scilab_file"`
  - Remote (submodule `scilab__/codeforces_script` @ branch `scilab_`): [run_all_scilab_files.sh](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/run_all_scilab_files.sh)

**Workflow yml (executes in CI):**
- [scilab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml#L23-L27) - downloads Scilab 6.1.1, symlinks `/usr/local/bin/scilab-cli`
- [scilab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml#L53-L58) - `chmod +x ./run_all_scilab_files.sh && ./run_all_scilab_files.sh` (iterates all `.sci` files)
  - Remote: [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml)

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L133) — submodule sync that triggers the `scilab_` branch run

**Example:**
```bash
scilab-cli -nb -f files/hello.sci
```

---

## 2. **Inline / One-Liners**

### 2.1 scilab-cli -nb -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted string as Scilab source. Scilab analog of `python -c` / `node -e`. No file required.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [scilab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml#L60-L61) - `scilab-cli -nb -e "disp('Hello from inline Scilab!');"`
  - Remote: [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml)

**Example:**
```bash
scilab-cli -nb -e "disp('Hello from inline Scilab!');"
```

---

## 3. **Polyglot Embedding (Scilab ➜ Other)**

### 3.1 unix('python3 -c …') / unix('shell cmd') (Shell Bridge)
**Method:** Scilab's built-in `unix(cmd)` function shells out to `/bin/sh -c cmd`. Used inside a `.sci` script, it lets Scilab orchestrate arbitrary shell commands — including spawning Python (`python3 -c '…'`) and bridging the result back. This is the canonical Scilab → external-tool polyglot pattern.

**Locations:**
- [scilab__/codeforces_script/files/shell_.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/shell_.sci#L4) - `unix('echo ''Hello from shell Scilab!''')`
- [scilab__/codeforces_script/files/shell_.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/shell_.sci#L8) - `unix('ls -l')`
- [scilab__/codeforces_script/files/shell_.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/shell_.sci#L10) - `unix('pwd')`
- [scilab__/codeforces_script/files/shell_.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/shell_.sci#L12) - `unix('python3 -c ''print(2222222)''')` (Scilab → Python)

**Workflow yml (executes in CI):**
- [scilab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml#L53-L58) - the same `run_all_scilab_files.sh` loop picks up `shell_.sci`, so every `unix(...)` line runs end-to-end

**Example:**
```scilab
// inside a .sci file
unix('echo ''Hello from shell Scilab!''')
unix('python3 -c ''print(2222222)''')
```

---

## 4. **Other Front-Ends (Documented Only — Not Yet in CI)**

### 4.1 echo '…' | scilab-cli -nb (Program from stdin)
**Method:** `scilab-cli -nb` will read its program from stdin when no `-f` / `-e` argument is given. Allows piping a Scilab program in from another shell command (`cat plan.sci | scilab-cli -nb`).

**Workflow yml (executes in CI):** None. Installing Scilab in CI is a ~700 MB download for what is effectively a thin variation of §1.1 / §2.1, so [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) deliberately skips Scilab; this method is included here for completeness.

**Example:**
```bash
echo "disp('Hello from scilab via pipe!'); quit;" | scilab-cli -nb
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `scilab-cli -nb -f <file.sci>` | Run a Scilab script in batch mode | [hello.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/hello.sci) |
| `scilab-cli -nb -e "<sci expr>"` | Inline Scilab expression | [main.yml#L60](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/.github/workflows/main.yml#L60) |
| `unix('<shell cmd>')` inside `.sci` | Scilab → shell / Python bridge | [shell_.sci](https://github.com/aqwertyuiop48/codeforces_script/blob/scilab_/files/shell_.sci) |
| `echo '…' \| scilab-cli -nb` | Program from stdin (documented only) | _no CI workflow_ |
