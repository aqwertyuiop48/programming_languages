# ClojureScript Execution Methods in Programming Languages Repository

This document catalogues **all distinct ClojureScript execution methods** discovered for compiling, running, and executing `.cljs` code throughout the repository. JVM Clojure methods are catalogued separately in [clojure.md](clojure.md) because the implementations live in separate folders (`clojure_/clojure_script_/codeforces_script/` vs `clojure_/codeforces_script/`), target Node.js / browser instead of the JVM, and use a disjoint toolchain (nbb / shadow-cljs / lumo) with no `clojure` CLI or `lein` involved.

Each method takes ClojureScript source as input and produces the program's output. Intermediate steps (toolchain installation alone, dependency-only fetches, run-only steps on a pre-built artifact) are not listed as separate methods; if a single command performs compile + run together, that counts as one method.

## Table of Contents

1. **nbb (Scripting on Node, no compile step)**
   - 1.1 [nbb <file.cljs> (Run Script File)](#11-nbb-filecljs-run-script-file)
   - 1.2 [nbb -e '...' (Inline Expression)](#12-nbb--e--inline-expression)
   - 1.3 [nbb -e '...' (Multi-line with npm interop)](#13-nbb--e--multi-line-with-npm-interop)
   - 1.4 [npm run start → nbb (package.json script)](#14-npm-run-start--nbb-packagejson-script)

2. **shadow-cljs (Build → Node.js Target)**
   - 2.1 [npx shadow-cljs compile <build> && node out/main.js](#21-npx-shadow-cljs-compile-build--node-outmainjs)

3. **lumo-cljs (Standalone Self-Hosted CLJS)**
   - 3.1 [lumo -e '...' (Inline Expression)](#31-lumo--e--inline-expression)

4. **GitHub Action Setups (Toolchain Provisioning)**
   - 4.1 [actions/setup-node@v3 + npm install -g <runtime>](#41-actionssetup-nodev3--npm-install--g-runtime)

> **Note:** A `planck`-based execution path is present but **commented out** in the workflow ([clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L21-L27](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L21-L27)) and therefore not an active execution method.

---

## 1. **nbb (Scripting on Node, no compile step)**

[`nbb`](https://github.com/babashka/nbb) ("Not Babashka") is a fast, ad-hoc ClojureScript interpreter that runs on Node.js with full npm interop and zero build step.

### 1.1 nbb <file.cljs> (Run Script File)
**Method:** Pass a `.cljs` file directly to `nbb`; it loads the namespace and executes top-level forms. Stdin can be piped via shell redirection.

**Locations:**
- [Python/codeforces_script/direct/run.sh](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L52-L53) - `nbb "$script" < "$f" | tee "cljs_output/${testname}.txt"` (pipes test input on stdin)
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/direct/run.sh#L52-L53](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L52-L53)

**Workflow yml (executes in CI):**
- [Python/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L108-L113) - matrix-driven `./run.sh ${{ matrix.problem }}.cljs`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L108-L113](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L108-L113)

**Example:**
```bash
nbb hello.cljs
nbb 1A.cljs < test1.in
```

### 1.2 nbb -e '...' (Inline Expression)
**Method:** Evaluate a ClojureScript form given as a single shell argument.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L32) - `nbb -e '(+ 1 2 3)'`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L32](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L32)
- [Python/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L49) - `nbb -e '(+ 1 2 3)'`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L49](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L49)

**Example:**
```bash
nbb -e '(+ 1 2 3)'
```

### 1.3 nbb -e '...' (Multi-line with npm interop)
**Method:** A long heredoc-free form passed to `nbb -e` exercising npm interop (`csv-parse`, `shelljs`, `term-size`, `zx`) via `(:require ["pkg$default" :as alias])` style.

**Locations:**
- [clojure_/clojure_script_/codeforces_script/hello.cljs](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/hello.cljs) - the equivalent file form
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/hello.cljs](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/hello.cljs)

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L36-L58) - multi-line `nbb -e '(ns hello ...)'` with `csv-parse/sync`, `fs`, `path`, `shelljs`, `term-size`, `zx`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L36-L58](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L36-L58)

**Example:**
```bash
nbb -e '(ns hello
  (:require ["csv-parse/sync" :as csv]
            ["fs" :as fs]
            [nbb.core :refer [*file* await]]))
(println "Hello from nbb!")
(println (fs/readdirSync "."))'
```

### 1.4 npm run start → nbb (package.json script)
**Method:** `package.json`'s `"start"` script points at `nbb hello.cljs` so `npm run start` runs the ClojureScript file via the npm script runner.

**Locations:**
- [clojure_/clojure_script_/codeforces_script/package.json](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L2-L4) - `"start": "nbb hello.cljs"`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/package.json#L2-L4](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L2-L4)

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L33) - `npm run start`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L33](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L33)

