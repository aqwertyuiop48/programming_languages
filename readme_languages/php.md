# PHP Execution Methods in Programming Languages Repository

This document catalogues **all distinct PHP methods** discovered for running PHP code throughout the repository. PHP has no dedicated source folder in the workspace — every method here is exercised inline from `.github/workflows/pytest3_.yml` using the `php-cli` apt package installed in CI.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [php \<file.php\> (Direct Interpreter Invocation)](#11-php-filephp-direct-interpreter-invocation)

2. **Inline / One-Liners**
   - 2.1 [php -r "..." (Inline Expression, No `<?php` Tags)](#21-php--r--inline-expression-no-php-tags)
   - 2.2 [php -a \<\<EOF (Interactive REPL via Heredoc)](#22-php--a-eof-interactive-repl-via-heredoc)
   - 2.3 [cat file.php \| php (Program from Stdin via Pipe)](#23-cat-filephp--php-program-from-stdin-via-pipe)

---

## 1. **Direct File Execution**

### 1.1 php \<file.php\> (Direct Interpreter Invocation)
**Method:** Invoke the `php` CLI on a `.php` file. The script must include the opening `<?php` tag. Simplest form; works regardless of shebang or executable bit.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L52) — `php /tmp/hello.php`

**Example:**
```bash
php hello.php
```

---

## 2. **Inline / One-Liners**

### 2.1 php -r "..." (Inline Expression, No `<?php` Tags)
**Method:** `-r` runs PHP code given on the command line without `<?php`/`?>` tags. PHP analog of `python -c` / `node -e`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L63) — `php -r 'echo "Hello from php -r! PHP " . PHP_VERSION . PHP_EOL;'`

**Example:**
```bash
php -r 'echo "Hello! PHP " . PHP_VERSION . PHP_EOL;'
```

### 2.2 php -a \<\<EOF (Interactive REPL via Heredoc)
**Method:** `-a` starts PHP's interactive REPL. Feeding it a shell heredoc executes multi-line PHP statements without a file on disk. Each line must include trailing `;`.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L67) — `php -a <<'EOF' … EOF`

**Example:**
```bash
php -a <<'EOF'
echo "Hello from php -a!\n";
echo "Sum: " . array_sum([1,2,3,4,5]) . "\n";
exit;
EOF
```

### 2.3 cat file.php \| php (Program from Stdin via Pipe)
**Method:** Pipe PHP source (including `<?php` tag) into `php` via stdin. Useful when the program is generated on the fly or comes from another command.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest3_.yml](../.github/workflows/pytest3_.yml#L75) — `printf '<?php echo "..."; ?>' | php`

**Example:**
```bash
printf '<?php echo "hello\n";\n' | php
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `php <file.php>` | Run a `.php` source file | [pytest3_.yml#L52](../.github/workflows/pytest3_.yml#L52) |
| `php -r "<expr>"` | Inline expression (no PHP tags) | [pytest3_.yml#L63](../.github/workflows/pytest3_.yml#L63) |
| `php -a <<EOF … EOF` | Interactive REPL via heredoc | [pytest3_.yml#L67](../.github/workflows/pytest3_.yml#L67) |
| `cat file.php \| php` | Source from stdin via pipe | [pytest3_.yml#L75](../.github/workflows/pytest3_.yml#L75) |
