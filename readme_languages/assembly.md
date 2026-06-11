# Assembly Execution Methods in Programming Languages Repository

This document catalogues **all distinct x86-64 Assembly methods** discovered for assembling and running Assembly code throughout the repository. Unlike most other languages here, the `assembly_/` tree lives **directly in the main repository** — it is not a git submodule. Assembly sources are written for the NASM assembler and produce ELF64 binaries; some link bare (`ld`), others link against libc (`gcc -no-pie -lc`). A separate workflow also exercises a Docker-orchestrated and a tmpfs-based variant.

## Table of Contents

1. **Assemble and Link**
   - 1.1 [nasm -f elf64 + ld (Bare Syscall Binary)](#11-nasm--f-elf64--ld-bare-syscall-binary)
   - 1.2 [nasm -f elf64 + gcc -no-pie -lc (libc-Linked Binary)](#12-nasm--f-elf64--gcc--no-pie--lc-libc-linked-binary)

2. **Container-Based Workflows**
   - 2.1 [run_asm.sh (Docker-Orchestrated nasm + gcc)](#21-run_asmsh-docker-orchestrated-nasm--gcc)
   - 2.2 [docker run --tmpfs … nasm + ld (Inline-from-Heredoc in tmpfs)](#22-docker-run---tmpfs--nasm--ld-inline-from-heredoc-in-tmpfs)

3. **Polyglot Embedding (Assembly ➜ Other)**
   - 3.1 [sys_execve (59) ➜ /usr/bin/python3 -c "…" (Assembly ➜ Python)](#31-sys_execve-59--usrbinpython3--c--assembly--python)

4. **Alternate Assemblers**
   - 4.1 [GNU as (GAS, AT&T syntax) + ld](#41-gnu-as-gas-att-syntax--ld)
   - 4.2 [yasm -f elf64 + ld (NASM-Compatible)](#42-yasm--f-elf64--ld-nasm-compatible)

---

## 1. **Assemble and Link**

### 1.1 nasm -f elf64 + ld (Bare Syscall Binary)
**Method:** Assemble a `.asm` source into an ELF64 object with `nasm -f elf64`, then link directly with the GNU linker `ld` (no libc, no CRT). Suitable for programs whose `_start` entry point uses raw Linux syscalls (e.g. `sys_write` / `sys_exit`).

**Locations:**
- [assembly_/hello.asm](../assembly_/hello.asm) - "Hello world!" + arithmetic (add/sub/imul/div); uses syscall-based `print_result` subroutine
- [assembly_/python_.asm](../assembly_/python_.asm) - `sys_execve` ➜ Python (see §3.1)

**Workflow yml (executes in CI):**
- [.github/workflows/assembly_.yml](../.github/workflows/assembly_.yml#L17-L18) - `sudo apt-get install -y nasm gcc binutils`
- [.github/workflows/assembly_.yml](../.github/workflows/assembly_.yml#L21-L48) - bash loop over `assembly_/*.asm` running `nasm -f elf64 "$asm_file" -o "$filename.o"` then `ld "$filename.o" -o "$filename"` then `./"$filename"` (and cleanup)

**Example:**
```bash
nasm -f elf64 hello.asm -o hello.o
ld hello.o -o hello
./hello
```

### 1.2 nasm -f elf64 + gcc -no-pie -lc (libc-Linked Binary)
**Method:** Same NASM front-end, but link via `gcc -no-pie -lc` instead of bare `ld`. This pulls in the C startup files and libc so the assembly source can declare `global main` and `extern printf` / `extern malloc` etc. Required when the program relies on libc symbols.

**Locations:**
- [assembly_/assembly_gcc/bubble_sort.asm](../assembly_/assembly_gcc/bubble_sort.asm) - Bubble sort with `global main` and `extern printf` (libc)

**Workflow yml (executes in CI):**
- [.github/workflows/assembly_.yml](../.github/workflows/assembly_.yml#L43-L46) - `cd assembly_gcc && nasm -f elf64 -o bubble_sort.o bubble_sort.asm && gcc -no-pie -o bubble_sort bubble_sort.o -lc && ./bubble_sort`

**Example:**
```bash
nasm -f elf64 -o bubble_sort.o bubble_sort.asm
gcc -no-pie -o bubble_sort bubble_sort.o -lc
./bubble_sort
```

---

## 2. **Container-Based Workflows**

### 2.1 run_asm.sh (Docker-Orchestrated nasm + gcc)
**Method:** A shell wrapper that embeds an assembly source as a heredoc inside the script, then drives `docker build` against [assembly_/Dockerfile](../assembly_/Dockerfile) (`FROM ubuntu:latest` + `apt-get install -y nasm gcc`), writes the heredoc'd `.asm` inside the container, and runs `nasm -f elf64 … && gcc -nostartfiles -no-pie -o … && ./hello`. Useful as a self-contained reproducer that needs no host-side toolchain.

**Locations:**
- [assembly_/run_asm.sh](../assembly_/run_asm.sh) - heredoc'd "Hello, Docker World!" assembly + docker build/exec sequence
- [assembly_/Dockerfile](../assembly_/Dockerfile) - `FROM ubuntu:latest`, `apt-get install -y nasm gcc`, `WORKDIR /app`

**Workflow yml (executes in CI):**
- [.github/workflows/assembly_.yml](../.github/workflows/assembly_.yml#L56-L62) - `cd assembly_ && chmod +x run_asm.sh && ./run_asm.sh`

**Example:**
```bash
cd assembly_
chmod +x run_asm.sh
./run_asm.sh
```

### 2.2 docker run --tmpfs … nasm + ld (Inline-from-Heredoc in tmpfs)
**Method:** Spawn a `debian:latest` container with a `--tmpfs /tmpfs:rw,exec,size=64m` mount, install `nasm binutils` inside, write a heredoc'd assembly source to `/tmp/my_tmpfs/example.asm`, then assemble (`nasm -f elf64`) + link (`ld`) + execute — all inside the in-memory tmpfs. Exercises the same toolchain as §1.1 but inside an ephemeral container with no on-disk artifacts.

**Locations:**
None tracked as a `.asm` file — the source is embedded as a heredoc inside the workflow itself.

**Workflow yml (executes in CI):**
- [.github/workflows/shell_tmpfs.yml](../.github/workflows/shell_tmpfs.yml#L18-L25) - `docker build -t my-asm-app .` (with inline Dockerfile installing `nasm binutils`)
- [.github/workflows/shell_tmpfs.yml](../.github/workflows/shell_tmpfs.yml#L27-L54) - `docker run --rm --tmpfs /tmpfs:rw,exec,size=64m debian:latest bash -c "…"` containing the heredoc'd `.asm`, `nasm -f elf64 …`, `ld …`, `./example`

**Example:**
```bash
docker run --rm --tmpfs /tmpfs:rw,exec,size=64m debian:latest bash -c "
  apt-get update && apt-get install -y nasm binutils &&
  mkdir -p /tmp/my_tmpfs &&
  echo '<asm source here>' > /tmp/my_tmpfs/example.asm &&
  nasm -f elf64 /tmp/my_tmpfs/example.asm -o /tmp/my_tmpfs/example.o &&
  ld /tmp/my_tmpfs/example.o -o /tmp/my_tmpfs/example &&
  /tmp/my_tmpfs/example
"
```

---

## 3. **Polyglot Embedding (Assembly ➜ Other)**

### 3.1 sys_execve (59) ➜ /usr/bin/python3 -c "…" (Assembly ➜ Python)
**Method:** From inside `_start`, set up the registers for the Linux `sys_execve` syscall (rax = 59) with `rdi` pointing at `"/usr/bin/python3"`, `rsi` pointing at `argv[]` containing `"-c"` and an inline Python script, and `rdx` pointing at `envp[] = NULL`. The kernel replaces the current process image with the Python interpreter, which then executes the inline Python source. Canonical Assembly → Python polyglot via direct syscall (no libc).

**Locations:**
- [assembly_/python_.asm](../assembly_/python_.asm) - `python_command db '/usr/bin/python3', 0` + `python_script db 'import sys; print(...)'` + `mov rax, 59; syscall`

**Workflow yml (executes in CI):**
- [.github/workflows/assembly_.yml](../.github/workflows/assembly_.yml#L21-L48) - the bash loop assembles + links + runs every `*.asm` under `assembly_/`, including `python_.asm`. When that binary runs, the `execve` syscall fires and Python takes over the process.

**Example:**
```nasm
section .data
    python_command db '/usr/bin/python3', 0
    arg1           db '-c', 0
    python_script  db 'print("Hello from Python")', 0

section .text
    global _start
_start:
    mov rax, 59                  ; sys_execve
    lea rdi, [rel python_command]
    lea rsi, [rel arg1]
    lea rdx, [rel python_script]
    syscall
```

---

## 4. **Alternate Assemblers**

### 4.1 GNU as (GAS, AT&T syntax) + ld
**Method:** `as` (the GNU assembler from `binutils`) uses AT&T syntax (`movq $1, %rax`) by default — syntactically incompatible with NASM/yasm's Intel syntax. Same ELF64 output, same `ld` link step, same syscall ABI; only the source-language conventions differ.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `as /tmp/hello_gas.s -o /tmp/hello_gas.o && ld /tmp/hello_gas.o -o /tmp/hello_gas && /tmp/hello_gas`

**Example:**
```gas
        .data
msg:    .ascii "Hello from GAS!\n"
len = . - msg
        .text
        .globl _start
_start:
        movq $1, %rax           # sys_write
        movq $1, %rdi
        leaq msg(%rip), %rsi
        movq $len, %rdx
        syscall
        movq $60, %rax          # sys_exit
        xorq %rdi, %rdi
        syscall
```
```bash
as hello_gas.s -o hello_gas.o
ld hello_gas.o -o hello_gas
./hello_gas
```

### 4.2 yasm -f elf64 + ld (NASM-Compatible)
**Method:** `yasm` is a complete rewrite of NASM that accepts the same Intel-syntax source files (`nasm`-compatible) and emits the same ELF64 object format. Drop-in alternative to `nasm` (§1.1) when `yasm` is preferred for license or behavior reasons.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `yasm -f elf64 /tmp/hello_yasm.asm -o /tmp/hello_yasm.o && ld /tmp/hello_yasm.o -o /tmp/hello_yasm && /tmp/hello_yasm`

**Example:**
```bash
yasm -f elf64 hello.asm -o hello.o
ld hello.o -o hello
./hello
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `nasm -f elf64 + ld` | Bare syscall ELF64 binary | [hello.asm](../assembly_/hello.asm) |
| `nasm -f elf64 + gcc -no-pie -lc` | libc-linked ELF64 binary | [bubble_sort.asm](../assembly_/assembly_gcc/bubble_sort.asm) |
| `run_asm.sh` (Docker) | Self-contained Dockerised build/run | [run_asm.sh](../assembly_/run_asm.sh) |
| `docker run --tmpfs … nasm + ld` | Ephemeral tmpfs build/run | [shell_tmpfs.yml](../.github/workflows/shell_tmpfs.yml) |
| `sys_execve` → `python3 -c` | Assembly → Python polyglot | [python_.asm](../assembly_/python_.asm) |
| `as` (GAS, AT&T) + `ld` | Alternate assembler (GNU `as`) | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `yasm -f elf64` + `ld` | Alternate assembler (NASM-compatible) | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
