# Bash Execution Methods in Programming Languages Repository

This document catalogues **all distinct Bash methods** discovered for running Bash *as a language* (not just a shell driver for other languages) throughout the repository. The methods documented here are exercised inline from `.github/workflows/pytest4_.yml`; `bash` is preinstalled on every GitHub-hosted Ubuntu runner so no `apt` step is required.

> **Note:** A standalone `bash/` directory exists in the repository (with `readme.md` only), but the four execution methods documented below are exercised by `pytest4_.yml` rather than by any dedicated bash workflow.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [bash \<file.sh\> (Script File Execution)](#11-bash-filesh-script-file-execution)

2. **Inline / Stdin**
   - 2.1 [bash -c "..." (Inline Command String)](#21-bash--c--inline-command-string)
   - 2.2 [bash \<\<EOF (Heredoc on Stdin)](#22-bash-eof-heredoc-on-stdin)
   - 2.3 [echo '...' \| bash (Program from Stdin via Pipe)](#23-echo---bash-program-from-stdin-via-pipe)

---

## 1. **Direct File Execution**

### 1.1 bash \<file.sh\> (Script File Execution)
**Method:** Invoke `bash` on a `.sh` script file. Works regardless of shebang or executable bit (those matter only when invoking the file directly as `./script.sh`).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L619) — `bash /tmp/hello.sh`

**Example:**
```bash
bash hello.sh
```

---

## 2. **Inline / Stdin**

### 2.1 bash -c "..." (Inline Command String)
**Method:** `-c` runs the quoted argument as a bash command string and exits. Bash analog of `python -c` / `node -e`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L631) — `bash -c 'echo "Hello from bash -c!"; s=0; for i in 1 2 3 4 5; do (( s += i )); done; echo "Sum: $s"'`

**Example:**
```bash
bash -c 'echo hi from bash -c'
```

### 2.2 bash \<\<EOF (Heredoc on Stdin)
**Method:** With no arguments, `bash` reads commands from stdin. Feed it a shell heredoc to embed a multi-line bash program inside another shell script.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L635) — `bash <<'EOF' … EOF`

**Example:**
```bash
bash <<'EOF'
echo "hi from bash heredoc!"
nums=(1 2 3 4 5)
sum=0
for n in "${nums[@]}"; do (( sum += n )); done
echo "Sum: $sum"
EOF
```

### 2.3 echo '...' \| bash (Program from Stdin via Pipe)
**Method:** Pipe a one-line bash program into `bash` via stdin. Smaller form of §2.2.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest4_.yml](../.github/workflows/pytest4_.yml#L645) — `echo 'echo "Hello from bash via pipe! $BASH_VERSION"' | bash`

**Example:**
```bash
echo 'echo hi from pipe' | bash
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `bash <file.sh>` | Run a `.sh` script file | [pytest4_.yml#L619](../.github/workflows/pytest4_.yml#L619) |
| `bash -c "<cmd>"` | Inline command string | [pytest4_.yml#L631](../.github/workflows/pytest4_.yml#L631) |
| `bash <<EOF … EOF` | Multi-line heredoc on stdin | [pytest4_.yml#L635](../.github/workflows/pytest4_.yml#L635) |
| `echo '...' \| bash` | Program from stdin via pipe | [pytest4_.yml#L645](../.github/workflows/pytest4_.yml#L645) |
