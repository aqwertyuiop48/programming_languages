# Haskell Execution Methods in Programming Languages Repository

This document catalogues **all distinct Haskell methods** discovered for running Haskell code throughout the repository. Haskell has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `ghc` apt package (which also installs `runghc`, `runhaskell`, and `ghci`).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [runghc \<file.hs\> (Interpret + Run in One Step)](#11-runghc-filehs-interpret--run-in-one-step)
   - 1.2 [runhaskell \<file.hs\> (Alias for runghc)](#12-runhaskell-filehs-alias-for-runghc)

2. **Compile + Run (Paired)**
   - 2.1 [ghc \<file.hs\> + ./binary (Compile to Native + Run)](#21-ghc-filehs--binary-compile-to-native--run)

3. **Inline / REPL**
   - 3.1 [ghci \<\<EOF (Haskell REPL via Heredoc)](#31-ghci-eof-haskell-repl-via-heredoc)

---

## 1. **Direct File Execution**

### 1.1 runghc \<file.hs\> (Interpret + Run in One Step)
**Method:** `runghc` compiles a `.hs` source to a temporary object and runs it immediately, in one step. Convenient for scripts; no separate `ghc` invocation needed.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L83) — `runghc /tmp/hello.hs`

**Example:**
```bash
runghc hello.hs
```

### 1.2 runhaskell \<file.hs\> (Alias for runghc)
**Method:** `runhaskell` is a thin wrapper around `runghc` shipped with GHC. Identical behavior; preferred name when scripting Haskell on systems where multiple Haskell implementations might coexist.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L94) — `runhaskell /tmp/hello.hs`

**Example:**
```bash
runhaskell hello.hs
```

---

## 2. **Compile + Run (Paired)**

### 2.1 ghc \<file.hs\> + ./binary (Compile to Native + Run)
**Method:** Run `ghc` to compile a `.hs` source to a native executable, then invoke the resulting binary. Two-step pipeline ideal for production binaries.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L98) — `ghc -o /tmp/hello_hs /tmp/hello.hs && /tmp/hello_hs`

**Example:**
```bash
ghc -o hello hello.hs
./hello
```

---

## 3. **Inline / REPL**

### 3.1 ghci \<\<EOF (Haskell REPL via Heredoc)
**Method:** `ghci` is GHC's interactive REPL. Feeding it a shell heredoc runs multi-line Haskell expressions and `let` bindings. `-v0` suppresses banner output; `:quit` exits the REPL.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L104) — `ghci -v0 <<'EOF' … :quit EOF`

**Example:**
```bash
ghci -v0 <<'EOF'
putStrLn "Hello from ghci!"
let nums = [1,2,3,4,5 :: Int]
putStrLn ("Sum: " ++ show (sum nums))
:quit
EOF
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `runghc <file.hs>` | Single-step interpret + run | [pytest3_.yml#L83](../.github/workflows/pytest3_.yml#L83) |
| `runhaskell <file.hs>` | Same as `runghc` | [pytest3_.yml#L94](../.github/workflows/pytest3_.yml#L94) |
| `ghc <file.hs>` + `./bin` | Compile to native + run | [pytest3_.yml#L98](../.github/workflows/pytest3_.yml#L98) |
| `ghci <<EOF … EOF` | REPL via heredoc | [pytest3_.yml#L104](../.github/workflows/pytest3_.yml#L104) |
