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
- [java/temporal/edu-101-java-code/.bash.cfg](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.bash.cfg#L34) - `go run worker/main.go` (Temporal worker)
- [java/temporal/edu-101-java-code/.bash.cfg](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.bash.cfg#L37) - `go run start/main.go "${1}"` (Temporal starter)
  - Remote (submodule `java/temporal/edu-101-java-code` @ branch `main`): [java/temporal/edu-101-java-code/.bash.cfg#L33-L37](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.bash.cfg#L33-L37)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L187-L196) - Canonical inline demo step `go run /tmp/hello_go.go`
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L62-L80) - "Run all Go files in the folders" step loops `go run "$file"` over `execute/*.go` and `2_intro/*.go` (see also §1.2)
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/.github/workflows/main.yml#L62-L80](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L62-L80)

**Example:**
```bash
go run main.go
go run server.go
go run worker/main.go
```

### 1.2 go run Loop over Directory
**Method:** CI step iterates every `*.go` in a directory and invokes `go run` on each file. Useful for repos that hold many small standalone `package main` files (codeforces solutions, snippets, demos).

**Locations:**
- [golang/codeforces_script/execute/](https://github.com/aqwertyuiop48/codeforces_script/tree/golang_/execute), [2_intro/](https://github.com/aqwertyuiop48/codeforces_script/tree/golang_/2_intro) - Source dirs driven by the loops

**Workflow yml (executes in CI):**
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L62-L80) - "Run all Go files in the folders" step: two consecutive loops `cd execute && for file in *.go; do go run "$file"; done` then the same for `2_intro/`
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/.github/workflows/main.yml#L62-L80](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L62-L80)

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
- [golang/codeforces_script/server_/server_1.go](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/server_/server_1.go) - The backgrounded server source
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/server_/server_1.go](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/server_/server_1.go)

**Workflow yml (executes in CI):**
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L78-L84) - "Run Go Server" step: `mkdir -p videos && nohup go run server_/server_1.go & && sleep 10`
  - Remote: [golang/codeforces_script/.github/workflows/main.yml#L78-L84](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L78-L84)
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L85-L100) - Follow-on "Capture HTML content" + "Take screenshot" steps drive the backgrounded server via `curl` and `npx playwright screenshot`
  - Remote: [golang/codeforces_script/.github/workflows/main.yml#L85-L100](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L85-L100)

**Example:**
```bash
mkdir -p videos
nohup go run server_/server_1.go &
sleep 10
curl http://localhost:8080
```

### 1.4 go build + ./binary (Compile + Run Pair)
**Method:** `go build -o <path> <pkg>` emits a persistent binary; the next command runs it. Differs from `go run` in that the binary is kept on disk and can be re-invoked, shipped, or signed.

**Locations:** No persistent `go build`-based example lives outside the demo workflow yet.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L198-L200) - `go build -o /tmp/hello_go_bin /tmp/hello_go.go && /tmp/hello_go_bin`

**Example:**
```bash
go build -o /tmp/app ./cmd/server && /tmp/app
```

### 1.5 go test (Compile + Run Tests)
**Method:** `go test ./...` discovers `*_test.go` files across the package set, compiles a per-package test binary, and runs it in one command. End-to-end source → test results.

