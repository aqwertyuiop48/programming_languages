# Erlang Execution Methods in Programming Languages Repository

This document catalogues **all distinct Erlang methods** discovered for running Erlang code throughout the repository. Erlang has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `erlang` apt package (installed transitively with `elixir`).

## Table of Contents

1. **Script Runner**
   - 1.1 [escript \<file\> (Erlang Script Runner, Shebang-Friendly)](#11-escript-file-erlang-script-runner-shebang-friendly)

2. **Inline / One-Liners**
   - 2.1 [erl -noshell -eval "..." (Inline Expression)](#21-erl--noshell--eval--inline-expression)

---

## 1. **Script Runner**

### 1.1 escript \<file\> (Erlang Script Runner, Shebang-Friendly)
**Method:** `escript` runs a single-file Erlang program. The file defines a `main/1` function and may begin with `#!/usr/bin/env escript` to be invoked directly as `./hello.escript`. No compile step required.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L139) — `chmod +x /tmp/hello.escript && escript /tmp/hello.escript`

**Example:**
```erlang
#!/usr/bin/env escript
main(_) ->
    io:format("Hello from escript!~n").
```
```bash
chmod +x hello.escript
escript hello.escript
```

---

## 2. **Inline / One-Liners**

### 2.1 erl -noshell -eval "..." (Inline Expression)
**Method:** `erl -noshell -eval '<expr>.'` evaluates an Erlang expression then exits when followed by `init:stop().`. `-noshell` suppresses the interactive prompt. Erlang analog of `python -c`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L152) — `erl -noshell -eval 'io:format("..."), init:stop().'`

**Example:**
```bash
erl -noshell -eval 'io:format("hi~n"), init:stop().'
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `escript <file>` | Run a single-file Erlang script | [pytest3_.yml#L139](../.github/workflows/pytest3_.yml#L139) |
| `erl -noshell -eval "<expr>."` | Inline Erlang expression | [pytest3_.yml#L152](../.github/workflows/pytest3_.yml#L152) |