**Example:**
```bash
npm run start    # → nbb hello.cljs
```

---

## 2. **shadow-cljs (Build → Node.js Target)**

[shadow-cljs](https://github.com/thheller/shadow-cljs) is the de-facto CLJS build tool with first-class npm interop. The repo's `shadow-cljs.edn` defines two `:node-script` builds (`:app` → `out/main.js`, `:eval` → `out/eval.js`).

### 2.1 npx shadow-cljs compile <build> && node out/main.js
**Method:** Compile a named shadow-cljs build to a Node.js script, then execute the emitted JS via `node`. The compile and run halves are inseparable from a "source-in → output-out" perspective.

**Locations:**
- [clojure_/clojure_script_/codeforces_script/my-cljs-node-app/shadow-cljs.edn](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/my-cljs-node-app/shadow-cljs.edn) - `:app` and `:eval` build definitions
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/my-cljs-node-app/shadow-cljs.edn](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/my-cljs-node-app/shadow-cljs.edn)

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L60-L65) - `npx shadow-cljs compile app` then `node out/main.js`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L60-L65](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L60-L65)
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L72-L77) - `npx shadow-cljs compile eval` then `node out/eval.js`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L72-L77](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L72-L77)

**Example:**
```bash
cd my-cljs-node-app
npm install
npm install left-pad
npx shadow-cljs compile app
node out/main.js

npx shadow-cljs compile eval
node out/eval.js
```

---

## 3. **lumo-cljs (Standalone Self-Hosted CLJS)**

### 3.1 lumo -e '...' (Inline Expression)
**Method:** [Lumo](https://github.com/anmonteiro/lumo) is a standalone, self-hosted ClojureScript REPL/interpreter that runs on Node.js. `lumo -e` evaluates a one-liner without a JVM.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L67-L70) - `npm install -g lumo-cljs` then `lumo -e "(println (+ 1 2))"`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L67-L70](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L67-L70)

**Example:**
```bash
npm install -g lumo-cljs
lumo -e "(println (+ 1 2))"
```

---

## 4. **GitHub Action Setups (Toolchain Provisioning)**

### 4.1 actions/setup-node@v3 + npm install -g <runtime>
**Method:** The only action used for CLJS provisioning is `actions/setup-node`; each runtime (`nbb`, `lumo-cljs`, `shadow-cljs`) is then installed via npm. No JVM is required for any of §1, §3; shadow-cljs in §2 transparently pulls in a JVM only at compile time.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L17-L19) - `uses: actions/setup-node@v3` with `node-version: '20'`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L17-L19](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L17-L19)
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L31) - `npm install nbb -g`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L31](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L31)
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L69) - `npm install -g lumo-cljs`
  - Remote (submodule `clojure_/clojure_script_/codeforces_script` @ branch `clojure_script`): [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml#L69](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L69)

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| nbb &lt;file.cljs&gt; | Run .cljs file directly | [Python/codeforces_script/direct/run.sh](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L53)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L53) |
| nbb -e '...' | Inline expression | [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L32)<br/>[remote @ `clojure_script`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L32) |
| nbb -e (multi-line + npm) | npm-interop expression | [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L36)<br/>[remote @ `clojure_script`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L36) |
| npm run start | package.json script → nbb | [package.json](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L3)<br/>[remote @ `clojure_script`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L3) |
| shadow-cljs compile + node | Build → Node target | [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L64)<br/>[remote @ `clojure_script`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L64) |
| lumo -e '...' | Standalone self-hosted CLJS | [main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L70)<br/>[remote @ `clojure_script`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L70) |

---

## Key Frameworks & Tools Integrated

- **nbb** (Babashka family) - Fast Node-hosted CLJS interpreter, full npm interop, zero build step
- **shadow-cljs** - Reference CLJS build tool with first-class npm interop (`:node-script` target used here)
- **lumo-cljs** - Standalone self-hosted ClojureScript REPL/runner on Node.js
- **Node.js** - The runtime for every method above (`actions/setup-node@v3` with Node 20)
- **npm packages exercised** - `left-pad`, `csv-parse`, `shelljs`, `term-size`, `zx`
- **GitHub Actions** - `actions/setup-node@v3`; runtimes themselves installed via `npm install -g`
- **planck** - Standalone CLJS REPL — present in the workflow but commented out (not an active execution path)

---