**Locations:** No checked-in `*_test.go` files outside the inline demo; pattern documented for future suites.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L202-L217) - Canonical demo: writes `add.go` + `add_test.go` then `go test -v ./...`

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
- [golang/readme.txt](../golang/readme.txt#L268-L271) - Installation reference

**Workflow yml (executes in CI):**
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L60) - "Clone goeval repository" + "Install goeval" + "Add Go bin directory to PATH" + "Verify goeval installation" (`goeval -i fmt '…'`) + "Run Go code with goeval" (`goeval -i fmt -i math '…'`)
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [golang/codeforces_script/.github/workflows/main.yml#L40-L60](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L60)

**Example:**
```bash
goeval -i fmt 'fmt.Println("Hello, Goeval!")'
goeval -i fmt -i math 'fmt.Printf("sqrt(16) = %.2f\n", math.Sqrt(16))'
```

### 2.2 yaegi run \<file.go\> (Go Interpreter)
**Method:** [`yaegi`](https://github.com/traefik/yaegi) is a pure-Go interpreter (no compiler step). `yaegi run <file.go>` parses and executes the file directly. Useful for scripting, hot-reload, and embedded Go evaluation. Alternative to `goeval` for whole-file execution.

**Locations:** No checked-in `.go` source yet; pattern documented via the inline demo workflow.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest1_.yml](../.github/workflows/pytest1_.yml#L219-L229) - `go install github.com/traefik/yaegi/cmd/yaegi@latest` then `yaegi run /tmp/hello_yaegi.go`

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
- [java/temporal/edu-101-java-code/exercises/finale-workflow/README.md](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/README.md#L34-L45) - `go run worker/main.go` (terminal 1) + `go run start/main.go "Mason Egger"` (terminal 2)
  - Remote (submodule `java/temporal/edu-101-java-code` @ branch `main`): [exercises/finale-workflow/README.md#L34-L45](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/README.md#L34-L45)
- [java/temporal/edu-101-java-code/exercises/finale-workflow/go/worker/main.go](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/go/worker/main.go) - Worker implementation
  - Remote: [exercises/finale-workflow/go/worker/main.go](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/go/worker/main.go)
- [java/temporal/edu-101-java-code/exercises/finale-workflow/go/start/main.go](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/go/start/main.go) - Starter implementation
  - Remote: [exercises/finale-workflow/go/start/main.go](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/go/start/main.go)
- [java/temporal/edu-101-java-code/exercises/finale-workflow/go/workflow.go](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/go/workflow.go) - Workflow definition
  - Remote: [exercises/finale-workflow/go/workflow.go](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/exercises/finale-workflow/go/workflow.go)
- [java/temporal/edu-101-java-code/.bash.cfg](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.bash.cfg#L33-L37) - Bash aliases `ex4w` / `ex4s`
  - Remote: [.bash.cfg#L33-L37](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.bash.cfg#L33-L37)

**Workflow yml (executes in CI):** No GitHub Actions workflow runs Temporal end-to-end (worker + starter need a live Temporal server). The equivalent automation lives in a Gitpod task instead:
- [java/temporal/edu-101-java-code/.gitpod.yml](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.gitpod.yml#L10-L20) - "Temporal Local Development Server" task spawns `temporal server start-dev` so the worker + starter `go run` commands can connect
  - Remote: [.gitpod.yml#L10-L20](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.gitpod.yml#L10-L20)
- [java/temporal/edu-101-java-code/.gitpod.yml](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.gitpod.yml#L51-L70) - "Temporal server" / additional task entries
  - Remote: [.gitpod.yml#L51-L70](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/.gitpod.yml#L51-L70)

Related root CI that syncs the submodule (but does not run the worker/starter):
- [.github/workflows/main.yml#L193](../.github/workflows/main.yml#L193) — root bulk-sync workflow does `cd java/temporal/edu-101-java-code && git pull` (submodule sync only; no `go run` of the Temporal worker/starter).

Invoked manually via two terminals or the `ex4w` / `ex4s` bash aliases.

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
- [golang/codeforces_script/server_/server_1.go](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/server_/server_1.go#L26) - `http.ListenAndServe(":8080", nil)`
  - Remote (submodule `golang/codeforces_script` @ branch `golang_`): [server_/server_1.go#L26](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/server_/server_1.go#L26)
- [golang/3_go_server/server_2/main.go](../golang/3_go_server/server_2/main.go#L41) - Same pattern
- [golang/readme.txt](../golang/readme.txt#L216-L245) - HTTP server example

**Workflow yml (executes in CI):**
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L78-L100) - End-to-end run: "Run Go Server" backgrounds `server_/server_1.go` (L78-L84) → "Capture HTML content" curls `/`, `/hello`, `/hello2` (L85-L93) → "Take screenshot" / "Record video" Playwright steps (L94-L155) drive the live `ListenAndServe`
  - Remote: [golang/codeforces_script/.github/workflows/main.yml#L78-L100](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L78-L100)

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
- [golang/6_go_movies_crud/readme.md](../golang/6_go_movies_crud/readme.md#L4) - Run instructions
- [golang/6_go_movies_crud_1/readme.md](../golang/6_go_movies_crud_1/readme.md#L4) - Run instructions

**Workflow yml (executes in CI):**
- [.github/workflows/go_movies_crud.yml](../.github/workflows/go_movies_crud.yml#L26-L48) - dedicated pipeline: `go mod tidy` + `go mod download` in `golang/6_go_movies_crud/`, then `nohup go run main.go &` with a 30 s readiness loop polling `:8080/movies`
- [.github/workflows/go_movies_crud.yml](../.github/workflows/go_movies_crud.yml#L50-L85) - exercises all 5 CRUD endpoints with `curl`: `GET /movies`, `GET /movies/1`, `POST /movies`, `PUT /movies/1`, `DELETE /movies/2`, then re-`GET`s to capture final state — each response saved to `crud_out/0N_*.json`
- [.github/workflows/go_movies_crud.yml](../.github/workflows/go_movies_crud.yml#L87-L101) - `jq` assertions: initial length is 2, `GET /movies/1` returns "Movie One", `POST` returns "Movie Three", `PUT` updates title, final length is 2 after DELETE + POST, and `id == "2"` is gone
- [.github/workflows/go_movies_crud.yml](../.github/workflows/go_movies_crud.yml#L103-L122) - teardown: kills the background `go run` PID, tails `server.log`, uploads `crud_out/` as the `go-movies-crud-artifacts` artifact

The pattern is the same `nohup go run … &` + `curl` skeleton as the reference template in [`golang/codeforces_script/.github/workflows/main.yml#L78-L100`](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L78-L100), but retargeted at `6_go_movies_crud/main.go` with `:8080/movies` instead of the single-file `:8080/hello` server, and uses `jq` for assertions instead of Playwright (no value in screenshotting raw JSON responses).

The `6_go_movies_crud_1/main_3.go` variant (same gorilla/mux server, but reached through a Go → Python `os/exec` → goeval polyglot chain) is exercised by the `go` matrix entry of:
- [.github/workflows/go_movies_crud_1.yml](../.github/workflows/go_movies_crud_1.yml#L89-L106) - `nohup go run main_3.go &` then the same 5-endpoint CRUD battery + jq assertions

The other two polyglot variants in `6_go_movies_crud_1/` (Python-driven, Node-driven) wrap the same server through `goeval` and are documented under §6.1 and §6.3 respectively.

**Example:**
```bash
cd golang/6_go_movies_crud
go run main.go &
curl http://localhost:8080/movies
curl -X POST -H "Content-Type: application/json" \
  -d '{"isbn":"99999","title":"Movie Three","director":{"firstname":"Ada","lastname":"Lovelace"}}' \
  http://localhost:8080/movies
```

### 4.3 TCP Socket Server + Client (paired go run)
**Method:** Paired Go programs — one binds a TCP listener via `net.Listen("tcp", ":9988")`, the other dials it via `net.Dial`. Both are launched independently with `go run`. End-to-end execution requires both halves.

**Locations:**
- [golang/5_socket_programming/server.go](../golang/5_socket_programming/server.go), [client.go](../golang/5_socket_programming/client.go) - Primary server/client pair
- [golang/5_socket_programming/server_2.go](../golang/5_socket_programming/server_2.go), [client_2.go](../golang/5_socket_programming/client_2.go) - Variant pair
- [golang/5_socket_programming/readme.md](../golang/5_socket_programming/readme.md#L1-L20) - Run instructions

**Workflow yml (executes in CI):**
- [.github/workflows/go_socket_programming.yml](../.github/workflows/go_socket_programming.yml#L27-L46) - matrix over `pair1` (server.go + client.go on :9988, single request/response) and `pair2` (server_2.go + client_2.go on :8000, streaming numbered lines)
- [.github/workflows/go_socket_programming.yml](../.github/workflows/go_socket_programming.yml#L60-L78) - per matrix entry: `nohup go run <server> &` then 60 s readiness loop using bash's `/dev/tcp/127.0.0.1/<port>` probe (no `nc` needed)
- [.github/workflows/go_socket_programming.yml](../.github/workflows/go_socket_programming.yml#L80-L93) - runs the client; for the streaming pair2 it's wrapped in `timeout --preserve-status -s INT 5 go run client_2.go` so the infinite reconnect loop exits cleanly
- [.github/workflows/go_socket_programming.yml](../.github/workflows/go_socket_programming.yml#L95-L104) - asserts both halves saw the expected exchange via `grep -F`: pair1 checks `Received:  Hello Server! Greetings.` / `Thanks! Got your message:Hello Server! Greetings.`; pair2 checks `data from server` in both logs
- [.github/workflows/go_socket_programming.yml](../.github/workflows/go_socket_programming.yml#L106-L117) - teardown + uploads `socket_out_<pair>/{server,client}.log` as `go-socket-<pair>-artifacts`

The pattern mirrors the reference background-then-foreground skeleton at [`golang/codeforces_script/.github/workflows/main.yml#L78-L84`](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L78-L84) (`nohup go run … &` + `sleep`), but replaces `sleep`/`curl` with `/dev/tcp` (TCP socket, no HTTP) and adds `timeout` for the streaming variant.

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
- [golang/golang_/golang_vercel/api/entrypoint.go](https://github.com/aqwertyuiop48/golang_vercel/blob/main/api/entrypoint.go) - gin-gonic-based handler
  - Remote (submodule `golang/golang_/golang_vercel` @ branch `main`): [api/entrypoint.go](https://github.com/aqwertyuiop48/golang_vercel/blob/main/api/entrypoint.go)
- [golang/golang_/golang_vercel/vercel.json](https://github.com/aqwertyuiop48/golang_vercel/blob/main/vercel.json) - Routes config
  - Remote: [vercel.json](https://github.com/aqwertyuiop48/golang_vercel/blob/main/vercel.json)
- [golang/golang_/golang_vercel/go.mod](https://github.com/aqwertyuiop48/golang_vercel/blob/main/go.mod) - Module declaration
  - Remote: [go.mod](https://github.com/aqwertyuiop48/golang_vercel/blob/main/go.mod)
- [golang/golang_/golang_vercel/readme.md](https://github.com/aqwertyuiop48/golang_vercel/blob/main/readme.md#L11-L13) - Deploy commands
  - Remote: [readme.md#L11-L13](https://github.com/aqwertyuiop48/golang_vercel/blob/main/readme.md#L11-L13)
- [javascript/next_/nextjs_app/api/entrypoint.go](https://github.com/aqwertyuiop48/nextjs_app/blob/main/api/entrypoint.go) - Same pattern under a Next.js project
  - Remote (submodule `javascript/next_/nextjs_app` @ branch `main`): [api/entrypoint.go](https://github.com/aqwertyuiop48/nextjs_app/blob/main/api/entrypoint.go)

**Workflow yml (executes in CI):** None that compiles the Vercel Go function itself — by design, the Go build runs on Vercel's infrastructure after `git push`. Vercel auto-detects `api/*.go`, runs `go build` server-side, and deploys the binary as a serverless function. The deploy is invoked manually via `vercel --prod` (see readme above) or by Vercel's git-push integration.

Related submodule CI that runs adjacent to (but does not exercise) the Go function:
- [javascript/next_/nextjs_app/.github/workflows/main.yml](https://github.com/aqwertyuiop48/nextjs_app/blob/main/.github/workflows/main.yml) — the `nextjs_app` submodule does ship a workflow, but it only does `docker build` of the Next.js image (which packages the JS app, not `api/entrypoint.go`). The Go function stays a Vercel-only deploy artifact.
- [.github/workflows/main.yml#L94](../.github/workflows/main.yml#L94) — root bulk-sync workflow does `cd golang/golang_/golang_vercel && git pull` (submodule sync only; no `go build` invoked).

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

**Workflow yml (executes in CI):**
- [.github/workflows/go_movies_crud_1.yml](../.github/workflows/go_movies_crud_1.yml#L57-L67) - installs `goeval` (clone + `go install .`) and exposes it on PATH — the prerequisite for the whole bridge
- [.github/workflows/go_movies_crud_1.yml](../.github/workflows/go_movies_crud_1.yml#L89-L106) - `python main_2.py` matrix entry: launches the Python script in the background, polls `:8080/movies` up to 60 s (goeval first compiles the embedded snippet), then runs the 5-endpoint CRUD battery (§4.2-style) + jq assertions
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L51) - reference `goeval` preamble that this workflow mirrors
  - Remote: [golang/codeforces_script/.github/workflows/main.yml#L40-L51](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L51)

> The second `main_2.py` listed under Locations (in `golang/3_go_server/server_2/`) follows the same pattern but is not bundled into a workflow — its Go server target is different from the gorilla/mux CRUD server.

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

**Workflow yml (executes in CI):** None for the bridge itself. As with §6.1, the prerequisite `goeval` install + PATH setup that this bridge depends on lives in:
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L51) - Reusable goeval preamble
  - Remote: [golang/codeforces_script/.github/workflows/main.yml#L40-L51](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L40-L51)

**Example:**
```js
const cp = require('child_process');
const goSrc = `
import "fmt"
fmt.Println("hi from nodejs-driven goeval")
`;
cp.execFile("goeval", [goSrc], (e, out) => console.log(out));
```

### 6.3 Node.js child_process.execFile ➜ python -c ➜ subprocess ➜ goeval
**Method:** A three-language chain — Node.js builds a Python snippet (which itself contains a Go snippet as a `subprocess.run(["goeval", ...])` call), then spawns `python -c "<that snippet>"` via `child_process.execFile`. Node receives the goeval-emitted Go stdout transitively, through Python, back through the Node callback. Effectively `node main_2.js` boots the same gorilla/mux REST server that §4.2 runs natively, but through two extra interpreter hops.

**Locations:**
- [golang/6_go_movies_crud_1/main_2.js](../golang/6_go_movies_crud_1/main_2.js#L1-L116) - `runner.execFile("python", ["-c", python_go_main], ...)` where `python_go_main` wraps the gorilla/mux server in `subprocess.run(["goeval", ...])`

**Workflow yml (executes in CI):**
- [.github/workflows/go_movies_crud_1.yml](../.github/workflows/go_movies_crud_1.yml#L57-L67) - shared `goeval` install + PATH setup
- [.github/workflows/go_movies_crud_1.yml](../.github/workflows/go_movies_crud_1.yml#L70-L73) - `npm install sprintf-js` (Node helper dep required by `main_2.js`)
- [.github/workflows/go_movies_crud_1.yml](../.github/workflows/go_movies_crud_1.yml#L89-L106) - `node main_2.js` matrix entry: same background-launch + 60 s readiness poll + 5-endpoint CRUD battery + jq assertions as §6.1

**Example:**
```bash
cd golang/6_go_movies_crud_1
npm install sprintf-js
node main_2.js &
curl http://localhost:8080/movies
```
