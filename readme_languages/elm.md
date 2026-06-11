# Elm Execution Methods in Programming Languages Repository

This document catalogues **all distinct Elm methods** discovered for compiling and running Elm code throughout the repository. Elm compiles to JavaScript (`elm make … --output=*.js`) which is then loaded by HTML hosts. The CI also includes Python-driven Docker orchestration for end-to-end HTML generation.

## Table of Contents

1. **Toolchain Setup**
   - 1.1 [npm install -g elm + elm install \<package\>](#11-npm-install--g-elm--elm-install-package)

2. **Compile and Run**
   - 2.1 [elm make \<file.elm\> --output=\<file.js\> (Compile to JS)](#21-elm-make-fileelm---outputfilejs-compile-to-js)

3. **Polyglot Embedding (Other ➜ Elm)**
   - 3.1 [python run_docker.py (Python ➜ Docker ➜ HTML)](#31-python-run_dockerpy-python--docker--html)

4. **Interactive REPL**
   - 4.1 [elm repl (Heredoc on stdin)](#41-elm-repl-heredoc-on-stdin)

---

## 1. **Toolchain Setup**

### 1.1 npm install -g elm + elm install \<package\>
**Method:** Elm itself is published as an npm package (`elm`); installing it globally exposes the `elm` CLI on `$PATH`. Project-level Elm packages (`elm/core`, `elm/html`, `elm/browser`, `hecrj/html-parser`) are then pulled in via `elm install` and persisted in `elm.json`.

**Locations:**
- [elm/codeforces_script/elm.json](../elm/codeforces_script/elm.json) - Application config (Elm 0.19.1) declaring direct deps: `elm/core`, `elm/html`, `elm/browser`, `elm/json`, `elm/virtual-dom`, `elm/time`, `elm/url`, `elm/parser`, `hecrj/html-parser`, `rtfeldman/elm-hex`
  - Remote (submodule `elm/codeforces_script` @ branch `elm_`): [elm.json](https://github.com/aqwertyuiop48/codeforces_script/blob/elm_/elm.json)

**Workflow yml (executes in CI):**
- [elm/codeforces_script/.github/workflows/main.yml](../elm/codeforces_script/.github/workflows/main.yml#L18-L25) - `actions/setup-node@v2` + `npm install -g elm`
- [elm/codeforces_script/.github/workflows/main.yml](../elm/codeforces_script/.github/workflows/main.yml#L37) - `elm install elm/core && elm install elm/html && elm install elm/browser && elm install hecrj/html-parser`

**Example:**
```bash
npm install -g elm
elm install elm/core
elm install elm/html
```

---

## 2. **Compile and Run**

### 2.1 elm make \<file.elm\> --output=\<file.js\> (Compile to JS)
**Method:** `elm make` is the Elm compiler. Given a `.elm` entry-point module, it emits JavaScript (or HTML). The CI compiles every Elm source under `src/` into a sibling `.js` in `js_/`. Each `src/Foo.elm` becomes `js_/Foo.js`, which is then loaded from a corresponding HTML host page in `html_/`.

**Locations:**
- [elm/codeforces_script/src/Main.elm](../elm/codeforces_script/src/Main.elm) - root module
- [elm/codeforces_script/src/Hello_1.elm](../elm/codeforces_script/src/Hello_1.elm) - simplest example
- [elm/codeforces_script/src/Html_2.elm](../elm/codeforces_script/src/Html_2.elm), [Html_3.elm](../elm/codeforces_script/src/Html_3.elm), [Html_iframe.elm](../elm/codeforces_script/src/Html_iframe.elm) - HTML producers
- [elm/codeforces_script/src/Functions_4.elm](../elm/codeforces_script/src/Functions_4.elm), [Lists_5.elm](../elm/codeforces_script/src/Lists_5.elm), [Conditionals_6.elm](../elm/codeforces_script/src/Conditionals_6.elm), [Strings_7.elm](../elm/codeforces_script/src/Strings_7.elm) - language-feature demos
- [elm/codeforces_script/html_/](../elm/codeforces_script/html_/) - HTML host pages that include the compiled `.js`
- [elm/readme.txt](../elm/readme.txt) - documents `elm make Main.elm`, `elm-live`, `brew install elm`
  - Remote: [src/](https://github.com/aqwertyuiop48/codeforces_script/tree/elm_/src)

**Workflow yml (executes in CI):**
- [elm/codeforces_script/.github/workflows/main.yml](../elm/codeforces_script/.github/workflows/main.yml#L38-L46) - nine `elm make src/<X>.elm --output=js_/<X>.js` lines covering every Elm module
- [elm/codeforces_script/.github/workflows/main.yml](../elm/codeforces_script/.github/workflows/main.yml#L48-L60) - uploads `html_/`, `js_/`, and the per-module `index*.html` artifacts

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L124) — submodule sync that triggers the `elm_` branch run

**Example:**
```bash
elm make src/Hello_1.elm --output=js_/Hello_1.js
```

---

## 3. **Polyglot Embedding (Other ➜ Elm)**

### 3.1 python run_docker.py (Python ➜ Docker ➜ HTML)
**Method:** Two Python scripts (`run_docker.py`, `run_docker1.py`) orchestrate Docker as part of the Elm build pipeline — they `docker build` the local Dockerfile, `docker run -d` a long-lived container, `docker exec` an HTML-generation shell snippet inside it (writing `index_container.html`), `docker cp` the artifact out, then stop/remove the container. `run_docker1.py` is the same flow but mounts a `type=tmpfs` volume for the intermediate file.

**Locations:**
- [elm/codeforces_script/run_docker.py](../elm/codeforces_script/run_docker.py) - `run_docker`, `run_docker1`, `run_docker2`, `run_docker3` functions; full `docker build / run / exec / cp / stop / rm` sequence
- [elm/codeforces_script/run_docker1.py](../elm/codeforces_script/run_docker1.py) - variant with `--mount type=tmpfs,dst=/tmp/my_tmpfs`
- [elm/codeforces_script/Dockerfile](../elm/codeforces_script/Dockerfile) - `FROM ubuntu:latest` + `apt-get install -y wget unzip`

**Workflow yml (executes in CI):**
- [elm/codeforces_script/.github/workflows/main.yml](../elm/codeforces_script/.github/workflows/main.yml#L28-L30) - `python run_docker.py` (runs the four `run_docker*` functions)
- [elm/codeforces_script/.github/workflows/main.yml](../elm/codeforces_script/.github/workflows/main.yml#L32-L34) - `python run_docker1.py` (tmpfs variant)

**Example:**
```bash
python run_docker.py
python run_docker1.py
```

---

## 4. **Interactive REPL**

### 4.1 elm repl (Heredoc on stdin)
**Method:** `elm repl` is Elm's interactive read-eval-print loop. It must be run inside a directory containing an `elm.json` (use `elm init` to bootstrap one). Feeding it a bash heredoc lets you script multi-line REPL sessions in CI. Useful for evaluating expressions without writing a full module + `elm make` step.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `mkdir /tmp/elm_repl_demo && cd /tmp/elm_repl_demo && yes | elm init && elm repl <<EOF … EOF`

**Example:**
```bash
mkdir my_repl && cd my_repl
yes | elm init
elm repl <<'EOF'
"Hello from elm repl heredoc!"
1 + 2
:exit
EOF
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `npm install -g elm` + `elm install <pkg>` | Toolchain & package install | [elm.json](../elm/codeforces_script/elm.json) |
| `elm make <file.elm> --output=<file.js>` | Compile Elm → JavaScript | [src/](../elm/codeforces_script/src/) |
| `python run_docker.py` | Python ➜ Docker ➜ HTML build orchestration | [run_docker.py](../elm/codeforces_script/run_docker.py) |
| `elm repl` (heredoc) | Interactive REPL session | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
