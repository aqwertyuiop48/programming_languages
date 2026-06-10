# Perl Execution Methods in Programming Languages Repository

This document catalogues **all distinct Perl-only methods** discovered for running Perl code throughout the repository. Because Perl is interpreted, every method is single-step (source → output). Each command-line flag combination is documented separately because the flags meaningfully change execution semantics (implicit line loop, auto-print, in-place edit, etc.).

## Table of Contents

1. **Direct File Execution**
   - 1.1 [perl \<file.pl\> (Direct Interpreter Invocation)](#11-perl-filepl-direct-interpreter-invocation)
   - 1.2 [#!/usr/bin/perl Shebang Script](#12-usrbinperl-shebang-script)

2. **Inline / One-Liners**
   - 2.1 [perl -e '...' (Inline Expression)](#21-perl--e--inline-expression)
   - 2.2 [perl -E '...' (Inline with `say` Enabled)](#22-perl--e--inline-with-say-enabled)
   - 2.3 [perl -n -e '...' (Implicit Line Loop, No Auto-Print)](#23-perl--n--e--implicit-line-loop-no-auto-print)
   - 2.4 [perl -p -e '...' (Implicit Line Loop with Auto-Print)](#24-perl--p--e--implicit-line-loop-with-auto-print)
   - 2.5 [perl - <<EOF (Stdin Heredoc)](#25-perl---eof-stdin-heredoc)
   - 2.6 [echo '...' | perl - (Piped REPL Stdin)](#26-echo---perl---piped-repl-stdin)
   - 2.7 [perl -i -pe '...' (In-Place Edit)](#27-perl--i--pe--in-place-edit)

3. **Polyglot Embedding (Other ➜ Perl)**
   - 3.1 [Node.js child_process.execFile ➜ perl -e](#31-nodejs-child_processexecfile--perl--e)
   - 3.2 [Go ➜ Node.js ➜ perl -e (Nested)](#32-go--nodejs--perl--e-nested)

---

## 1. **Direct File Execution**

### 1.1 perl \<file.pl\> (Direct Interpreter Invocation)
**Method:** Invoke the Perl interpreter on a `.pl` script file. Simplest form; works regardless of whether the file has a shebang or executable bit.

**Locations:**
- [perl/codeforces_script/script.pl](../perl/codeforces_script/script.pl) - Multi-line string demo (`q{...}`)
  - Remote: [script.pl](https://github.com/aqwertyuiop48/codeforces_script/blob/perl_/script.pl)

**Workflow yml (executes in CI):**
- [perl/codeforces_script/.github/workflows/main.yml](../perl/codeforces_script/.github/workflows/main.yml#L24) - `perl script.pl`
  - Remote (submodule `perl/codeforces_script` @ branch `perl_`): [perl/codeforces_script/.github/workflows/main.yml#L24](https://github.com/aqwertyuiop48/codeforces_script/blob/perl_/.github/workflows/main.yml#L24)
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L235-L242) - Canonical inline demo

**Example:**
```bash
perl script.pl
```

### 1.2 #!/usr/bin/perl Shebang Script
**Method:** Place `#!/usr/bin/perl` (or `#!/usr/bin/env perl`) on the first line of a `.pl` file, mark it executable with `chmod +x`, and invoke it directly as `./script.pl`. The kernel reads the shebang and invokes Perl.

**Locations:**
- [perl/codeforces_script/script.pl](../perl/codeforces_script/script.pl#L1) - `#!/usr/bin/perl` shebang
  - Remote: [script.pl#L1](https://github.com/aqwertyuiop48/codeforces_script/blob/perl_/script.pl#L1)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](.github/workflows/main.yml) _(rule R2)_ — covers `perl/codeforces_script/script.pl`
- [perl/codeforces_script/.github/workflows/main.yml](perl/codeforces_script/.github/workflows/main.yml) _(rule R1)_ — covers `perl/codeforces_script/script.pl`

**Example:**
```perl
#!/usr/bin/perl
use strict; use warnings;
print "hi\n";
```
```bash
chmod +x script.pl
./script.pl
```

---

## 2. **Inline / One-Liners**

### 2.1 perl -e '...' (Inline Expression)
**Method:** `-e` evaluates its argument as a Perl program. Direct Perl analog of `python -c` / `node -e`. No file required.

**Locations:**
- [javascript/readme.ipynb](../javascript/readme.ipynb#L1017-L1137) - Used inside Node.js `execFile('perl', ['-e', code])` polyglot (see §3.1)
- [golang/readme.txt](../golang/readme.txt#L97) - Used inside Go-driven Node.js polyglot (see §3.2)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L244-L246) - Direct canonical demo

**Example:**
```bash
perl -e 'print "Hello!\n"; print "Perl $^V\n";'
```

### 2.2 perl -E '...' (Inline with `say` Enabled)
**Method:** `-E` is like `-e` but additionally enables all features of the matching Perl version — most notably `say` (print with newline). Use when you want `say` without an explicit `use feature 'say'`.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L248-L250) - Direct canonical demo

**Example:**
```bash
perl -E 'say "Hello!"; say "Perl $^V";'
```

### 2.3 perl -n -e '...' (Implicit Line Loop, No Auto-Print)
**Method:** `-n` wraps the program in an implicit `while (<>) { ... }` line-loop reading from stdin or files. No auto-print — you must `print` explicitly. Filter / extract / count-per-line idiom.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L252-L254) - `printf '…\n' | perl -n -e 'print "got: $_"'`

**Example:**
```bash
printf 'apple\nbanana\ncherry\n' | perl -n -e 'print "got: $_"'
```

### 2.4 perl -p -e '...' (Implicit Line Loop with Auto-Print)
**Method:** Like `-n` but additionally auto-prints `$_` at the end of each iteration. Sed-style stream-edit idiom.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L256-L258) - `printf '…\n' | perl -p -e 's/a/A/g'`

**Example:**
```bash
printf 'apple\nbanana\ncherry\n' | perl -p -e 's/a/A/g'
```

### 2.5 perl - <<EOF (Stdin Heredoc)
**Method:** Pass `-` as the script argument so Perl reads source from stdin, then feed it a shell heredoc. Multi-line Perl program without a file on disk.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L260-L268) - `perl - <<'EOF' … EOF`

**Example:**
```bash
perl - <<'EOF'
use strict; use warnings;
my @nums = (1,2,3,4,5);
my $sum = 0; $sum += $_ for @nums;
print "Sum: $sum\n";
EOF
```

### 2.6 echo '...' | perl - (Piped REPL Stdin)
**Method:** One-liner pipe form of §2.5 — stdin source comes from `echo` (or any command emitting Perl on stdout) instead of a heredoc.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L270-L272) - `echo '…' | perl -`

**Example:**
```bash
echo 'print "hi $^V\n";' | perl -
```

### 2.7 perl -i -pe '...' (In-Place Edit)
**Method:** `-i` rewrites the named file(s) in place; combined with `-pe` it becomes a streaming sed-style editor. The output replaces the original file.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L274-L277) - `perl -i -pe 's/foo/FOO/' /tmp/edit.txt`

**Example:**
```bash
perl -i -pe 's/foo/FOO/' file.txt
# optionally with a backup suffix:
perl -i.bak -pe 's/foo/FOO/' file.txt
```

---

## 3. **Polyglot Embedding (Other ➜ Perl)**

### 3.1 Node.js child_process.execFile ➜ perl -e
**Method:** Node.js builds a Perl source string and passes it to `child_process.execFile('perl', ['-e', code], cb)`. The callback receives Perl's stdout/stderr. Used to invoke Perl utilities (here, socket-programming snippets) from a Node.js orchestrator.

**Locations:**
- [javascript/readme.ipynb](../javascript/readme.ipynb#L1017-L1018) - Basic hello
- [javascript/readme.ipynb](../javascript/readme.ipynb#L1043-L1044) - Socket client
- [javascript/readme.ipynb](../javascript/readme.ipynb#L1090-L1091) - Socket server
- [javascript/readme.ipynb](../javascript/readme.ipynb#L1136-L1137) - Additional networking

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L286-L288) - inline shim `node -e "require('child_process').execFile('perl',['-e','print \"hi from perl-via-node\\n\"'],...)"`. Perl is preinstalled on the `ubuntu-latest` runner, so no setup action is needed. The `.ipynb` cells in Locations above are not run directly (no `jupyter nbconvert`/`papermill` runner in the repo), but the §3.1 method itself — Node → Perl via `execFile` — is exercised end-to-end.

**Example:**
```js
const cp = require('child_process');
const code = 'print "hi from perl-via-node\\n";';
cp.execFile('perl', ['-e', code], (e, out) => console.log(out));
```

### 3.2 Go ➜ Node.js ➜ perl -e (Nested)
**Method:** Three-language nesting: Go uses `exec.Command("node", "-e", <js>)` where the JS payload itself calls `child_process.execFile('perl', ['-e', perl_code])`. Demonstrates polyglot chaining across runtime boundaries — Go → Node → Perl, with output flowing back through both pipes.

**Locations:**
- [golang/readme.txt](../golang/readme.txt#L50-L97) - Full nested example; Perl code embedded in Go-generated Node string at line 59

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L290-L300) - inline shim writes a minimal `/tmp/g2np.go` whose `main()` does `exec.Command("node", "-e", \`require('child_process').execFile('perl',['-e','print "hi from go-via-node-via-perl\\n"'],...)\`)`, then `go run /tmp/g2np.go`. Go is preinstalled at `/usr/local/go/bin/go` on `ubuntu-latest`, Node from `setup-node@v4` earlier in the job, Perl from the base image — all three runtimes available without extra setup. The `golang/readme.txt` reference in Locations is a plain doc file (never executed directly), but the §3.2 method itself — Go → Node → Perl — is exercised end-to-end.

**Example:**
```go
exec.Command("node", "-e", `
  const cp = require('child_process');
  cp.execFile('perl', ['-e', 'print "hi from go-via-node-via-perl\n";'],
    (e, out) => console.log(out));
`).Run()
```
