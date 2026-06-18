# TypeScript Execution Methods in Programming Languages Repository

This document catalogues **all distinct TypeScript execution methods** discovered for running `.ts` and `.tsx` sources throughout the repository.

Pure JavaScript / Node.js execution methods, JS test runners, JS-only frameworks, and the cross-language `node -e` polyglot patterns are documented separately in [javascript.md](javascript.md).

Each method takes TS source as input and produces program output. Pure type-checking (`tsc --noEmit`), build-only compilation (`tsc`, `tsc --watch` without serve), linters (`eslint --ext .ts`), pure deployment configs (Vercel / Netlify), and `tsconfig.json` declarations are **not** execution methods and are excluded. A method that **transpiles + executes** in one shell invocation (`ts-node`, `tsx`, `bun run`) counts as one method. Framework dev-servers whose source files are `.ts` / `.tsx` are listed here because the user issues a single command and TypeScript is what is being run.

## Table of Contents

1. **Direct TypeScript Runners (transpile + run in one step)**
   - 1.1 [ts-node \<file.ts\> (Inline TS Transpile + Run)](#11-ts-node-filets-inline-ts-transpile--run)
   - 1.2 [tsx \<file.ts\> (esbuild-powered TS runner)](#12-tsx-filets-esbuild-powered-ts-runner)
   - 1.3 [bun run \<file.ts\> / bun run --watch (Bun runtime)](#13-bun-run-filets--bun-run---watch-bun-runtime)
   - 1.4 [ts-node -e "..." (Inline Expression)](#14-ts-node--e--inline-expression)
   - 1.5 [ts-node -p -e "..." (Print Expression Value)](#15-ts-node--p--e--print-expression-value)
   - 1.6 [ts-node <<EOF (Stdin Heredoc)](#16-ts-node-eof-stdin-heredoc)
   - 1.7 [echo '...' | ts-node (Piped REPL Stdin)](#17-echo---ts-node-piped-repl-stdin)
   - 1.8 [bun -e "..." (Inline Expression)](#18-bun--e--inline-expression)
   - 1.9 [bun --print "..." (Print Expression Value)](#19-bun---print--print-expression-value)
   - 1.10 [bun - <<EOF (Stdin Heredoc)](#110-bun---eof-stdin-heredoc)

2. **TypeScript-First Framework Dev Servers**
   - 2.1 [ng serve / ng build / ng test (Angular CLI)](#21-ng-serve--ng-build--ng-test-angular-cli)
   - 2.2 [nest start [--watch] (NestJS)](#22-nest-start---watch-nestjs)
   - 2.3 [vinxi dev / vinxi start (SolidJS Start)](#23-vinxi-dev--vinxi-start-solidjs-start)
   - 2.4 [qwik build + vite --mode ssr (Qwik)](#24-qwik-build--vite---mode-ssr-qwik)
   - 2.5 [ts-node server.ts (AdonisJS-TS entry)](#25-ts-node-serverts-adonisjs-ts-entry)

3. **TypeScript Variants of Cross-Cutting Frameworks**
   - 3.1 [next dev / next build / next start (TypeScript Next.js projects)](#31-next-dev--next-build--next-start-typescript-nextjs-projects)
   - 3.2 [vite (Vite with TypeScript entries)](#32-vite-vite-with-typescript-entries)
   - 3.3 [remix dev (TypeScript Remix projects)](#33-remix-dev-typescript-remix-projects)
   - 3.4 [redwood dev / rw dev (RedwoodJS TypeScript)](#34-redwood-dev--rw-dev-redwoodjs-typescript)

4. **TypeScript Test Runners**
   - 4.1 [jest with ts-jest / Nest preset](#41-jest-with-ts-jest--nest-preset)
   - 4.2 [vitest (TypeScript specs)](#42-vitest-typescript-specs)
   - 4.3 [npx playwright test (TypeScript specs)](#43-npx-playwright-test-typescript-specs)
   - 4.4 [stencil test --spec --e2e (Stencil)](#44-stencil-test---spec---e2e-stencil)

5. **Containerized Execution (TypeScript builds)**
   - 5.1 [Docker multi-stage (tsc / nest build then `node dist/`)](#51-docker-multi-stage-tsc--nest-build-then-node-dist)

6. **Embedded / Polyglot Execution**
   - 6.1 [`child_process.exec("node -e …")` from a .ts file](#61-child_processexecnode--e--from-a-ts-file)

---

## 1. **Direct TypeScript Runners (transpile + run in one step)**

`ts-node`, `tsx`, and `bun` are register-and-run shims that transpile TypeScript in-process and hand the result to the runtime — the user sees a single command that takes `.ts` source and produces program output.

### 1.1 ts-node \<file.ts\> (Inline TS Transpile + Run)
**Method:** `ts-node` registers a TS loader, transpiles `<file.ts>` in memory, and runs it under Node. Used here for both dev (`ts-node-dev`-style start) and production-style starts (no separate `tsc` build step).

**Locations:**
- [typescript/express_/ts_express_vercel_app/package.json](https://github.com/aqwertyuiop48/ts_express_vercel_app/blob/main/package.json#L7) - `"start": "ts-node index.ts"`
  - Remote (submodule `typescript/express_/ts_express_vercel_app` @ branch `main`): [package.json#L7](https://github.com/aqwertyuiop48/ts_express_vercel_app/blob/main/package.json#L7)
- [typescript/koa_/koa_project/package.json](https://github.com/aqwertyuiop48/koa_project/blob/typescript/package.json#L14) - `"start": "ts-node index.ts"`
  - Remote (submodule @ branch `typescript`): [package.json#L14](https://github.com/aqwertyuiop48/koa_project/blob/typescript/package.json#L14)
- [typescript/ts_node_server/ts_node_server/package.json](https://github.com/aqwertyuiop48/ts_node_server/blob/main/package.json#L7) - `"start": "ts-node index.ts"`
  - Remote (submodule @ branch `main`): [package.json#L7](https://github.com/aqwertyuiop48/ts_node_server/blob/main/package.json#L7)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L89-L100) - "ts-node <file.ts> (file run)" step writes `/tmp/hello_tsnode.cts` then runs `ts-node /tmp/hello_tsnode.cts` — direct CI coverage.

**Example:**
```bash
ts-node index.ts
npm start    # if package.json's start is "ts-node index.ts"
```

### 1.2 tsx \<file.ts\> (esbuild-powered TS runner)
**Method:** `tsx` is an esbuild-backed alternative to `ts-node` — single command compiles and runs `.ts` / `.tsx` files under Node.

**Locations:**
- [javascript/saas-microservices/apps/api/package.json](https://github.com/aqwertyuiop48/saas-microservices/blob/main/apps/api/package.json#L10) - `"dev": "tsx src/index.ts"`

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L129-L139) - "tsx <file.ts> (file run)" step writes `/tmp/hello_tsx.ts` then runs `tsx /tmp/hello_tsx.ts` — direct CI coverage.

**Example:**
```bash
tsx src/index.ts
npm run dev
```

### 1.3 bun run \<file.ts\> / bun run --watch (Bun runtime)
**Method:** [Bun](https://bun.sh/) is a JavaScript runtime that natively executes TypeScript (no separate transpile step required) and provides a file-watcher with `--watch`.

**Locations:**
- [javascript/new_frameworks/elysia/package.json](https://github.com/aqwertyuiop48/elysia/blob/main/package.json#L7) - `"dev": "bun run --watch src/index.ts"` (Elysia is a TS-first web framework for Bun)
  - Remote (submodule @ branch `main`): [package.json#L7](https://github.com/aqwertyuiop48/elysia/blob/main/package.json#L7)
- [javascript/turborepo-with-hono/apps/web/README.md](https://github.com/aqwertyuiop48/turborepo-with-hono/blob/main/apps/web/README.md#L14) - `bun dev`
  - Remote (submodule @ branch `main`): [README.md#L14](https://github.com/aqwertyuiop48/turborepo-with-hono/blob/main/apps/web/README.md#L14)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L141-L151) - "bun run <file.ts>" step writes `/tmp/hello_bun.ts` then runs `bun run /tmp/hello_bun.ts` — direct CI coverage.

**Example:**
```bash
bun run src/index.ts
bun run --watch src/index.ts
bun dev
```

### 1.4 ts-node -e "..." (Inline Expression)
**Method:** Evaluate a TypeScript expression supplied as a shell argument — the TS analog of `node -e` ([javascript.md §1.2](javascript.md#12-node--e--inline-expression)). When the surrounding `package.json` declares `"type": "module"` (or under Node 20+ defaults), `ts-node` defaults to ESM and emits `export {};` which fails under `vm`; force CommonJS via `TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}'` and isolate from the workspace tsconfig with `--cwd`.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L102-L105) - `TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' ts-node --cwd /tmp -e '<expr>'`

**Example:**
```bash
TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' \
  ts-node --cwd /tmp -e 'const msg: string = "hi"; console.log(msg);'
```

### 1.5 ts-node -p -e "..." (Print Expression Value)
**Method:** Print the value of a TypeScript expression. ts-node's `-p` flag requires `-e` (unlike `node -p`); together they evaluate the expression and `console.log` the result. Useful for one-liners that emit runtime metadata.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L107-L110) - ``TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' ts-node --cwd /tmp -p -e '`ts-node -p :: ${process.version} on ${process.platform}`' ``

**Example:**
```bash
TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' \
  ts-node --cwd /tmp -p -e '`Node ${process.version} on ${process.platform}`'
```

### 1.6 ts-node <<EOF (Stdin Heredoc)
**Method:** Pipe a TypeScript program into ts-node's stdin via a shell heredoc. ts-node auto-detects piped stdin when no script path is given. Same CommonJS / `--cwd` workarounds as §1.4.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L112-L122) - `TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' ts-node --cwd /tmp <<'EOF' ... EOF`

**Example:**
```bash
TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' \
  ts-node --cwd /tmp <<'EOF'
const nums: number[] = [1, 2, 3, 4, 5];
console.log("Sum: " + nums.reduce((a, b) => a + b, 0));
EOF
```

### 1.7 echo '...' | ts-node (Piped REPL Stdin)
**Method:** One-liner form of §1.6 — any shell command whose stdout is TypeScript source can drive a ts-node run.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L124-L127) - `echo '<ts>' | TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' ts-node --cwd /tmp`

**Example:**
```bash
echo 'const x: number = 42; console.log(x);' \
  | TS_NODE_COMPILER_OPTIONS='{"module":"commonjs"}' ts-node --cwd /tmp
```

### 1.8 bun -e "..." (Inline Expression)
**Method:** Evaluate a TypeScript expression supplied as a shell argument under Bun. Bun parses TypeScript natively so no compiler-option workaround is needed (unlike ts-node).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L153-L155) - `bun -e 'const msg: string = "Hello from bun -e!"; console.log(msg); console.log("Bun version: " + Bun.version);'`

**Example:**
```bash
bun -e 'const msg: string = "hi"; console.log(msg, Bun.version);'
```

### 1.9 bun --print "..." (Print Expression Value)
**Method:** Print the value of a TypeScript expression under Bun — analog of `node -p` but for TS source. Bun does not expose a `-p` short flag; only `--print` works.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L157-L159) - ``bun --print '`bun --print :: Bun ${Bun.version} on ${process.platform}`'``

**Example:**
```bash
bun --print '`Bun ${Bun.version} on ${process.platform}`'
```

### 1.10 bun - <<EOF (Stdin Heredoc)
**Method:** Pass `-` as the script argument so Bun reads program source from stdin, then feed it a shell heredoc. TypeScript-aware sibling of `node - <<EOF` ([javascript.md §1.5](javascript.md#15-node----eof-stdin-heredoc)).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L161-L167) - `bun - <<'EOF' ... const greeting: string = "Hello from bun stdin heredoc!"; ... EOF`

**Example:**
```bash
bun - <<'EOF'
const greeting: string = "hi from bun stdin!";
console.log(greeting, Bun.version);
EOF
```

---

## 2. **TypeScript-First Framework Dev Servers**

These frameworks are TypeScript by convention — their CLI accepts `.ts` source as the canonical input and the dev server handles compilation transparently.

### 2.1 ng serve / ng build / ng test (Angular CLI)
**Method:** Angular CLI dev server. Compiles TypeScript components and serves with live reload. `ng test` launches Karma with TS specs; `ng build` produces a production bundle.

**Locations:**
- [javascript/angular_/my_angular_app/package.json](https://github.com/aqwertyuiop48/my_angular_app/blob/main/package.json#L6-L9) - `"start": "ng serve"`, `"build": "ng build"`, `"test": "ng test"`
  - Remote (submodule @ branch `main`): [package.json#L6-L9](https://github.com/aqwertyuiop48/my_angular_app/blob/main/package.json#L6-L9)
- [javascript/angular_/my_angular_app_main/package.json](https://github.com/aqwertyuiop48/my_angular_app/blob/main_/package.json#L6-L7)
  - Remote (submodule @ branch `main_`): [package.json#L6-L7](https://github.com/aqwertyuiop48/my_angular_app/blob/main_/package.json#L6-L7)
- [javascript/new_frameworks/ionic-angular/package.json](https://github.com/aqwertyuiop48/ionic-angular/blob/main/package.json#L8-L11) - `ng serve` / `ng build` / `ng test` (Ionic + Angular)
- [java/angular_springboot/angular-springboot-crud/crud-angular/package.json](https://github.com/aqwertyuiop48/angular-springboot-crud/blob/main/crud-angular/package.json#L6) - `ng serve` (Angular front-end of a Spring Boot CRUD)
  - Remote (submodule @ branch `main`): [package.json#L6](https://github.com/aqwertyuiop48/angular-springboot-crud/blob/main/crud-angular/package.json#L6)

**Workflow yml (executes in CI):**
- [java/angular_springboot/angular-springboot-crud/.github/workflows/build.yml](https://github.com/aqwertyuiop48/angular-springboot-crud/blob/main/.github/workflows/build.yml#L48-L51) - runs `npm run test:ci` (→ `ng test --no-watch --no-progress --code-coverage --browsers=ChromeHeadless`) and `npm run build` (→ `ng build`) — transitive coverage via npm scripts in [crud-angular/package.json#L7-L10](https://github.com/aqwertyuiop48/angular-springboot-crud/blob/main/crud-angular/package.json#L7-L10).

**Example:**
```bash
ng serve
ng build
ng test --browsers=ChromeHeadless
```

### 2.2 nest start [--watch] (NestJS)
**Method:** NestJS is a TypeScript-first Node back-end framework. `nest start` launches the compiled app; `--watch` adds incremental rebuild + restart on save; `--debug --watch` adds the Node inspector.

**Locations:**
- [javascript/nest_/nestjs_app/package.json](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L12-L15) - `"start": "nest start"`, `"start:dev": "nest start --watch"`, `"start:debug": "nest start --debug --watch"`
  - Remote (submodule @ branch `main`): [package.json#L12-L15](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L12-L15)
- [typescript/nest_/nestjs_js/package.json](https://github.com/aqwertyuiop48/nestjs_js/blob/main/package.json#L12) - `"start": "nest start"`
  - Remote (submodule @ branch `main`): [package.json#L12](https://github.com/aqwertyuiop48/nestjs_js/blob/main/package.json#L12)
- [typescript/nest_/nestjs_tsx/package.json](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/package.json#L13)
  - Remote (submodule @ branch `main`): [package.json#L13](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/package.json#L13)

**Workflow yml (executes in CI):**
- [typescript/nest_/nestjs_tsx/.github/workflows/deploy.yml](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/.github/workflows/deploy.yml#L31) - `npm run build` resolves via [package.json#L10](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/package.json#L10) to `nest build` — transitive coverage via npm script (production-side of the same Nest CLI).
  - Remote (submodule @ branch `main`): [.github/workflows/deploy.yml#L31](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/.github/workflows/deploy.yml#L31)

**Example:**
```bash
nest start
nest start --watch
nest start --debug --watch
```

### 2.3 vinxi dev / vinxi start (SolidJS Start)
**Method:** [Vinxi](https://vinxi.vercel.app/) is the universal app framework that powers SolidJS Start — `vinxi dev` boots an HMR dev server from TypeScript / TSX entry files.

**Locations:**
- [typescript/solid_/solid_app/package.json](https://github.com/aqwertyuiop48/solid_app/blob/main/package.json#L5-L7) - `"dev": "vinxi dev"`, `"build": "vinxi build"`, `"start": "vinxi start"`
  - Remote (submodule @ branch `main`): [package.json#L5-L7](https://github.com/aqwertyuiop48/solid_app/blob/main/package.json#L5-L7)
- [typescript/solid_/solid_ts/solid_app/package.json](https://github.com/aqwertyuiop48/solid_app/blob/typescript/package.json) - SolidJS-TS variant
  - Remote (submodule @ branch `typescript`): [package.json](https://github.com/aqwertyuiop48/solid_app/blob/typescript/package.json)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `typescript/solid_/solid_app/package.json`; `typescript/solid_/solid_ts/solid_app/package.json`

**Example:**
```bash
vinxi dev
vinxi build && vinxi start
```

### 2.4 qwik build + vite --mode ssr (Qwik)
**Method:** Qwik is a resumable framework whose dev mode runs Vite in SSR mode (`vite --mode ssr`) over TSX source; `qwik build` orchestrates the multi-stage build (`build.client`, `build.server`, `build.preview`) for production.

**Locations:**
- [typescript/qwik_/qwik-app/package.json](https://github.com/aqwertyuiop48/qwik-app/blob/main/package.json#L7-L16) - `"build": "qwik build"`, `"dev": "vite --mode ssr"`, `"start": "vite --open --mode ssr"`, `"preview": "qwik build preview && vite preview --open"`
  - Remote (submodule @ branch `main`): [package.json#L7-L16](https://github.com/aqwertyuiop48/qwik-app/blob/main/package.json#L7-L16)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `typescript/qwik_/qwik-app/package.json`

**Example:**
```bash
vite --mode ssr             # dev (SSR)
qwik build                  # production build
qwik build preview && vite preview --open
```

### 2.5 ts-node server.ts (AdonisJS-TS entry)
**Method:** The TypeScript Adonis variants in this repo boot via `ts-node server.ts` (alternatively a precompiled `node server.js`). Documented separately from the generic `ts-node` entry because the framework structure (`app/`, `config/`, `bootstrap/`, `.env`) is what produces the running HTTP server.

**Locations:**
- [typescript/adonis_/serverless-adonis/package.json](https://github.com/aqwertyuiop48/serverless-adonis/blob/typescript/package.json#L9) - `"serve": "node server.js"` (post-build entry; sources are TS)
  - Remote (submodule @ branch `typescript`): [package.json#L9](https://github.com/aqwertyuiop48/serverless-adonis/blob/typescript/package.json#L9)
- [typescript/adonis_/serverless-adonis-ts/package.json](https://github.com/aqwertyuiop48/serverless-adonis-ts/blob/main/package.json#L10) - `"serve": "node server.js"` (TS-typed Adonis project)
  - Remote (submodule @ branch `typescript`): [package.json#L10](https://github.com/aqwertyuiop48/serverless-adonis-ts/blob/typescript/package.json#L10)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `typescript/adonis_/serverless-adonis-ts/package.json`; `typescript/adonis_/serverless-adonis/package.json`

**Example:**
```bash
ts-node server.ts
# or after build:
node server.js
```

---

## 3. **TypeScript Variants of Cross-Cutting Frameworks**

Frameworks listed in [javascript.md](javascript.md#3-framework-dev-servers-single-command-source--running-app) (Next.js, Vite, Remix, Redwood, etc.) also have TypeScript projects in this repo. The CLI command is identical; the only difference is that the source tree is `.ts` / `.tsx`.

### 3.1 next dev / next build / next start (TypeScript Next.js projects)
**Method:** Same `next` CLI as the JS docs — these are the TS-source Next.js apps.

**Locations:**
- [javascript/next_/nextjs_app_typescript/package.json](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/package.json) - TS-source Next.js app
  - Remote (submodule @ branch `typescript`): [package.json](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/package.json)
- [javascript/next_/nextjs_news_search_microservices/package.json](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/package.json#L7-L9) - `"dev": "next dev --turbopack"`, `"start": "next start -p 8080"` (TS sources)
- [javascript/new_frameworks/storybook/package.json](https://github.com/aqwertyuiop48/storybook/blob/main/package.json#L4-L6) - `"dev": "next dev"`, `"build": "next build"`, `"start": "next start"` (TS deps incl. `typescript`)
  - Remote (submodule @ branch `main`): [package.json#L4-L6](https://github.com/aqwertyuiop48/storybook/blob/main/package.json#L4-L6)

**Workflow yml (executes in CI):**
- [javascript/next_/nextjs_news_search_microservices/.github/workflows/ci.yml](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/.github/workflows/ci.yml#L30) - `npm run build` resolves via [package.json#L8](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/package.json#L8) to `next build` — transitive coverage of the TypeScript Next.js pipeline (TS sources confirmed via [tsconfig.json](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/tsconfig.json) and `*.tsx` app routes).

**Example:**
```bash
next dev
next build && next start
```

### 3.2 vite (Vite with TypeScript entries)
**Method:** Vite natively understands `.ts` / `.tsx` entry files — `vite` (no args) starts the dev server with HMR; `vite preview` serves the build.

**Locations:**
- [typescript/qwik_/qwik-app/package.json](https://github.com/aqwertyuiop48/qwik-app/blob/main/package.json#L15) - `"dev": "vite --mode ssr"` (TSX entries)
- [javascript/new_frameworks/tanstack-start/package.json](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L6-L8) - `vite dev --port 3000`, `vite preview` (TS sources)
- [javascript/new_frameworks/hydrogen/package.json](https://github.com/aqwertyuiop48/hydrogen/blob/main/package.json#L13) - `"test": "WATCH=true vitest"` (TS dep on Vite + Vitest)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/hydrogen/package.json`; `javascript/new_frameworks/tanstack-start/package.json`; `typescript/qwik_/qwik-app/package.json`

**Example:**
```bash
vite
vite preview
```

### 3.3 remix dev (TypeScript Remix projects)
**Method:** Remix CLI dev server applied to a TS source tree.

**Locations:**
- [typescript/redwood_/netlify-deploy/README.md](https://github.com/aqwertyuiop48/netlify-deploy/blob/typescript/README.md#L20) - `yarn redwood dev` (Redwood TS variant uses the same `remix`-style dev pattern internally)
  - Remote (submodule @ branch `typescript`): [README.md#L20](https://github.com/aqwertyuiop48/netlify-deploy/blob/typescript/README.md#L20)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `typescript/redwood_/netlify-deploy/README.md`

**Example:**
```bash
remix dev
```

### 3.4 redwood dev / rw dev (RedwoodJS TypeScript)
**Method:** Redwood TS variant — same `yarn redwood dev` / `yarn rw test` CLI; sources are `.ts` / `.tsx`.

**Locations:**
- [typescript/redwood_/netlify-deploy/README.md](https://github.com/aqwertyuiop48/netlify-deploy/blob/typescript/README.md#L20)
  - Remote (submodule @ branch `typescript`): [README.md#L20](https://github.com/aqwertyuiop48/netlify-deploy/blob/typescript/README.md#L20)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `typescript/redwood_/netlify-deploy/README.md`

**Example:**
```bash
yarn redwood dev
yarn rw test
```

---

## 4. **TypeScript Test Runners**

### 4.1 jest with ts-jest / Nest preset
**Method:** `jest` is invoked the same way as for JS, but configured to load TypeScript via `ts-jest` or via NestJS's `@nestjs/testing` Jest preset (which discovers `*.spec.ts`).

**Locations:**
- [javascript/nest_/nestjs_app/package.json](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L18-L24) - `"test": "jest"`, `"test:watch": "jest --watch"`, `"test:cov": "jest --coverage"`, `"test:e2e": "jest --config ./test/jest-e2e.json"` (configured for TS)
  - Remote (submodule @ branch `main`): [package.json#L18-L24](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L18-L24)
- [typescript/nest_/nestjs_js/package.json](https://github.com/aqwertyuiop48/nestjs_js/blob/main/package.json#L17) - `"test": "jest"` (TS specs)
- [typescript/nest_/nestjs_tsx/package.json](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/package.json#L18) - `"test": "jest"`

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/nest_/nestjs_app/package.json`; `typescript/nest_/nestjs_js/package.json`; `typescript/nest_/nestjs_tsx/package.json`
- [javascript/nest_/nestjs_app/.github/workflows/deploy-to-vercel.yml](https://github.com/aqwertyuiop48/nestjs_app/blob/main/.github/workflows/deploy-to-vercel.yml) _(rule R1)_ — covers `javascript/nest_/nestjs_app/package.json`
- [typescript/nest_/nestjs_tsx/.github/workflows/deploy.yml](https://github.com/aqwertyuiop48/nestjs_tsx/blob/main/.github/workflows/deploy.yml) _(rule R1)_ — covers `typescript/nest_/nestjs_tsx/package.json`

**Example:**
```bash
jest                                # uses ts-jest config
jest --config ./test/jest-e2e.json
```

### 4.2 vitest (TypeScript specs)
**Method:** Vitest natively understands TypeScript via Vite — no extra config needed for `.test.ts` / `.spec.ts`.

**Locations:**
- [javascript/new_frameworks/tanstack-start/package.json](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L9) - `"test": "vitest run"`
- [javascript/new_frameworks/hydrogen/package.json](https://github.com/aqwertyuiop48/hydrogen/blob/main/package.json#L13) - `"test": "WATCH=true vitest"`

**Workflow yml (executes in CI):**
- [javascript/next_/nextjs_news_search_microservices/.github/workflows/ci.yml](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/.github/workflows/ci.yml#L37) - `npx vitest run --coverage` — direct CI coverage over TS specs at [tests/lib/nyt.test.ts](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/tests/lib/nyt.test.ts), [tests/lib/guardian.test.ts](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/tests/lib/guardian.test.ts), and [tests/api/search.test.ts](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/tests/api/search.test.ts).

**Example:**
```bash
vitest
vitest run --coverage
```

### 4.3 npx playwright test (TypeScript specs)
**Method:** Playwright's TS template runs `*.spec.ts` directly via its built-in TS pipeline.

**Locations:**
- Implied by Playwright projects that produce `.spec.ts` artifacts; primary invocation pattern is identical to JS:
  - [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L27) - `npx playwright install` (`npx playwright test` over TS specs follows the same pattern)
    - Remote (submodule @ branch `golang_`): [main.yml#L27](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L27)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `golang/codeforces_script/.github/workflows/main.yml`
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml) _(rule R1)_ — covers `golang/codeforces_script/.github/workflows/main.yml`

**Example:**
```bash
npx playwright install
npx playwright test
```

### 4.4 stencil test --spec --e2e (Stencil)
**Method:** Stencil's TypeScript-first compiler runs `*.spec.ts` (unit) and `*.e2e.ts` (Puppeteer-driven E2E) in one CLI call.

**Locations:**
- [javascript/stencil_/stencil/package.json](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L9) - `"test": "stencil test --spec --e2e"`
  - Remote (submodule @ branch `main`): [package.json#L9](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L9)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/stencil_/stencil/package.json`

**Example:**
```bash
stencil test --spec --e2e
```

---

## 5. **Containerized Execution (TypeScript builds)**

### 5.1 Docker multi-stage (tsc / nest build then `node dist/`)
**Method:** Multi-stage `Dockerfile` — the **build** stage runs `npm install` + `tsc` (or `nest build` / `next build`); the **runtime** stage copies `dist/` and starts with `node dist/main.js`. The `docker build … && docker run …` pipeline takes TS source and produces a running container.

**Locations:**
- [javascript/next_/nextjs_app_typescript/Dockerfile](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/Dockerfile#L2-L17) - `FROM node:22-alpine AS build` … `FROM node:22-alpine` (TS Next.js multi-stage)
  - Remote (submodule @ branch `typescript`): [Dockerfile#L2-L17](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/Dockerfile#L2-L17)
- [javascript/angular_/my_angular_app/Dockerfile](https://github.com/aqwertyuiop48/my_angular_app/blob/main/Dockerfile#L4-L19) - `FROM node:22-alpine AS build-stage` + `RUN npm run build` (Angular TS → static files)
  - Remote (submodule @ branch `main`): [Dockerfile#L4-L19](https://github.com/aqwertyuiop48/my_angular_app/blob/main/Dockerfile#L4-L19)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/angular_/my_angular_app/Dockerfile`; `javascript/next_/nextjs_app_typescript/Dockerfile`
- [javascript/angular_/my_angular_app/.github/workflows/main.yml](https://github.com/aqwertyuiop48/my_angular_app/blob/main/.github/workflows/main.yml) _(rule R1)_ — covers `javascript/angular_/my_angular_app/Dockerfile`
- [javascript/next_/nextjs_app_typescript/.github/workflows/main.yml](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/.github/workflows/main.yml) _(rule R1)_ — covers `javascript/next_/nextjs_app_typescript/Dockerfile`

**Example:**
```dockerfile
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json tsconfig.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:22-alpine
WORKDIR /app
COPY --from=build /app/dist ./dist
COPY --from=build /app/node_modules ./node_modules
CMD ["node", "dist/main.js"]
```

---

## 6. **Embedded / Polyglot Execution**

### 6.1 `child_process.exec("node -e …")` from a .ts file
**Method:** A TypeScript file uses Node's `child_process` API to spawn a fresh `node` subprocess. The parent's source is `.ts`, so the entry point goes through a TS runner (`ts-node`, `tsx`) before reaching the spawned `node -e` polyglot bridge.

**Locations:**
- [typescript/inputs/nested_child_process.ts](../typescript/inputs/nested_child_process.ts#L3) - `exec(\`node -e "console.log(2)"\`)` from TypeScript

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L319-L326) - writes a minimal `/tmp/ts_cp_exec.ts` whose body is `import { exec } from 'child_process'; exec(\`node -e "console.log(2 + 3)"\`, (_e, stdout) => process.stdout.write(stdout));`, then `ts-node /tmp/ts_cp_exec.ts`, asserted with `grep -F '5'`. `ts-node` and `typescript` are installed globally earlier in the job (`npm install -g ts-node typescript tsx`), and the spawned `node -e` uses the same Node runtime — no extra setup needed. The repo's own [typescript/inputs/nested_child_process.ts](../typescript/inputs/nested_child_process.ts) (and the sibling `_helper.ts`) carry hardcoded `/Users/sreedhar.k/.nvm/versions/node/v16.10.0/...` macOS paths in their `tsc --typeRoots …` invocations and so can't be run directly in Linux CI, but the §6.1 method itself — `.ts` source → `ts-node` → `child_process.exec("node -e …")` — is exercised end-to-end.

**Example:**
```ts
import { exec } from 'child_process';
exec('node -e "console.log(2 + 3)"', (_err, stdout) => console.log(stdout));
```

> The receiving side of this bridge (the `node -e` polyglot pattern across Go / Java / Ruby / Objective-C / C++) is documented in [javascript.md §9.3](javascript.md#93-go--java--ruby--objective-c--c--node--e-subprocess).

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `ts-node <file.ts>` | TS transpile-and-run under Node | [typescript/express_/ts_express_vercel_app/package.json](https://github.com/aqwertyuiop48/ts_express_vercel_app/blob/main/package.json#L7)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/ts_express_vercel_app/blob/main/package.json#L7) |
| `tsx <file.ts>` | esbuild-powered TS runner | [javascript/saas-microservices/apps/api/package.json](https://github.com/aqwertyuiop48/saas-microservices/blob/main/apps/api/package.json#L10) |
| `bun run --watch <file.ts>` | Bun runtime (native TS) | [javascript/new_frameworks/elysia/package.json](https://github.com/aqwertyuiop48/elysia/blob/main/package.json#L7)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/elysia/blob/main/package.json#L7) |
| `ng serve` / `ng build` / `ng test` | Angular CLI | [javascript/angular_/my_angular_app/package.json](https://github.com/aqwertyuiop48/my_angular_app/blob/main/package.json#L6)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/my_angular_app/blob/main/package.json#L6) |
| `nest start [--watch]` | NestJS | [javascript/nest_/nestjs_app/package.json](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L13)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L13) |
| `vinxi dev` | SolidJS Start | [typescript/solid_/solid_app/package.json](https://github.com/aqwertyuiop48/solid_app/blob/main/package.json#L5)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/solid_app/blob/main/package.json#L5) |
| `qwik build` + `vite --mode ssr` | Qwik | [typescript/qwik_/qwik-app/package.json](https://github.com/aqwertyuiop48/qwik-app/blob/main/package.json#L15)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/qwik-app/blob/main/package.json#L15) |
| `ts-node server.ts` (Adonis-TS) | AdonisJS TS variants | [typescript/adonis_/serverless-adonis-ts/package.json](https://github.com/aqwertyuiop48/serverless-adonis-ts/blob/main/package.json#L10)<br/>[remote @ `typescript`](https://github.com/aqwertyuiop48/serverless-adonis-ts/blob/typescript/package.json#L10) |
| `next dev` / `next build` / `next start` | TS Next.js apps | [javascript/next_/nextjs_app_typescript/package.json](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/package.json)<br/>[remote @ `typescript`](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/package.json) |
| `vite` / `vite preview` | TS Vite projects | [typescript/qwik_/qwik-app/package.json](https://github.com/aqwertyuiop48/qwik-app/blob/main/package.json#L15) |
| `redwood dev` / `rw dev` | RedwoodJS TS | [typescript/redwood_/netlify-deploy/README.md](https://github.com/aqwertyuiop48/netlify-deploy/blob/typescript/README.md#L20)<br/>[remote @ `typescript`](https://github.com/aqwertyuiop48/netlify-deploy/blob/typescript/README.md#L20) |
| `jest` (ts-jest / Nest preset) | TS unit + E2E tests | [javascript/nest_/nestjs_app/package.json](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L18)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/nestjs_app/blob/main/package.json#L18) |
| `vitest [run]` | TS specs via Vite | [javascript/new_frameworks/tanstack-start/package.json](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L9)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L9) |
| `npx playwright test` | TS browser specs | [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L27)<br/>[remote @ `golang_`](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L27) |
| `stencil test --spec --e2e` | Stencil TS tests | [javascript/stencil_/stencil/package.json](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L9)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L9) |
| Docker multi-stage (tsc + `node dist/`) | Containerized TS | [javascript/next_/nextjs_app_typescript/Dockerfile](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/Dockerfile#L2)<br/>[remote @ `typescript`](https://github.com/aqwertyuiop48/nextjs_app/blob/typescript/Dockerfile#L2) |
| `child_process.exec("node -e …")` from .ts | TS → Node subprocess polyglot | [typescript/inputs/nested_child_process.ts](../typescript/inputs/nested_child_process.ts#L3) |

---

## Key Frameworks & Tools Integrated

- **Angular** — TypeScript-first front-end framework (`ng serve` / `ng build` / `ng test`)
- **NestJS** — TypeScript-first Node back-end (`nest start [--watch]`)
- **SolidJS / Vinxi** — fine-grained reactive framework with the Vinxi metaframework (`vinxi dev`)
- **Qwik** — resumable framework on Vite SSR (`vite --mode ssr` + `qwik build`)
- **AdonisJS-TS** — TypeScript variant of the Adonis MVC framework
- **Elysia** — Bun-native TS web framework (`bun run --watch`)
- **Stencil** — TypeScript-first Web Components compiler (`stencil test --spec --e2e`)
- **Next.js / Vite / Remix / Redwood / Hydrogen / TanStack Start** — cross-cutting frameworks with TS source trees in this repo
- **ts-node / tsx / bun** — direct TypeScript runners (no separate build step)
- **Jest + ts-jest / Vitest / Playwright** — TypeScript test runners

> JavaScript / Node.js execution methods, JS-only frameworks, and cross-language `node -e` polyglot patterns are documented in [javascript.md](javascript.md).

---

**Last Updated:** June 9, 2026
**Repository:** /workspaces/programming_languages
