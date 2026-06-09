# Go Execution Methods in Programming Languages Repository

This document catalogues **all distinct Go-only methods** discovered for building, running, and executing Go code throughout the repository. Each method takes Go source code as input and produces the program's output. If a single command performs compile + run together (e.g. `go run`, `go test`), that counts as one method; otherwise the compile and run commands are paired into a single method.

## Table of Contents

1. **Direct Compilation & Execution**
   - 1.1 [go run \<file.go\> (Single-File Compile + Run)](#11-go-run-filego-single-file-compile--run)
   - 1.2 [go run Loop over Directory](#12-go-run-loop-over-directory)
   - 1.3 [nohup go run … & (Background Server)](#13-nohup-go-run---background-server)
   - 1.4 [go build + ./binary (Compile + Run Pair)](#14-go-build--binary-compile--run-pair)
   - 1.5 [go test (Compile + Run Tests)](#15-go-test-compile--run-tests)

2. **Inline Interpreters**
   - 2.1 [goeval -i \<pkg\> '\<expr\>' (Inline Go REPL)](#21-goeval--i-pkg-expr-inline-go-repl)
   - 2.2 [yaegi run \<file.go\> (Go Interpreter)](#22-yaegi-run-filego-go-interpreter)

3. **Distributed Workflows**
   - 3.1 [Temporal Worker + Starter (go run pair)](#31-temporal-worker--starter-go-run-pair)

4. **Web Frameworks & Servers**
   - 4.1 [net/http ListenAndServe via go run](#41-nethttp-listenandserve-via-go-run)
   - 4.2 [gorilla/mux REST Server via go run](#42-gorillamux-rest-server-via-go-run)
   - 4.3 [TCP Socket Server + Client (paired go run)](#43-tcp-socket-server--client-paired-go-run)

5. **Serverless Deployment**
   - 5.1 [Vercel Go Functions (gin-gonic)](#51-vercel-go-functions-gin-gonic)

6. **Polyglot Embedding (Other ➜ Go)**
   - 6.1 [Python subprocess.run ➜ goeval](#61-python-subprocessrun--goeval)
   - 6.2 [Node.js child_process.execFile ➜ goeval](#62-nodejs-child_processexecfile--goeval)

---

## 1. **Direct Compilation & Execution**

### 1.1 go run \<file.go\> (Single-File Compile + Run)
**Method:** The `go run` toolchain command compiles and immediately executes a single `.go` file (or set of files within the same package) in one step. Binary lives in a temp dir and is discarded after the process exits. Canonical script-style Go execution.

**Locations:**
- [golang/readme.txt](../golang/readme.txt#L14) - `go run hello.go` baseline example
- [golang/6_go_movies_crud/readme.md](../golang/6_go_movies_crud/readme.md#L4) - `go run main.go` (Movies CRUD REST API)
- [golang/6_go_movies_crud_1/readme.md](../golang/6_go_movies_crud_1/readme.md#L4) - `go run main.go`
- [golang/5_socket_programming/readme.md](../golang/5_socket_programming/readme.md#L4-L20) - `go run server.go`, `go run client.go`, `go run server_2.go`, `go run client_2.go`
- [golang/7_mysql_connector/readme.md](../golang/7_mysql_connector/readme.md#L11-L12) - `go run mysql_connector_.go`, `go run mysql_connector_2.go`
- [java/temporal/edu-101-java-code/.bash.cfg](../java/temporal/edu-101-java-code/.bash.cfg#L34) - `go run worker/main.go` (Temporal worker)
- [java/temporal/edu-101-java-code/.bash.cfg](../java/temporal/edu-101-java-code/.bash.cfg#L37) - `go run start/main.go "${1}"` (Temporal starter)
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L187-L196) - Canonical inline demo

**Example:**
```bash
go run main.go
go run server.go
go run worker/main.go
```

### 1.2 go run Loop over Directory
**Method:** CI step iterates every `*.go` in a directory and invokes `go run` on each file. Useful for repos that hold many small standalone `package main` files (codeforces solutions, snippets, demos).

**Locations:**
- [golang/codeforces_script/.github/workflows/main.yml](../golang/codeforces_script/.github/workflows/main.yml#L66-L80) - Two consecutive loops over `execute/` and `2_intro/`
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/.github/workflows/main.yml#L66-L80](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L66-L80)
- [golang/codeforces_script/execute/](../golang/codeforces_script/execute/), [2_intro/](../golang/codeforces_script/2_intro/) - Source dirs driven by the loops

**Example:**
```bash
cd execute
for file in *.go; do
  echo "Running $file"
  go run "$file"
done
```

### 1.3 nohup go run … & (Background Server)
**Method:** `nohup go run server.go &` launches a long-running Go server in the background of a CI step, followed by `sleep` to wait for the port to bind. Subsequent steps can `curl` the server and a Playwright step can screenshot rendered pages.

**Locations:**
- [golang/codeforces_script/.github/workflows/main.yml](../golang/codeforces_script/.github/workflows/main.yml#L81-L100) - `nohup go run server_/server_1.go &` + `sleep 10` + curl + Playwright screenshots
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/.github/workflows/main.yml#L81-L100](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L81-L100)
- [golang/codeforces_script/server_/server_1.go](../golang/codeforces_script/server_/server_1.go) - The backgrounded server source

**Example:**
```bash
mkdir -p videos
nohup go run server_/server_1.go &
sleep 10
curl http://localhost:8080
```

### 1.4 go build + ./binary (Compile + Run Pair)
**Method:** `go build -o <path> <pkg>` emits a persistent binary; the next command runs it. Differs from `go run` in that the binary is kept on disk and can be re-invoked, shipped, or signed.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L198-L200) - `go build -o /tmp/hello_go_bin /tmp/hello_go.go && /tmp/hello_go_bin`

**Example:**
```bash
go build -o /tmp/app ./cmd/server && /tmp/app
```

### 1.5 go test (Compile + Run Tests)
**Method:** `go test ./...` discovers `*_test.go` files across the package set, compiles a per-package test binary, and runs it in one command. End-to-end source → test results.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L202-L217) - Canonical demo: `add.go` + `add_test.go` + `go test -v ./...`

**Example:**
```go
// add.go
package gotest
func Add(a, b int) int { return a + b }

// add_test.go
package gotest
import "testing"
func TestAdd(t *testing.T) { if Add(2,3)!=5 { t.Fatal("nope") } }
```
```bash
go test -v ./...
```

---

## 2. **Inline Interpreters**

### 2.1 goeval -i \<pkg\> '\<expr\>' (Inline Go REPL)
**Method:** [`goeval`](https://github.com/dolmen-go/goeval) is an inline Go evaluator. `goeval -i fmt '<expr>'` imports the package and evaluates the expression — the Go analog of `python -c` / `node -e`. Multiple `-i` flags chain imports.

**Locations:**
- [golang/codeforces_script/.github/workflows/main.yml](../golang/codeforces_script/.github/workflows/main.yml#L40-L59) - Clone + `go install .` + `goeval -i fmt '...'` + `goeval -i fmt -i math '...'`
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/.github/workflows/main.yml#L40-L59](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L59)
- [golang/readme.txt](../golang/readme.txt#L268-L271) - Installation reference

**Example:**
```bash
goeval -i fmt 'fmt.Println("Hello, Goeval!")'
goeval -i fmt -i math 'fmt.Printf("sqrt(16) = %.2f\n", math.Sqrt(16))'
```

### 2.2 yaegi run \<file.go\> (Go Interpreter)
**Method:** [`yaegi`](https://github.com/traefik/yaegi) is a pure-Go interpreter (no compiler step). `yaegi run <file.go>` parses and executes the file directly. Useful for scripting, hot-reload, and embedded Go evaluation. Alternative to `goeval` for whole-file execution.

**Locations:**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L219-L229) - `go install github.com/traefik/yaegi/cmd/yaegi@latest` + `yaegi run /tmp/hello_yaegi.go`

**Example:**
```bash
go install github.com/traefik/yaegi/cmd/yaegi@latest
yaegi run /tmp/hello.go
```

---

## 3. **Distributed Workflows**

### 3.1 Temporal Worker + Starter (go run pair)
**Method:** Temporal's Go SDK requires two long-running processes: a **worker** that registers workflow/activity implementations and polls the task queue, and a **starter** that submits workflow executions. Both are launched via `go run`; together they execute the full workflow end-to-end.

**Locations:**
- [java/temporal/edu-101-java-code/exercises/finale-workflow/README.md](../java/temporal/edu-101-java-code/exercises/finale-workflow/README.md#L34-L45) - `go run worker/main.go` (terminal 1) + `go run start/main.go "Mason Egger"` (terminal 2)
- [java/temporal/edu-101-java-code/exercises/finale-workflow/go/worker/main.go](../java/temporal/edu-101-java-code/exercises/finale-workflow/go/worker/main.go) - Worker implementation
- [java/temporal/edu-101-java-code/exercises/finale-workflow/go/start/main.go](../java/temporal/edu-101-java-code/exercises/finale-workflow/go/start/main.go) - Starter implementation
- [java/temporal/edu-101-java-code/exercises/finale-workflow/go/workflow.go](../java/temporal/edu-101-java-code/exercises/finale-workflow/go/workflow.go) - Workflow definition
- [java/temporal/edu-101-java-code/.bash.cfg](../java/temporal/edu-101-java-code/.bash.cfg#L33-L37) - Bash aliases `ex4w` / `ex4s`

**Example:**
```bash
# Terminal 1
cd go && go run worker/main.go
# Terminal 2
cd go && go run start/main.go "Your Name"
```

---

## 4. **Web Frameworks & Servers**

### 4.1 net/http ListenAndServe via go run
**Method:** Standard-library HTTP server using `http.ListenAndServe(":8080", nil)` launched via `go run`. The CI workflow backgrounds it (§1.3), then drives it with `curl`/Playwright.

**Locations:**
- [golang/codeforces_script/server_/server_1.go](../golang/codeforces_script/server_/server_1.go#L26) - `http.ListenAndServe(":8080", nil)`
- [golang/3_go_server/server_2/main.go](../golang/3_go_server/server_2/main.go#L41) - Same pattern
- [golang/readme.txt](../golang/readme.txt#L216-L245) - HTTP server example

**Example:**
```bash
go run server_/server_1.go
# in another terminal:
curl http://localhost:8080/hello
```

### 4.2 gorilla/mux REST Server via go run
**Method:** REST API using `gorilla/mux` router launched via `go run main.go`. CRUD endpoints serve JSON over `http.ListenAndServe`.

**Locations:**
- [golang/6_go_movies_crud/main.go](../golang/6_go_movies_crud/main.go#L117) - `log.Fatal(http.ListenAndServe(":8080", r))`
- [golang/6_go_movies_crud_1/main_3.go](../golang/6_go_movies_crud_1/main_3.go#L116) - Same pattern

**Example:**
```bash
go run main.go
curl http://localhost:8080/movies
```

### 4.3 TCP Socket Server + Client (paired go run)
**Method:** Paired Go programs — one binds a TCP listener via `net.Listen("tcp", ":9988")`, the other dials it via `net.Dial`. Both are launched independently with `go run`. End-to-end execution requires both halves.

**Locations:**
- [golang/5_socket_programming/server.go](../golang/5_socket_programming/server.go), [client.go](../golang/5_socket_programming/client.go) - Primary server/client pair
- [golang/5_socket_programming/server_2.go](../golang/5_socket_programming/server_2.go), [client_2.go](../golang/5_socket_programming/client_2.go) - Variant pair
- [golang/5_socket_programming/readme.md](../golang/5_socket_programming/readme.md#L1-L20) - Run instructions

**Example:**
```bash
# Terminal 1
go run server.go
# Terminal 2
go run client.go
```

---

## 5. **Serverless Deployment**

### 5.1 Vercel Go Functions (gin-gonic)
**Method:** A `main` package under `api/` exporting an HTTP handler is detected by Vercel as a serverless Go function. Vercel compiles and deploys it on `vercel --prod`; HTTP requests to the configured route invoke the handler. End-to-end source → live HTTPS endpoint via one deploy command.

**Locations:**
- [golang/golang_/golang_vercel/api/entrypoint.go](../golang/golang_/golang_vercel/api/entrypoint.go) - gin-gonic-based handler
- [golang/golang_/golang_vercel/vercel.json](../golang/golang_/golang_vercel/vercel.json) - Routes config
- [golang/golang_/golang_vercel/go.mod](../golang/golang_/golang_vercel/go.mod) - Module declaration
- [golang/golang_/golang_vercel/readme.md](../golang/golang_/golang_vercel/readme.md#L11-L13) - Deploy commands
- [javascript/next_/nextjs_app/api/entrypoint.go](../javascript/next_/nextjs_app/api/entrypoint.go) - Same pattern under a Next.js project

**Example:**
```bash
git push origin main
vercel . && vercel --prod
# → https://your-project.vercel.app/api/applications
```

---

## 6. **Polyglot Embedding (Other ➜ Go)**

### 6.1 Python subprocess.run ➜ goeval
**Method:** A Python program builds a multi-line Go source string and passes it to `subprocess.run(["goeval", "<go code>"])`. Python receives the Go program's stdout/stderr through the subprocess result.

**Locations:**
- [golang/6_go_movies_crud_1/main_2.py](../golang/6_go_movies_crud_1/main_2.py#L2) - `subprocess.run(["goeval", '''<go>'''])`
- [golang/3_go_server/server_2/main_2.py](../golang/3_go_server/server_2/main_2.py#L2) - Same pattern

**Example:**
```python
import subprocess
subprocess.run(["goeval", '''
import "fmt"
fmt.Println("hi from python-driven goeval")
'''])
```

### 6.2 Node.js child_process.execFile ➜ goeval
**Method:** Node.js builds a Go source string and passes it to `child_process.execFile("goeval", [code], cb)`. The callback receives the Go program's stdout/stderr. Used to embed Go server/client snippets inside a Node.js orchestrator.

**Locations:**
- [golang/4_nodejs_embed/server_go_js.go](../golang/4_nodejs_embed/server_go_js.go#L65-L68) - Node.js inside a Go-string-template invoking `goeval` for a TCP server
- [golang/4_nodejs_embed/client_go_js.go](../golang/4_nodejs_embed/client_go_js.go#L41-L43) - Same pattern for the client side
- [golang/4_nodejs_embed/node_embed.go](../golang/4_nodejs_embed/node_embed.go) - Embedder shell

**Example:**
```js
const cp = require('child_process');
const goSrc = `
import "fmt"
fmt.Println("hi from nodejs-driven goeval")
`;
cp.execFile("goeval", [goSrc], (e, out) => console.log(out));
```
