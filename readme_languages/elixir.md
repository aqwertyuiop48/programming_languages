# Elixir Execution Methods in Programming Languages Repository

This document catalogues **all distinct Elixir methods** discovered for running Elixir code throughout the repository. Elixir has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `elixir` apt package (which also pulls in `erlang`).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [elixir \<file.exs\> (Script File Execution)](#11-elixir-fileexs-script-file-execution)

2. **Inline / REPL**
   - 2.1 [elixir -e "..." (Inline Expression)](#21-elixir--e--inline-expression)
   - 2.2 [iex -e "..." (Interactive Shell, One-Shot Expression)](#22-iex--e--interactive-shell-one-shot-expression)

---

## 1. **Direct File Execution**

### 1.1 elixir \<file.exs\> (Script File Execution)
**Method:** Invoke the `elixir` CLI on a `.exs` (Elixir script) file. The `.exs` extension marks a script intended to be executed directly rather than compiled to a `.beam` file.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L117) — `elixir /tmp/hello.exs`

**Example:**
```bash
elixir hello.exs
```

---

## 2. **Inline / REPL**

### 2.1 elixir -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted argument as Elixir source and exits. Elixir analog of `python -c` / `node -e`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L127) — `elixir -e 'IO.puts "Hello from elixir -e!"; IO.puts "Sum: #{Enum.sum([1,2,3,4,5])}"'`

**Example:**
```bash
elixir -e 'IO.puts "hello"'
```

### 2.2 iex -e "..." (Interactive Shell, One-Shot Expression)
**Method:** `iex` is Elixir's interactive shell. With `-e` it evaluates an expression on startup. Piping empty stdin (`echo '' | iex -e ...`) ensures `iex` exits after the expression runs instead of waiting for input.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L131) — `echo '' | iex -e 'IO.puts("Hello from iex -e!"); IO.puts("Sum: #{Enum.sum([1,2,3,4,5])}")'`

**Example:**
```bash
echo '' | iex -e 'IO.puts("hi from iex")'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `elixir <file.exs>` | Run an Elixir script file | [pytest3_.yml#L117](../.github/workflows/pytest3_.yml#L117) |
| `elixir -e "<expr>"` | Inline Elixir expression | [pytest3_.yml#L127](../.github/workflows/pytest3_.yml#L127) |
| `echo '' \| iex -e "<expr>"` | One-shot eval in IEx shell | [pytest3_.yml#L131](../.github/workflows/pytest3_.yml#L131) |
