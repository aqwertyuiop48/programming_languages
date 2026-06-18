# Matlab Execution Methods in Programming Languages Repository

This document catalogues **all distinct Matlab methods** discovered for running Matlab code throughout the repository. The CI uses the official `matlab-actions/setup-matlab@v1` action and the `matlab -batch` headless front-end (suitable for non-interactive runs).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [matlab -batch "run('\<file.m\>')" (Batch File Execution)](#11-matlab--batch-runfilem-batch-file-execution)

2. **Inline / One-Liners**
   - 2.1 [matlab -batch "\<inline code\>" (Inline Expression)](#21-matlab--batch-inline-code-inline-expression)

3. **Polyglot Embedding (Matlab ➜ Other)**
   - 3.1 [system('python3 -c …') / system('shell cmd') (Shell Bridge)](#31-systempython3--c--systemshell-cmd-shell-bridge)

4. **Alternate Runtime: GNU Octave (FOSS, Matlab-Compatible)**
   - 4.1 [octave --eval "..." (Inline Expression)](#41-octave---eval--inline-expression)
   - 4.2 [octave \<file.m\> (Run a .m File Under Octave)](#42-octave-filem-run-a-m-file-under-octave)

---

## 1. **Direct File Execution**

### 1.1 matlab -batch "run('\<file.m\>')" (Batch File Execution)
**Method:** `matlab -batch <stmt>` is the headless / non-interactive launcher. Wrapping `run('<file.m>')` inside it tells Matlab to execute the named script file and exit. The CI uses a Bash wrapper that loops over every `.m` under `files/`.

**Locations:**
- [matlab__/codeforces_script/files/hello_world.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/hello_world.m) - `disp('Hello, Matlab World!')`
- [matlab__/codeforces_script/files/data_analysis.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/data_analysis.m) - Statistical analysis demo
- [matlab__/codeforces_script/files/plot_sine_wave.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/plot_sine_wave.m) - Plot with `saveas()` → `output/`
- [matlab__/codeforces_script/files/polynomial_fit.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/polynomial_fit.m) - Polynomial fit demo
- [matlab__/codeforces_script/files/solve_linear_system.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/solve_linear_system.m) - Linear system solver
- [matlab__/codeforces_script/files/pendulum_simulation.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/pendulum_simulation.m) - Physics simulation
- [matlab__/codeforces_script/files/shell_script.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/shell_script.m) - Shell bridge via `system(...)` (see §3.1)
- [matlab__/codeforces_script/run_all_matlab_files.sh](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/run_all_matlab_files.sh#L11) - Bash wrapper: `for matlab_file in files/*.m; do matlab -batch "run('$matlab_file')"; done`
  - Remote (submodule `matlab__/codeforces_script` @ branch `matlab_`): [run_all_matlab_files.sh](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/run_all_matlab_files.sh)

**Workflow yml (executes in CI):**
- [matlab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L20-L23) - sets up Matlab R2023b via `matlab-actions/setup-matlab@v1`
- [matlab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L30-L36) - `chmod +x ./run_all_matlab_files.sh && ./run_all_matlab_files.sh` (iterates every `.m` file)
- [matlab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L40-L45) - uploads the produced plots in `output/` as an artifact
  - Remote: [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml)

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L132) — submodule sync that triggers the `matlab_` branch run

**Example:**
```bash
matlab -batch "run('files/hello_world.m')"
```

---

## 2. **Inline / One-Liners**

### 2.1 matlab -batch "\<inline code\>" (Inline Expression)
**Method:** When the quoted batch statement is plain Matlab source (not `run('...')`), it executes inline — no file needed. Matlab analog of `python -c` / `node -e`.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [matlab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L37) - `matlab -batch "disp('Running MATLAB code inline!'); a = 3; b = 5; disp(['Sum: ', num2str(a+b)])"`
  - Remote: [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml)

**Example:**
```bash
matlab -batch "disp('Running MATLAB code inline!'); a = 3; b = 5; disp(['Sum: ', num2str(a+b)])"
```

---

## 3. **Polyglot Embedding (Matlab ➜ Other)**

### 3.1 system('python3 -c …') / system('shell cmd') (Shell Bridge)
**Method:** Matlab's built-in `system(cmd)` function shells out to the OS. Combined with the batch driver, it lets Matlab orchestrate arbitrary shell commands — including spawning Python (`python3 -c '…'`). Canonical Matlab → external-tool polyglot pattern. The CI installs Python 3.8 alongside Matlab specifically so this bridge works.

**Locations:**
- [matlab__/codeforces_script/files/shell_script.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/shell_script.m#L2-L7) - multi-line `shell_command` joining `echo`, `pwd`, `ls`, `python3 -c "print(222222)"`
- [matlab__/codeforces_script/files/shell_script.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/shell_script.m#L10) - `[status, cmdout] = system(shell_command)` (executes it)

**Workflow yml (executes in CI):**
- [matlab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L25-L28) - sets up Python 3.8 (needed by `shell_script.m`'s `python3` bridge)
- [matlab__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L30-L36) - the `run_all_matlab_files.sh` loop picks up `shell_script.m` and runs it, exercising the `system(...)` bridge

**Example:**
```matlab
shell_command = ['echo "Hello from shell!"; ' ...
                 'pwd; ' ...
                 'python3 -c "print(222222)"'];
[status, cmdout] = system(shell_command);
disp(cmdout);
```

---

## 4. **Alternate Runtime: GNU Octave (FOSS, Matlab-Compatible)**

GNU Octave is a free, open-source language that is largely syntactically compatible with Matlab. The same `.m` sources frequently run under both. Octave installs from `apt` (`sudo apt-get install -y octave`) with no licensing requirement, making it a practical alternative for CI environments that cannot use the official `matlab-actions/setup-matlab@v1` action.

### 4.1 octave --eval "..." (Inline Expression)
**Method:** Octave's `--eval` flag runs the quoted Matlab/Octave source and exits. Direct counterpart to `matlab -batch "<code>"` (§2.1).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `octave --no-gui --eval 'disp("Hello from octave --eval!"); a = 3; b = 5; disp(["Sum: ", num2str(a+b)])'`

**Example:**
```bash
octave --no-gui --eval 'disp("Hello from octave!"); a = 3; b = 5; disp(["Sum: ", num2str(a+b)])'
```

### 4.2 octave \<file.m\> (Run a .m File Under Octave)
**Method:** Pass a `.m` file as the positional argument; Octave executes it as a script and exits. `-q` suppresses the startup banner. Drop-in replacement for `matlab -batch "run('<file.m>')"` for the majority of Matlab idioms.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `octave --no-gui -q /tmp/hello_oct.m`

**Example:**
```bash
octave --no-gui -q hello.m
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `matlab -batch "run('<file.m>')"` | Headless run of a `.m` script | [hello_world.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/hello_world.m) |
| `matlab -batch "<inline code>"` | Inline Matlab expression | [main.yml#L37](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/.github/workflows/main.yml#L37) |
| `system('<shell cmd>')` inside `.m` | Matlab → shell / Python bridge | [shell_script.m](https://github.com/aqwertyuiop48/codeforces_script/blob/matlab_/files/shell_script.m) |
| `octave --eval "<code>"` | FOSS inline expression | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `octave <file.m>` | FOSS run a `.m` file | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
