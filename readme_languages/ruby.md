# Ruby Execution Methods in Programming Languages Repository

This document catalogues **all distinct Ruby methods** discovered for running Ruby code throughout the repository. Ruby is interpreted; the main entry points are the `ruby` CLI for scripts/expressions, a TCP HTTP server (`server_.rb`), and a Dockerised packaging of the same server.

## Table of Contents

1. **Direct File Execution**
   - 1.1 [ruby \<file.rb\> (Direct Interpreter Invocation)](#11-ruby-filerb-direct-interpreter-invocation)

2. **Long-Running Servers**
   - 2.1 [ruby server_.rb (TCPServer HTTP Server)](#21-ruby-server_rb-tcpserver-http-server)

3. **Container / Image Workflows**
   - 3.1 [docker build / docker push (Ruby HTTP Server Image)](#31-docker-build--docker-push-ruby-http-server-image)

4. **Polyglot Embedding (Ruby ➜ Other)**
   - 4.1 [fork + exec "node -e …" (Ruby ➜ Node.js)](#41-fork--exec-node--e--ruby--nodejs)

5. **Static Site Generation**
   - 5.1 [bundle exec jekyll build / serve (Jekyll site)](#51-bundle-exec-jekyll-build--serve-jekyll-site)

6. **Inline / One-Liners**
   - 6.1 [ruby -e "..." (Inline Expression)](#61-ruby--e--inline-expression)
   - 6.2 [ruby -n -e "..." (Sed-Like Loop Over stdin)](#62-ruby--n--e--sed-like-loop-over-stdin)
   - 6.3 [irb <<EOF (Interactive REPL via Heredoc)](#63-irb-eof-interactive-repl-via-heredoc)
   - 6.4 [ruby -c \<file.rb\> (Syntax Check Only)](#64-ruby--c-filerb-syntax-check-only)

---

## 1. **Direct File Execution**

### 1.1 ruby \<file.rb\> (Direct Interpreter Invocation)
**Method:** Invoke the Ruby interpreter on a `.rb` source file. The CI iterates the `execute/` directory and runs every Ruby source it finds.

**Locations:**
- [ruby/codeforces_script/execute/hello.rb](../ruby/codeforces_script/execute/hello.rb) - `puts 'Hello, Ruby World!'`
- [ruby/codeforces_script/execute/hello_1.rb](../ruby/codeforces_script/execute/hello_1.rb) - Multi-line strings via `<<~HEREDOC`
- [ruby/codeforces_script/execute/child.rb](../ruby/codeforces_script/execute/child.rb) - `fork + exec` → Node.js (see §4.1)
  - Remote (submodule `ruby/codeforces_script` @ branch `ruby_`): [execute/](https://github.com/aqwertyuiop48/codeforces_script/tree/ruby_/execute)

**Workflow yml (executes in CI):**
- [ruby/codeforces_script/.github/workflows/main.yml](../ruby/codeforces_script/.github/workflows/main.yml#L19-L22) - sets up Ruby 3.3.5 via `ruby/setup-ruby@v1`
- [ruby/codeforces_script/.github/workflows/main.yml](../ruby/codeforces_script/.github/workflows/main.yml#L29-L33) - `for file in execute/*.rb; do ruby "$file"; done`
  - Remote: [main.yml#L29-L33](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/.github/workflows/main.yml#L29-L33)

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L122) — submodule sync that triggers the `ruby_` branch run

**Example:**
```bash
ruby execute/hello.rb
```

---

## 2. **Long-Running Servers**

### 2.1 ruby server_.rb (TCPServer HTTP Server)
**Method:** A vanilla `TCPServer`-based HTTP server written in Ruby. Listens on `ENV.fetch("PORT", 5678)` and responds with `HTTP/1.1 200 OK` + an HTML body for every request. Handles `HEAD` specially (no body) and recovers from `Errno::EPIPE`.

**Locations:**
- [ruby/codeforces_script/server_.rb](../ruby/codeforces_script/server_.rb) - `TCPServer.new port`, accept-loop, response writer
  - Remote: [server_.rb](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/server_.rb)

**Workflow yml (executes in CI):**
- [ruby/codeforces_script/Dockerfile](../ruby/codeforces_script/Dockerfile) - `CMD ["ruby", "server_.rb"]` (exposes 10000)
- Container is built and pushed in CI — see §3.1; the server is the container's entry point.

**Example:**
```bash
PORT=5678 ruby server_.rb
# in another terminal:
curl -i http://localhost:5678/
```

---

## 3. **Container / Image Workflows**

### 3.1 docker build / docker push (Ruby HTTP Server Image)
**Method:** Build a `ruby:3.3` Docker image whose entry point is `ruby server_.rb`, tag it, and push to Docker Hub. Lets the §2.1 server run anywhere Docker is available.

**Locations:**
- [ruby/codeforces_script/Dockerfile](../ruby/codeforces_script/Dockerfile) - `FROM ruby:3.3`, copies sources, `EXPOSE 10000`, `CMD ["ruby", "server_.rb"]`

**Workflow yml (executes in CI):**
- [ruby/codeforces_script/.github/workflows/main.yml](../ruby/codeforces_script/.github/workflows/main.yml#L44-L47) - `docker/login-action@v3` (DockerHub credentials)
- [ruby/codeforces_script/.github/workflows/main.yml](../ruby/codeforces_script/.github/workflows/main.yml#L50-L54) - `docker build -t ruby-http-server:latest .` + `docker tag … sreedockerhub19/ruby-http-server:latest` + `docker push sreedockerhub19/ruby-http-server:latest`

**Example:**
```bash
docker build -t ruby-http-server:latest .
docker run --rm -p 5678:5678 ruby-http-server:latest
```

---

## 4. **Polyglot Embedding (Ruby ➜ Other)**

### 4.1 fork + exec "node -e …" (Ruby ➜ Node.js)
**Method:** Ruby `fork { exec(...) }` spawns a child process that replaces itself with a `node -e "<js code>"` invocation. The Ruby parent runs concurrently while Node executes the inline JavaScript. Same family of patterns as the `Go / Java / Ruby / Objective-C / C++ → node -e` chain documented in [javascript.md §9.3](javascript.md#93-go--java--ruby--objective-c--c--node--e-subprocess).

**Locations:**
- [ruby/codeforces_script/execute/child.rb](../ruby/codeforces_script/execute/child.rb#L1-L7) - `fork do; exec <<~CMD\n  node -e "console.log('Hi from nested nodejs!');"\nCMD\nend`

**Workflow yml (executes in CI):**
- [ruby/codeforces_script/.github/workflows/main.yml](../ruby/codeforces_script/.github/workflows/main.yml#L29-L33) - the `for file in execute/*.rb` loop picks up `child.rb` and runs it; the workflow also sets up Node.js 18 at [main.yml#L37-L40](../ruby/codeforces_script/.github/workflows/main.yml#L37-L40) so the `node -e` subprocess resolves.

**Example:**
```ruby
fork do
  exec <<~CMD
    node -e "console.log('Hi from nested nodejs!');"
  CMD
end
```

---

## 5. **Static Site Generation**

### 5.1 bundle exec jekyll build / serve (Jekyll site)
**Method:** The `ruby/jekyll1` submodule is a Jekyll 4.3.x site (Ruby gem). `bundle exec jekyll build` produces a static `_site/` tree; `bundle exec jekyll serve` runs the local preview server. Scaffolded originally with `jekyll new my-blog`.

**Locations:**
- [ruby/jekyll1/Gemfile](../ruby/jekyll1/Gemfile) - declares `jekyll ~> 4.3.0`, `minima ~> 2.5`, `jekyll-feed ~> 0.12`, `webrick`
- [ruby/jekyll1/_config.yml](../ruby/jekyll1/_config.yml) - site config (theme: `minima`, plugins, etc.)
- [ruby/jekyll1/index.md](../ruby/jekyll1/index.md), [ruby/jekyll1/about.md](../ruby/jekyll1/about.md), [ruby/jekyll1/404.html](../ruby/jekyll1/404.html), [ruby/jekyll1/_posts/](../ruby/jekyll1/_posts/) - content
  - Remote (submodule `ruby/jekyll1` @ branch `main`): [jekyll1](https://github.com/aqwertyuiop48/jekyll1)

**Workflow yml (executes in CI):** None — the `ruby/jekyll1` submodule has no `.github/workflows/` directory. Build is intended to be triggered externally (e.g., Vercel zero-config Jekyll deploy mentioned in the submodule's `README.md`).

Transitively referenced (sync only) by:

- [.github/workflows/main.yml](../.github/workflows/main.yml#L155) — submodule sync of `ruby/jekyll1` @ branch `main`

**Example:**
```bash
bundle install
bundle exec jekyll build         # → _site/
bundle exec jekyll serve         # http://127.0.0.1:4000/
```

---

## 6. **Inline / One-Liners**

### 6.1 ruby -e "..." (Inline Expression)
**Method:** `-e` evaluates its quoted string as Ruby source. Ruby analog of `python -c` / `node -e`. No file required.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `ruby -e 'puts "Hello from ruby -e!"; puts "Ruby: #{RUBY_VERSION}"'`

**Example:**
```bash
ruby -e 'puts "Hello from ruby -e!"'
```

### 6.2 ruby -n -e "..." (Sed-Like Loop Over stdin)
**Method:** `-n` wraps `-e`'s code in an implicit `while gets; … end` loop, exposing the current line as `$_`. With `-p` instead, it also prints `$_` after each iteration. Canonical Ruby one-liner mode (analogous to `awk` / `perl -ne`).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `printf 'line1\nline2\nline3\n' | ruby -n -e 'puts "got: #{$_.chomp}"'`

**Example:**
```bash
printf 'a\nb\nc\n' | ruby -n -e 'puts "got: #{$_.chomp}"'
```

### 6.3 irb <<EOF (Interactive REPL via Heredoc)
**Method:** `irb` is Ruby's interactive REPL. Feeding it a bash heredoc lets you script multi-line Ruby interactively in CI logs (preserving prompt-by-prompt output).

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml)

**Example:**
```bash
irb --noprompt --noecho <<'EOF'
puts "Hello from irb heredoc!"
puts "Ruby: #{RUBY_VERSION}"
exit
EOF
```

### 6.4 ruby -c \<file.rb\> (Syntax Check Only)
**Method:** `-c` parses the file and reports `Syntax OK` (or a parse error) without executing it. Equivalent to `node --check` or `python -m py_compile`. Useful in pre-commit / lint stages.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml)

**Example:**
```bash
ruby -c some_file.rb
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `ruby <file.rb>` | Run a Ruby script file | [hello.rb](../ruby/codeforces_script/execute/hello.rb) |
| `ruby server_.rb` | Long-running TCP HTTP server | [server_.rb](../ruby/codeforces_script/server_.rb) |
| `docker build / push` (Ruby image) | Containerise the Ruby HTTP server | [Dockerfile](../ruby/codeforces_script/Dockerfile) |
| `fork + exec "node -e …"` | Ruby → Node.js polyglot | [child.rb](../ruby/codeforces_script/execute/child.rb) |
| `bundle exec jekyll build / serve` | Jekyll static-site build / preview | [Gemfile](../ruby/jekyll1/Gemfile) |
| `ruby -e "<ruby expr>"` | Inline Ruby expression | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `ruby -n -e "<expr>"` | Sed-like loop over stdin | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `irb <<EOF … EOF` | Interactive REPL via heredoc | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
| `ruby -c <file.rb>` | Syntax check only, no execution | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
