# JavaScript / Node.js Execution Methods in Programming Languages Repository

This document catalogues **all distinct JavaScript / Node.js execution methods** discovered for running `.js`, `.mjs`, `.cjs`, and `.jsx` sources throughout the repository.

TypeScript runners and TypeScript-first frameworks (Angular, NestJS, Qwik, SolidJS, `ts-node`, `tsx`, `bun`, etc.) are documented separately in [typescript.md](typescript.md).

Each method takes JS source as input and produces program output. Toolchain provisioning (`actions/setup-node`, `nvm install`), pure dependency installation (`npm install`, `yarn install`), build-only steps (`tsc`, `webpack`, `rollup -c` *without* watch / serve), linters (`eslint`, `prettier`), pure deployment configs (Vercel / Netlify), and `package-lock.json` / `yarn.lock` declarations (which only declare an installed binary, not an execution) are **not** execution methods and are excluded. A single command that builds **and** serves (e.g. `vite`, `brunch watch --server`, `stencil build --dev --watch --serve`, `rollup -c -w`) counts as one method.

## Table of Contents

1. **Direct Node.js Execution**
   - 1.1 [node \<file.js\> (Node interpreter)](#11-node-filejs-node-interpreter)
   - 1.2 [node -e "..." (Inline Expression)](#12-node--e--inline-expression)
   - 1.3 [node -e with shell heredoc (Multi-line Inline)](#13-node--e-with-shell-heredoc-multi-line-inline)
   - 1.4 [node -p "..." (Print Expression Value)](#14-node--p--print-expression-value)
   - 1.5 [node - <<EOF (Stdin Heredoc)](#15-node---eof-stdin-heredoc)
   - 1.6 [echo '...' | node - (Piped REPL Stdin)](#16-echo---node---piped-repl-stdin)
   - 1.7 [node --check \<file.js\> (Syntax Check Without Run)](#17-node---check-filejs-syntax-check-without-run)

2. **npm / yarn Script Runners**
   - 2.1 [npm start / npm test / npm run \<script\> (package.json scripts)](#21-npm-start--npm-test--npm-run-script-packagejson-scripts)
   - 2.2 [npx \<package\> (Ad-hoc Package Runner)](#22-npx-package-ad-hoc-package-runner)

3. **Framework Dev Servers (single-command source → running app)**
   - 3.1 [next dev / next start (Next.js)](#31-next-dev--next-start-nextjs)
   - 3.2 [nuxt dev (Nuxt.js)](#32-nuxt-dev-nuxtjs)
   - 3.3 [react-scripts start (Create React App)](#33-react-scripts-start-create-react-app)
   - 3.4 [vite (Vite dev / preview)](#34-vite-vite-dev--preview)
   - 3.5 [gatsby develop / gatsby serve (Gatsby)](#35-gatsby-develop--gatsby-serve-gatsby)
   - 3.6 [remix dev (Remix)](#36-remix-dev-remix)
   - 3.7 [redwood dev / rw dev (RedwoodJS)](#37-redwood-dev--rw-dev-redwoodjs)
   - 3.8 [stencil build --dev --watch --serve (Stencil)](#38-stencil-build---dev---watch---serve-stencil)
   - 3.9 [brunch watch --server (Brunch)](#39-brunch-watch---server-brunch)
   - 3.10 [umi dev (UmiJS)](#310-umi-dev-umijs)
   - 3.11 [nx serve (Nx monorepo)](#311-nx-serve-nx-monorepo)
   - 3.12 [turbo run dev / build (Turborepo)](#312-turbo-run-dev--build-turborepo)
   - 3.13 [nodemon \<file.js\> (Auto-restart on change)](#313-nodemon-filejs-auto-restart-on-change)
   - 3.14 [serve / npx ws / sirv (Static-file servers)](#314-serve--npx-ws--sirv-static-file-servers)
   - 3.15 [ember serve (Ember.js)](#315-ember-serve-emberjs)
   - 3.16 [rollup -c -w + sirv (Svelte legacy dev workflow)](#316-rollup--c--w--sirv-svelte-legacy-dev-workflow)
   - 3.17 [parcel (Parcel v2 dev server)](#317-parcel-parcel-v2-dev-server)
   - 3.18 [nitro dev (Nitro universal server)](#318-nitro-dev-nitro-universal-server)
   - 3.19 [polymer serve (Polymer CLI)](#319-polymer-serve-polymer-cli)
   - 3.20 [preact watch (Preact CLI)](#320-preact-watch-preact-cli)
   - 3.21 [shopify hydrogen dev (Hydrogen)](#321-shopify-hydrogen-dev-hydrogen)
   - 3.22 [storybook dev / storybook build (Storybook)](#322-storybook-dev--storybook-build-storybook)

4. **Static Site / Documentation Generators**
   - 4.1 [eleventy --serve (11ty)](#41-eleventy---serve-11ty)
   - 4.2 [hexo server (Hexo)](#42-hexo-server-hexo)
   - 4.3 [vuepress dev (VuePress)](#43-vuepress-dev-vuepress)
   - 4.4 [vitepress dev (VitePress)](#44-vitepress-dev-vitepress)
   - 4.5 [docusaurus-start (Docusaurus v1)](#45-docusaurus-start-docusaurus-v1)
   - 4.6 [docusaurus start / docusaurus serve (Docusaurus v2+)](#46-docusaurus-start--docusaurus-serve-docusaurus-v2)

5. **Test / Browser-Automation Runners**
   - 5.1 [jest (Jest test runner)](#51-jest-jest-test-runner)
   - 5.2 [vitest (Vite-native test runner)](#52-vitest-vite-native-test-runner)
   - 5.3 [mocha (Mocha test runner)](#53-mocha-mocha-test-runner)
   - 5.4 [npx cypress run (Cypress headless E2E)](#54-npx-cypress-run-cypress-headless-e2e)
   - 5.5 [npx playwright screenshot / test (Playwright)](#55-npx-playwright-screenshot--test-playwright)
   - 5.6 [npx wdio run (WebdriverIO)](#56-npx-wdio-run-webdriverio)

6. **Solidity / Hardhat (Node-hosted JS execution)**
   - 6.1 [npx hardhat run / test / node (Hardhat)](#61-npx-hardhat-run--test--node-hardhat)

7. **ClojureScript-on-Node**
   - 7.1 [nbb \<file.cljs\> and nbb -e (ClojureScript on Node)](#71-nbb-filecljs-and-nbb--e-clojurescript-on-node)

8. **Containerized Execution**
   - 8.1 [Docker (Node.js inside a container image)](#81-docker-nodejs-inside-a-container-image)

9. **Embedded / Polyglot Execution**
   - 9.1 [Java → GraalVM polyglot `Context.eval("js", …)`](#91-java--graalvm-polyglot-contextevaljs-)
   - 9.2 [`child_process.spawn/exec("node", …)` from another JS process](#92-child_processspawnexecnode--from-another-js-process)
   - 9.3 [Go / Java / Ruby / Objective-C / C++ → `node -e` subprocess](#93-go--java--ruby--objective-c--c--node--e-subprocess)

---

## 1. **Direct Node.js Execution**

### 1.1 node \<file.js\> (Node interpreter)
**Method:** Pass a `.js` / `.mjs` file directly to the `node` CLI. The V8 engine parses and executes the script in one step.

**Locations:**
- [javascript/adonis/serverless-adonis/package.json](https://github.com/aqwertyuiop48/serverless-adonis/blob/main/package.json#L9) - `"serve": "node server.js"` (AdonisJS v3 entry)
  - Remote (submodule @ branch `main`): [package.json#L9](https://github.com/aqwertyuiop48/serverless-adonis/blob/main/package.json#L9)

**Workflow yml (executes in CI):**
- [.github/workflows/mysql_.yml](../.github/workflows/mysql_.yml#L92) - `node javascript/mysql_connector_/mysql_connector_.js`
- [solidity__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L37) - `node scripts/solidity_in_js.js`
  - Remote (submodule `solidity__/codeforces_script` @ branch `solidity_`): [main.yml#L37](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L37)
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L65) - `node out/main.js` (after `npx shadow-cljs compile app`)
  - Remote (submodule @ branch `clojure_script`): [main.yml#L65](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L65)
- [javascript/java_embed/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L60) - `node java_node.js`
  - Remote (submodule @ branch `java_`): [main.yml#L60](https://github.com/aqwertyuiop48/codeforces_script/blob/java_/.github/workflows/main.yml#L60)

**Example:**
```bash
node javascript/mysql_connector_/mysql_connector_.js
node scripts/solidity_in_js.js
```

### 1.2 node -e "..." (Inline Expression)
**Method:** Evaluate a JS string supplied as a shell argument — no file written. Used heavily as the polyglot bridge from Java / Go / Ruby / Objective-C / C++ into Node.

**Locations:**
- [CPP/codeforces_script/cpp_/trial.cpp](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/trial.cpp#L29) - `node -e "console.log(2+3+' from nodejs');"` (C++ → Node)
  - Remote (submodule @ branch `cpp_`): [trial.cpp#L29](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/trial.cpp#L29)
- [typescript/inputs/shell_java_.js](../typescript/inputs/shell_java_.js#L3) - `spawn("node", ["-e", node_string])` (JS → Node)
- [golang/codeforces_script/execute/1_nested_functions.go](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/execute/1_nested_functions.go#L29) - `exec.Command("node", "-e", string_concat)` (Go → Node)
  - Remote (submodule @ branch `golang_`): [1_nested_functions.go#L29](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/execute/1_nested_functions.go#L29)
- [java/readme.txt](../java/readme.txt#L557) - `processBuilder.command("node", "-e", strings)` (Java → Node)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L196-L205) - dedicated "node -e" demo step runs `node -e 'const msg = "Hello from node -e!"; console.log(msg); ...'` — direct CI coverage.

**Example:**
```bash
node -e "console.log(2 + 3)"
```

### 1.3 node -e with shell heredoc (Multi-line Inline)
**Method:** Feed a multi-line JS program into `node -e` via a shell-quoted heredoc-style string. Lets a single CI step embed a non-trivial JS program — including `require(...)` imports — without a separate file.

**Locations:**
- [ruby/codeforces_script/execute/child.rb](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/execute/child.rb#L1-L7) - Ruby `exec <<~CMD ... node -e "console.log('Hi from nested nodejs!');" ... CMD`
  - Remote (submodule @ branch `ruby_`): [child.rb#L1-L7](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/execute/child.rb#L1-L7)

**Workflow yml (executes in CI):**
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L105-L130) - multi-line `node -e "..."` driving Playwright's `chromium.launch` to record videos
  - Remote (submodule @ branch `golang_`): [main.yml#L105-L130](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L105-L130)

**Example:**
```bash
node -e "
  const { chromium } = require('playwright');
  (async () => {
    const browser = await chromium.launch();
    const page = await (await browser.newContext()).newPage();
    await page.goto('http://localhost:8080');
    await browser.close();
  })();
"
```

### 1.4 node -p "..." (Print Expression Value)
**Method:** Evaluate a JS expression and print its value (analog of `node -e` but with an implicit `console.log` around the result). Handy for one-shot prints of runtime metadata or quick arithmetic without writing a script.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L204-L206) - ``node -p '`node -p :: ${process.version} on ${process.platform}`'``

**Example:**
```bash
node -p '2 + 3 * 4'
node -p '`Node ${process.version} on ${process.platform}`'
```

### 1.5 node - <<EOF (Stdin Heredoc)
**Method:** Pass `-` as the script argument so Node reads program source from stdin, then feed it a shell heredoc. Direct analog of `python3 - <<EOF` (see [python.md §2.2](python.md#22-python3---eof-stdin-heredoc)) for JavaScript. Distinct from `node -e "..."` heredoc (§1.3): here the program isn't an argv string, so quoting rules are simpler.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L208-L215) - `node - <<'EOF' ... console.log("Hello from node stdin heredoc!"); ... EOF`

**Example:**
```bash
node - <<'EOF'
const greeting = "Hello from node stdin heredoc!";
console.log(greeting);
console.log("Node version: " + process.version);
EOF
```

### 1.6 echo '...' | node - (Piped REPL Stdin)
**Method:** Pipe a JS program into Node's stdin via a shell pipe — the one-liner sibling of §1.5. Any shell command whose stdout is JS source can drive a Node run.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L217-L219) - `echo 'console.log("Hello from node via pipe! Node " + process.version)' | node -`

**Example:**
```bash
echo 'console.log("hi from node!")' | node -
printf '%s\n' 'const x = 1 + 2;' 'console.log(x);' | node -
```

### 1.7 node --check \<file.js\> (Syntax Check Without Run)
**Method:** Parse a JS file and report syntax errors without executing it. Used as a CI lint gate to fail fast before invoking a heavier runtime or test runner.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L221-L223) - `node --check /tmp/hello_node.js && echo "Syntax OK"`

**Example:**
```bash
node --check src/index.js && echo "Syntax OK"
```

---

## 2. **npm / yarn Script Runners**

### 2.1 npm start / npm test / npm run \<script\> (package.json scripts)
**Method:** Invoke a named script declared in `package.json#scripts`. The script body runs in a sub-shell with `node_modules/.bin` on `PATH`. `npm start` and `npm test` are shorthands for `npm run start` / `npm run test`.

**Locations:**
- Hundreds of `package.json` script declarations across `javascript/`.

**Workflow yml (executes in CI):**
- [.github/workflows/mains.yml](../.github/workflows/mains.yml#L27-L28) - `npm run asbuild` then `npm start` (AssemblyScript → Node)
- [.github/workflows/mains.yml](../.github/workflows/mains.yml#L35) - `npm run asbuild:optimized -- --memoryBase 40000`
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L34) - `npm run start` (resolves to `nbb hello.cljs`)
  - Remote (submodule @ branch `clojure_script`): [main.yml#L34](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L34)
- [javascript/main_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/main_/.github/workflows/main.yml#L107-L108) - `npm run pystart` / `npm run pystartmac`
  - Remote (submodule @ branch `main_`): [main.yml#L107-L108](https://github.com/aqwertyuiop48/codeforces_script/blob/main_/.github/workflows/main.yml#L107-L108)

**Example:**
```bash
npm start              # runs scripts.start
npm test               # runs scripts.test
npm run build          # runs scripts.build
npm run start:dev      # arbitrary named script
```

### 2.2 npx \<package\> (Ad-hoc Package Runner)
**Method:** `npx` resolves a binary from local `node_modules/.bin` (or fetches it from the npm registry), then executes it with the supplied args — in one command. Used as the entry point for many tools that themselves run JS / drive headless browsers.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L25-L37) - `npx hardhat compile`, `npx hardhat test`, `npx hardhat node`, `npx hardhat run scripts/deploy.js --network localhost`
  - Remote (submodule @ branch `solidity_`): [main.yml#L25-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L25-L37)
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L64) - `npx shadow-cljs compile app`
- [.github/workflows/mains.yml](../.github/workflows/mains.yml#L36) - `npx ws -p 1234 &` (local-web-server)
- [.github/workflows/webdriver_io.yml](../.github/workflows/webdriver_io.yml#L33) - `npx wdio run wdio.conf.js`
- [QA/cypress_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml#L31) - `npx cypress run`
  - Remote (submodule @ branch `cypress_testing`): [main.yml#L31](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml#L31)

**Example:**
```bash
npx cypress run
npx playwright screenshot http://localhost:8080 out.png
npx hardhat run scripts/deploy.js --network localhost
```

---

## 3. **Framework Dev Servers (single-command source → running app)**

Each entry below is a single CLI invocation that takes the framework's source tree as input and produces a running HTTP server (and, in dev mode, hot-reload on edits).

### 3.1 next dev / next start (Next.js)
**Method:** `next dev` boots the Next.js dev server (Webpack / Turbopack-driven) with HMR; `next start` runs a pre-built production server.

**Locations:**
- [javascript/next_/nextjs_project/package.json](https://github.com/aqwertyuiop48/nextjs_project/blob/main/package.json#L6-L8) - `"dev": "next dev"`, `"build": "next build"`, `"start": "next start"`
  - Remote (submodule @ branch `main`): [package.json#L6-L8](https://github.com/aqwertyuiop48/nextjs_project/blob/main/package.json#L6-L8)
- [javascript/next_/nextjs_news_search_microservices/package.json](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/package.json#L7-L9) - `"dev": "next dev --turbopack"`, `"start": "next start -p 8080"`
- [javascript/next_/nextjs_app/package.json](https://github.com/aqwertyuiop48/nextjs_app/blob/main/package.json#L7-L8) - `next build` / `next start`
  - Remote (submodule @ branch `main`): [package.json#L7-L8](https://github.com/aqwertyuiop48/nextjs_app/blob/main/package.json#L7-L8)
- [javascript/app_clones/whatsapp-2/package.json](https://github.com/aqwertyuiop48/whatsapp-2/blob/main/package.json#L5-L7) - `next dev` / `next build` / `next start`
  - Remote (submodule @ branch `main`): [package.json#L5-L7](https://github.com/aqwertyuiop48/whatsapp-2/blob/main/package.json#L5-L7)
- [javascript/turborepo-with-hono/apps/web/package.json](https://github.com/aqwertyuiop48/turborepo-with-hono/blob/main/apps/web/package.json#L7) - `next dev --turbopack --port 3001`

**Workflow yml (executes in CI):**
- [javascript/next_/nextjs_news_search_microservices/.github/workflows/ci.yml](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/.github/workflows/ci.yml#L30) - `npm run build` resolves via [package.json#L8](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/package.json#L8) to `next build` — transitive coverage via npm script.
- [javascript/nest_/nestjs_app/.github/workflows/deploy-to-vercel.yml](https://github.com/aqwertyuiop48/nestjs_app/blob/main/.github/workflows/deploy-to-vercel.yml#L38) - `npm run build` (within a Next-adjacent NestJS deploy) similarly invokes the framework CLI build pipeline.

**Example:**
```bash
next dev
next build && next start
```

### 3.2 nuxt dev (Nuxt.js)
**Method:** Nuxt CLI dev mode — single command compiles and serves the app with HMR.

**Locations:**
- [javascript/nuxt_/nuxtjs-boilerplate/package.json](https://github.com/aqwertyuiop48/nuxtjs-boilerplate/blob/main/package.json#L4-L5) - `"build": "nuxt build"`, `"dev": "nuxt dev"`
  - Remote (submodule @ branch `main`): [package.json#L4-L5](https://github.com/aqwertyuiop48/nuxtjs-boilerplate/blob/main/package.json#L4-L5)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/nuxt_/nuxtjs-boilerplate/package.json`

**Example:**
```bash
nuxt dev
```

### 3.3 react-scripts start (Create React App)
**Method:** CRA's wrapper boots a Webpack dev server (with HMR) for a React app from `src/`.

**Locations:**
- [javascript/react_/my_react_app/package.json](https://github.com/aqwertyuiop48/my_react_app/blob/main/package.json#L19) - `"test": "react-scripts test"` (and `start` / `build`)
  - Remote (submodule @ branch `main`): [package.json#L19](https://github.com/aqwertyuiop48/my_react_app/blob/main/package.json#L19)
- [javascript/react_/my_react_app_main/package.json](https://github.com/aqwertyuiop48/my_react_app/blob/main_/package.json#L19)
  - Remote (submodule @ branch `main_`): [package.json#L19](https://github.com/aqwertyuiop48/my_react_app/blob/main_/package.json#L19)
- [javascript/app_clones/Video-Meeting/package.json](https://github.com/aqwertyuiop48/Video-Meeting/blob/main/package.json#L39) - `react-scripts test`
- [javascript/ionic__/ionic_app/package.json](https://github.com/aqwertyuiop48/ionic_app/blob/main/package.json#L38-L40) - `react-scripts start` / `react-scripts test` (Ionic + React)
  - Remote (submodule @ branch `main`): [package.json#L38-L40](https://github.com/aqwertyuiop48/ionic_app/blob/main/package.json#L38-L40)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/app_clones/Video-Meeting/package.json`; `javascript/ionic__/ionic_app/package.json`; `javascript/react_/my_react_app/package.json`; `javascript/react_/my_react_app_main/package.json`

**Example:**
```bash
react-scripts start
react-scripts test
```

### 3.4 vite (Vite dev / preview)
**Method:** `vite` (no args) starts an ESM-native dev server with HMR. `vite preview` serves a pre-built bundle. `vite build` alone is build-only and excluded.

**Locations:**
- [javascript/vue_/vue_project/package.json](https://github.com/aqwertyuiop48/vue_project/blob/main/package.json#L3-L5) - `"dev": "vite"`, `"build": "vite build"`, `"preview": "vite preview"`
  - Remote (submodule @ branch `main`): [package.json#L3-L5](https://github.com/aqwertyuiop48/vue_project/blob/main/package.json#L3-L5)
- [javascript/new_frameworks/tanstack-start/package.json](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L6-L8) - `vite dev --port 3000`, `vite preview`
  - Remote (submodule @ branch `main`): [package.json#L6-L8](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L6-L8)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/tanstack-start/package.json`; `javascript/vue_/vue_project/package.json`

**Example:**
```bash
vite              # dev server
vite preview      # serve build output
```

### 3.5 gatsby develop / gatsby serve (Gatsby)
**Method:** `gatsby develop` runs the dev server with HMR; `gatsby serve` serves a pre-built static bundle.

**Locations:**
- [javascript/gatsby_/gatsby/package.json](https://github.com/aqwertyuiop48/gatsby/blob/main/package.json#L4-L7) - `"develop"`, `"start"`, `"build"`, `"serve"`
  - Remote (submodule @ branch `main`): [package.json#L4-L7](https://github.com/aqwertyuiop48/gatsby/blob/main/package.json#L4-L7)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/gatsby_/gatsby/package.json`

**Example:**
```bash
gatsby develop
gatsby serve
```

### 3.6 remix dev (Remix)
**Method:** Remix CLI dev server.

**Locations:**
- [javascript/remix_/remix/package.json](https://github.com/aqwertyuiop48/remix/blob/main/package.json#L5-L6) - `"build": "remix build"`, `"dev": "remix dev"`
  - Remote (submodule @ branch `main`): [package.json#L5-L6](https://github.com/aqwertyuiop48/remix/blob/main/package.json#L5-L6)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/remix_/remix/package.json`

**Example:**
```bash
remix dev
```

### 3.7 redwood dev / rw dev (RedwoodJS)
**Method:** Redwood's full-stack dev server starts the API and the web side simultaneously.

**Locations:**
- [javascript/redwood_/netlify-deploy/README.md](https://github.com/aqwertyuiop48/netlify-deploy/blob/main/README.md#L20) - `yarn redwood dev`
  - Remote (submodule @ branch `main`): [README.md#L20](https://github.com/aqwertyuiop48/netlify-deploy/blob/main/README.md#L20)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/redwood_/netlify-deploy/README.md`

**Example:**
```bash
yarn redwood dev
yarn rw test
```

### 3.8 stencil build --dev --watch --serve (Stencil)
**Method:** Single Stencil CLI command builds, watches, and serves web-component sources.

**Locations:**
- [javascript/stencil_/stencil/package.json](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L7-L9) - `"build": "stencil build"`, `"start": "stencil build --dev --watch --serve"`, `"test": "stencil test --spec --e2e"`
  - Remote (submodule @ branch `main`): [package.json#L7-L9](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L7-L9)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/stencil_/stencil/package.json`

**Example:**
```bash
stencil build --dev --watch --serve
```

### 3.9 brunch watch --server (Brunch)
**Method:** Brunch asset pipeline with auto-rebuild and a built-in HTTP server.

**Locations:**
- [javascript/brunch_/brunch/package.json](https://github.com/aqwertyuiop48/brunch/blob/main/package.json#L9-L11) - `"start": "brunch watch --server"`, `"dev": "brunch watch --server --port $PORT"`, `"build": "brunch build --production"`
  - Remote (submodule @ branch `main`): [package.json#L9-L11](https://github.com/aqwertyuiop48/brunch/blob/main/package.json#L9-L11)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/brunch_/brunch/package.json`

**Example:**
```bash
brunch watch --server
```

### 3.10 umi dev (UmiJS)
**Method:** UmiJS dev server. Needs `NODE_OPTIONS=--openssl-legacy-provider` here for compatibility with older OpenSSL APIs.

**Locations:**
- [javascript/umijs_/umijs/package.json](https://github.com/aqwertyuiop48/umijs/blob/main/package.json#L4) - `"start": "NODE_OPTIONS=--openssl-legacy-provider umi dev"`
  - Remote (submodule @ branch `main`): [package.json#L4](https://github.com/aqwertyuiop48/umijs/blob/main/package.json#L4)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/umijs_/umijs/package.json`

**Example:**
```bash
NODE_OPTIONS=--openssl-legacy-provider umi dev
```

### 3.11 nx serve (Nx monorepo)
**Method:** Nx task runner — `nx serve` resolves the default project's `serve` target and runs it (transitively invoking the project's framework dev server).

**Locations:**
- [javascript/nx_/nx-monorepo/package.json](https://github.com/aqwertyuiop48/nx-monorepo/blob/main/package.json#L5-L7) - `"start": "nx serve"`, `"build": "nx build"`, `"test": "nx test"`
  - Remote (submodule @ branch `main`): [package.json#L5-L7](https://github.com/aqwertyuiop48/nx-monorepo/blob/main/package.json#L5-L7)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/nx_/nx-monorepo/package.json`

**Example:**
```bash
nx serve
```

### 3.12 turbo run dev / build (Turborepo)
**Method:** Turborepo orchestrates per-package tasks across a monorepo, fanning out to each workspace's own `dev` / `build` script in dependency order.

**Locations:**
- [javascript/turborepo-with-hono/package.json](https://github.com/aqwertyuiop48/turborepo-with-hono/blob/main/package.json#L5-L6) - `"build": "turbo run build"`, `"dev": "turbo run dev"`
  - Remote (submodule @ branch `main`): [package.json#L5-L6](https://github.com/aqwertyuiop48/turborepo-with-hono/blob/main/package.json#L5-L6)
- [javascript/saas-microservices/package.json](https://github.com/aqwertyuiop48/saas-microservices/blob/main/package.json#L7-L10) - `"build": "turbo run build"`, `"dev": "turbo run dev"`

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/saas-microservices/package.json`; `javascript/turborepo-with-hono/package.json`

**Example:**
```bash
turbo run dev
turbo run build
```

### 3.13 nodemon \<file.js\> (Auto-restart on change)
**Method:** `nodemon` wraps `node`, restarting it when watched files change. Counts as a separate execution method because it produces continuous source-edit → re-run cycles from one command.

**Locations:**
- [javascript/express_/JavaScript-Applications/package.json](https://github.com/aqwertyuiop48/JavaScript-Applications/blob/main/package.json#L8) - `"start": "nodemon ./public/javascript_apps.js"`
  - Remote (submodule @ branch `main`): [package.json#L8](https://github.com/aqwertyuiop48/JavaScript-Applications/blob/main/package.json#L8)
- [javascript/koa_/koa_project/package.json](https://github.com/aqwertyuiop48/koa_project/blob/main/package.json#L13) - `"start": "nodemon index.js"`
- [javascript/adonis/serverless-adonis/package.json](https://github.com/aqwertyuiop48/serverless-adonis/blob/main/package.json#L7) - `"serve:dev": "nodemon --watch app --watch bootstrap --watch config --watch .env -x node server.js"`
  - Remote (submodule @ branch `main`): [package.json#L7](https://github.com/aqwertyuiop48/serverless-adonis/blob/main/package.json#L7)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/adonis/serverless-adonis/package.json`; `javascript/express_/JavaScript-Applications/package.json`; `javascript/koa_/koa_project/package.json`

**Example:**
```bash
nodemon ./public/javascript_apps.js
nodemon --watch app -x node server.js
```

### 3.14 serve / npx ws / sirv (Static-file servers)
**Method:** `serve` (npm package), `local-web-server` (`ws`), and `sirv` host a directory of HTML / JS / WASM over HTTP — the single command that turns static JS sources into a running site loaded by the browser.

**Locations:**
- [typescript/webassembly_/2d_game_/README.md](../typescript/webassembly_/2d_game_/README.md#L34) - `npx ws -p 1234`
- [javascript/svelte_/svelte/package.json](https://github.com/aqwertyuiop48/svelte/blob/main/package.json#L8) - `"start": "sirv public --no-clear"`
  - Remote (submodule @ branch `main`): [package.json#L8](https://github.com/aqwertyuiop48/svelte/blob/main/package.json#L8)
- [javascript/new_frameworks/preact/package.json](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L5) - `"serve": "sirv build --port 8080 --cors --single"`
  - Remote (submodule @ branch `main`): [package.json#L5](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L5)

**Workflow yml (executes in CI):**
- [.github/workflows/mains.yml](../.github/workflows/mains.yml#L26-L28) - `npm install serve@14.2.4`, `npm run asbuild`, `npm start` (CRA's `serve` for an AssemblyScript bundle, then `curl http://localhost:3000`)
- [.github/workflows/mains.yml](../.github/workflows/mains.yml#L36) - `npx ws -p 1234 &` (local-web-server)

**Example:**
```bash
npx serve -l 3000 build/
npx ws -p 1234
sirv public --no-clear
```

### 3.15 ember serve (Ember.js)
**Method:** Ember CLI dev server (Broccoli-driven asset pipeline + live-reload).

**Locations:**
- [javascript/ember_/ember/package.json](https://github.com/aqwertyuiop48/ember/blob/main/package.json#L17-L19) - `"dev": "ember serve --port $PORT"`, `"start": "ember serve"`, `"test": "ember test"`
  - Remote (submodule @ branch `main`): [package.json#L17-L19](https://github.com/aqwertyuiop48/ember/blob/main/package.json#L17-L19)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/ember_/ember/package.json`
- [javascript/ember_/ember/.travis.yml](https://github.com/aqwertyuiop48/ember/blob/main/.travis.yml) _(rule R1)_ — covers `javascript/ember_/ember/package.json`

**Example:**
```bash
ember serve
ember serve --port 4200
ember test
```

### 3.16 rollup -c -w + sirv (Svelte legacy dev workflow)
**Method:** Classic Svelte 3 template — `rollup -c -w` rebuilds the bundle on every save, while `sirv public` serves the output directory. Two cooperating long-running processes form the dev loop. The combined `rollup -c -w` watcher counts as a distinct dev-server execution because it produces continuous source-in → bundle-out without manual restart.

**Locations:**
- [javascript/svelte_/svelte/package.json](https://github.com/aqwertyuiop48/svelte/blob/main/package.json#L6-L8) - `"build": "rollup -c"`, `"dev": "rollup -c -w"`, `"start": "sirv public --no-clear"`
  - Remote (submodule @ branch `main`): [package.json#L6-L8](https://github.com/aqwertyuiop48/svelte/blob/main/package.json#L6-L8)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/svelte_/svelte/package.json`

**Example:**
```bash
rollup -c -w        # watch & rebuild
sirv public --no-clear   # serve the build/
```

### 3.17 parcel (Parcel v2 dev server)
**Method:** Parcel v2 zero-config bundler — `parcel <entry>` (or shorthand `parcel`) starts an HMR dev server; `parcel build` produces a production bundle.

**Locations:**
- [javascript/new_frameworks/parcel/package.json](https://github.com/aqwertyuiop48/parcel/blob/main/package.json#L6-L7) - `"start": "parcel"`, `"build": "parcel build"`
  - Remote (submodule @ branch `main`): [package.json#L6-L7](https://github.com/aqwertyuiop48/parcel/blob/main/package.json#L6-L7)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/parcel/package.json`

**Example:**
```bash
parcel              # dev server with HMR
parcel build
```

### 3.18 nitro dev (Nitro universal server)
**Method:** [Nitro](https://nitro.unjs.io/) is the universal server engine that powers Nuxt 3 — `nitro dev` boots a hot-reloading HTTP server from a `routes/` tree.

**Locations:**
- [javascript/new_frameworks/nitro/package.json](https://github.com/aqwertyuiop48/nitro/blob/main/package.json#L4-L9) - `"build": "nitro build"`, `"dev": "nitro dev"`, `"prepare": "nitro prepare"`
  - Remote (submodule @ branch `main`): [package.json#L4-L9](https://github.com/aqwertyuiop48/nitro/blob/main/package.json#L4-L9)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/nitro/package.json`

**Example:**
```bash
nitro dev
nitro build
```

### 3.19 polymer serve (Polymer CLI)
**Method:** Polymer CLI serves Web Components projects with module resolution and lite-server reload.

**Locations:**
- [javascript/new_frameworks/polymer/package.json](https://github.com/aqwertyuiop48/polymer/blob/main/package.json#L9-L14) - `"start": "polymer serve"`, `"dev": "polymer serve --port $PORT"`, `"build": "polymer build"`, `"test": "polymer test"`, `"lint": "polymer lint"`
  - Remote (submodule @ branch `main`): [package.json#L9-L14](https://github.com/aqwertyuiop48/polymer/blob/main/package.json#L9-L14)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/polymer/package.json`

**Example:**
```bash
polymer serve
polymer build
polymer test
```

### 3.20 preact watch (Preact CLI)
**Method:** Preact CLI dev server with hot reload.

**Locations:**
- [javascript/new_frameworks/preact/package.json](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L4-L6) - `"build": "NODE_OPTIONS=--openssl-legacy-provider preact build"`, `"dev": "preact watch"`, `"serve": "sirv build --port 8080 --cors --single"`
  - Remote (submodule @ branch `main`): [package.json#L4-L6](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L4-L6)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/preact/package.json`

**Example:**
```bash
preact watch
preact build
```

### 3.21 shopify hydrogen dev (Hydrogen)
**Method:** Shopify Hydrogen (Remix-based commerce framework) dev server.

**Locations:**
- [javascript/new_frameworks/hydrogen/package.json](https://github.com/aqwertyuiop48/hydrogen/blob/main/package.json#L8-L10) - `"dev": "shopify hydrogen dev"`, `"build": "shopify hydrogen build"`, `"preview": "shopify hydrogen preview"`
  - Remote (submodule @ branch `main`): [package.json#L8-L10](https://github.com/aqwertyuiop48/hydrogen/blob/main/package.json#L8-L10)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/hydrogen/package.json`

**Example:**
```bash
shopify hydrogen dev
shopify hydrogen build
```

### 3.22 storybook dev / storybook build (Storybook)
**Method:** `storybook dev -p <port>` launches the component explorer; `storybook build` exports a static site.

**Locations:**
- [javascript/new_frameworks/storybook/package.json](https://github.com/aqwertyuiop48/storybook/blob/main/package.json#L8-L9) - `"storybook": "storybook dev -p 6006"`, `"build-storybook": "storybook build"`
  - Remote (submodule @ branch `main`): [package.json#L8-L9](https://github.com/aqwertyuiop48/storybook/blob/main/package.json#L8-L9)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/storybook/package.json`

**Example:**
```bash
storybook dev -p 6006
storybook build
```

---

## 4. **Static Site / Documentation Generators**

### 4.1 eleventy --serve (11ty)
**Method:** Eleventy ([11ty](https://www.11ty.dev/)) static-site generator — `eleventy --serve` rebuilds and serves on every change.

**Locations:**
- [javascript/new_frameworks/eleventy/package.json](https://github.com/aqwertyuiop48/eleventy/blob/main/package.json#L18-L21) - `"build": "eleventy"`, `"watch": "eleventy --watch"`, `"serve": "eleventy --serve"`, `"start": "eleventy --serve"`
  - Remote (submodule @ branch `main`): [package.json#L18-L21](https://github.com/aqwertyuiop48/eleventy/blob/main/package.json#L18-L21)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/eleventy/package.json`

**Example:**
```bash
eleventy --serve
eleventy --watch
```

### 4.2 hexo server (Hexo)
**Method:** Hexo static blog generator. `hexo server` starts a dev server; `hexo generate` builds the static output.

**Locations:**
- [javascript/new_frameworks/hexo/package.json](https://github.com/aqwertyuiop48/hexo/blob/main/package.json#L20-L21) - `"dev": "hexo server -p $PORT"`, `"build": "hexo generate"`
  - Remote (submodule @ branch `main`): [package.json#L20-L21](https://github.com/aqwertyuiop48/hexo/blob/main/package.json#L20-L21)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/hexo/package.json`

**Example:**
```bash
hexo server -p 4000
hexo generate
```

### 4.3 vuepress dev (VuePress)
**Method:** VuePress documentation-site generator with HMR.

**Locations:**
- [javascript/new_frameworks/vuepress/package.json](https://github.com/aqwertyuiop48/vuepress/blob/main/package.json#L4-L5) - `"dev": "vuepress dev src"`, `"build": "vuepress build src"`
  - Remote (submodule @ branch `main`): [package.json#L4-L5](https://github.com/aqwertyuiop48/vuepress/blob/main/package.json#L4-L5)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/vuepress/package.json`

**Example:**
```bash
vuepress dev src
vuepress build src
```

### 4.4 vitepress dev (VitePress)
**Method:** VitePress — Vite-powered successor to VuePress.

**Locations:**
- [javascript/new_frameworks/vitepress/package.json](https://github.com/aqwertyuiop48/vitepress/blob/main/package.json#L11-L13) - `"dev": "vitepress dev docs"`, `"build": "vitepress build docs"`, `"serve": "vitepress serve docs"`
  - Remote (submodule @ branch `main`): [package.json#L11-L13](https://github.com/aqwertyuiop48/vitepress/blob/main/package.json#L11-L13)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/vitepress/package.json`

**Example:**
```bash
vitepress dev docs
vitepress serve docs
```

### 4.5 docusaurus-start (Docusaurus v1)
**Method:** Original (v1) Docusaurus CLI — separate `docusaurus-*` binaries per task.

**Locations:**
- [javascript/new_frameworks/docusaurus/package.json](https://github.com/aqwertyuiop48/docusaurus/blob/main/package.json#L4-L6) - `"start": "docusaurus-start"`, `"dev": "docusaurus-start --port $PORT"`, `"build": "docusaurus-build"`
  - Remote (submodule @ branch `main`): [package.json#L4-L6](https://github.com/aqwertyuiop48/docusaurus/blob/main/package.json#L4-L6)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/docusaurus/package.json`

**Example:**
```bash
docusaurus-start
docusaurus-build
```

### 4.6 docusaurus start / docusaurus serve (Docusaurus v2+)
**Method:** Modern Docusaurus CLI uses a single `docusaurus` binary with subcommands.

**Locations:**
- [javascript/new_frameworks/docusaurus-2/package.json](https://github.com/aqwertyuiop48/docusaurus-2/blob/main/package.json#L5-L10) - `"start": "docusaurus start"`, `"build": "docusaurus build"`, `"serve": "docusaurus serve"`
  - Remote (submodule @ branch `main`): [package.json#L5-L10](https://github.com/aqwertyuiop48/docusaurus-2/blob/main/package.json#L5-L10)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/docusaurus-2/package.json`

**Example:**
```bash
docusaurus start
docusaurus build
docusaurus serve
```

---

## 5. **Test / Browser-Automation Runners**

### 5.1 jest (Jest test runner)
**Method:** `jest` discovers and runs `*.test.js` / `*.spec.js`. Single command takes test source → results.

**Locations:**
- [javascript/new_frameworks/preact/package.json](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L8) - `"test": "jest"`
  - Remote (submodule @ branch `main`): [package.json#L8](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L8)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `javascript/new_frameworks/preact/package.json`

**Example:**
```bash
jest
jest --watch
jest --coverage
```

### 5.2 vitest (Vite-native test runner)
**Method:** `vitest` is a Jest-API-compatible runner that reuses Vite's transform pipeline. `vitest run --coverage` executes once and reports.

**Locations:**
- [javascript/next_/nextjs_news_search_microservices/package.json](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/package.json#L11) - `"test": "vitest"`
- [javascript/new_frameworks/tanstack-start/package.json](https://github.com/aqwertyuiop48/tanstack-start/blob/main/package.json#L9) - `"test": "vitest run"`
- [javascript/new_frameworks/hydrogen/package.json](https://github.com/aqwertyuiop48/hydrogen/blob/main/package.json#L13) - `"test": "WATCH=true vitest"`

**Workflow yml (executes in CI):**
- [javascript/next_/nextjs_news_search_microservices/.github/workflows/ci.yml](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/.github/workflows/ci.yml#L37) - `npx vitest run --coverage`

**Example:**
```bash
vitest
npx vitest run --coverage
```

### 5.3 mocha (Mocha test runner)
**Method:** Mocha is a flexible JS test runner — invoked directly (or via `npx mocha`) it discovers and runs spec files under `test/`. Hardhat depends on it transitively; some projects also use it standalone.

**Locations:**
- [solidity__/codeforces_script/package-lock.json](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/package-lock.json#L4888-L4917) - declares the `mocha` and `_mocha` binaries (transitive dep of Hardhat)
  - Remote (submodule @ branch `solidity_`): [package-lock.json#L4888-L4917](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/package-lock.json#L4888-L4917)
- [QA/cypress_/codeforces_script/package.json](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/package.json#L57) - `mocha-junit-reporter` (Cypress + Mocha reporter chain)
  - Remote (submodule @ branch `cypress_testing`): [package.json#L57](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/package.json#L57)
- [javascript/webdriver_io/package.json](../javascript/webdriver_io/package.json#L7) - `@wdio/mocha-framework` (WebdriverIO using Mocha as its test runner)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `QA/cypress_/codeforces_script/package.json`; `solidity__/codeforces_script/package-lock.json`
- [.github/workflows/webdriver_io.yml](../.github/workflows/webdriver_io.yml) _(rule R2)_ — covers `javascript/webdriver_io/package.json`
- [QA/cypress_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml) _(rule R1)_ — covers `QA/cypress_/codeforces_script/package.json`
- [solidity__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml) _(rule R1)_ — covers `solidity__/codeforces_script/package-lock.json`
- [solidity__/codeforces_script/.github/workflows/mains.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/mains.yml) _(rule R1)_ — covers `solidity__/codeforces_script/package-lock.json`

**Example:**
```bash
npx mocha
npx mocha 'test/**/*.spec.js'
```

### 5.4 npx cypress run (Cypress headless E2E)
**Method:** `cypress run` boots an Electron-backed headless browser, executes every `cypress/e2e/**` spec, and writes a report.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [QA/cypress_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml#L31) - `npx cypress run`
  - Remote (submodule @ branch `cypress_testing`): [main.yml#L31](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml#L31)
- [QA/cypress_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml#L51-L52) - report / video artifacts published

**Example:**
```bash
npx cypress run
```

### 5.5 npx playwright screenshot / test (Playwright)
**Method:** `playwright` ships its own browser binaries and is invoked via `npx playwright …`. `screenshot` and `test` both take JS specs / URLs as input and produce program output (images, traces, reports) in one step.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L27) - `npx playwright install`
  - Remote (submodule @ branch `golang_`): [main.yml#L27](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L27)
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L98-L100) - `npx playwright screenshot http://localhost:8080 videos/index.png` (multiple endpoints)
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L105-L130) - Playwright driven via `node -e` heredoc to record `chromium` video

**Example:**
```bash
npx playwright install
npx playwright screenshot http://localhost:8080 page.png
npx playwright test
```

### 5.6 npx wdio run (WebdriverIO)
**Method:** WebdriverIO's `wdio run <config>` reads a JS config, spawns the chosen runner (mocha / jasmine / cucumber), and executes browser tests in one step.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/webdriver_io.yml](../.github/workflows/webdriver_io.yml#L33) - `npx wdio run wdio.conf.js`

**Example:**
```bash
npx wdio run wdio.conf.js
```

---

## 6. **Solidity / Hardhat (Node-hosted JS execution)**

### 6.1 npx hardhat run / test / node (Hardhat)
**Method:** Hardhat is a Node-hosted Ethereum dev framework. `npx hardhat node` runs a local JSON-RPC chain; `npx hardhat run scripts/X.js --network localhost` executes a JS deploy / interaction script against it; `npx hardhat test` runs the project's JS test suite.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L25-L37) - `npm install --save-dev hardhat`, `npx hardhat compile`, `npx hardhat test`, `npx hardhat node`, `npx hardhat run scripts/deploy.js --network localhost && node scripts/solidity_in_js.js`
  - Remote (submodule @ branch `solidity_`): [main.yml#L25-L37](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L25-L37)

**Example:**
```bash
npx hardhat node &
npx hardhat run scripts/deploy.js --network localhost
npx hardhat test
```

---

## 7. **ClojureScript-on-Node**

### 7.1 nbb \<file.cljs\> and nbb -e (ClojureScript on Node)
**Method:** [nbb](https://github.com/babashka/nbb) is a Clojure scripting runtime that compiles ClojureScript to JS in-memory and runs it under Node — so the executing engine is V8. Two forms:

- `nbb <file.cljs>` — single command runs the file under Node.
- `nbb -e '<expr>'` — inline ClojureScript expression, no file needed.

**Locations:**
- [clojure_/clojure_script_/codeforces_script/package.json](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L3) - `"start": "nbb hello.cljs"`
  - Remote (submodule @ branch `clojure_script`): [package.json#L3](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L3)

**Workflow yml (executes in CI):**
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L31-L34) - `npm install nbb -g`, `nbb -e '(+ 1 2 3)'`, `npm run start`
- [clojure_/clojure_script_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/.github/workflows/main.yml#L36-L59) - multi-line `nbb -e '(ns hello (:require ["fs" :as fs] ...)) (println "Hello from nbb!") ...'`

**Example:**
```bash
nbb -e '(+ 1 2 3)'
nbb hello.cljs
```

---

## 8. **Containerized Execution**

### 8.1 Docker (Node.js inside a container image)
**Method:** Build a Docker image whose `Dockerfile` starts `FROM node:<tag>`, copies sources, installs deps, and exposes a Node entrypoint. Running the image executes the JS program — `docker build … && docker run …` is the single source-in → running-app pipeline.

**Locations:**
- [javascript/next_/nextjs_app/Dockerfile](https://github.com/aqwertyuiop48/nextjs_app/blob/main/Dockerfile#L2-L17) - `FROM node:22-alpine AS build` … `FROM node:22-alpine` (multi-stage build + runtime)
  - Remote (submodule @ branch `main`): [Dockerfile#L2-L17](https://github.com/aqwertyuiop48/nextjs_app/blob/main/Dockerfile#L2-L17)
- [javascript/next_/nextjs_news_search_microservices/Dockerfile](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/Dockerfile#L2-L14) - multi-stage `node:18-alpine` builder + runner
- [QA/cypress_/codeforces_script/Dockerfile](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/Dockerfile#L22-L25) - `RUN npm install` + Cypress entrypoint
  - Remote (submodule @ branch `cypress_testing`): [Dockerfile#L22-L25](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/Dockerfile#L22-L25)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `QA/cypress_/codeforces_script/Dockerfile`; `javascript/next_/nextjs_app/Dockerfile`; `javascript/next_/nextjs_news_search_microservices/Dockerfile`
- [QA/cypress_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml) _(rule R1)_ — covers `QA/cypress_/codeforces_script/Dockerfile`
- [javascript/next_/nextjs_app/.github/workflows/main.yml](https://github.com/aqwertyuiop48/nextjs_app/blob/main/.github/workflows/main.yml) _(rule R1)_ — covers `javascript/next_/nextjs_app/Dockerfile`
- [javascript/next_/nextjs_news_search_microservices/.github/workflows/ci.yml](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/.github/workflows/ci.yml) _(rule R1)_ — covers `javascript/next_/nextjs_news_search_microservices/Dockerfile`

**Example:**
```dockerfile
FROM node:22-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
CMD ["npm", "start"]
```
```bash
docker build -t my-node-app .
docker run -p 3000:3000 my-node-app
```

---

## 9. **Embedded / Polyglot Execution**

### 9.1 Java → GraalVM polyglot `Context.eval("js", …)`
**Method:** GraalVM's `org.graalvm.polyglot.Context` lets Java code execute a JavaScript expression in-process (no subprocess) and read back the result as a `Value`. Used in the codeforces solver below to evaluate arithmetic expressions for the LeetCode "Basic Calculator II" problem.

**Locations:**
- [java/codeforces_script/src/main/java/com/example/DataStructures.java](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L16) - `import org.graalvm.polyglot.*;`
  - Remote (submodule @ branch `javac_`): [DataStructures.java#L16](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L16)
- [java/codeforces_script/src/main/java/com/example/DataStructures.java](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L1040) - `return (int) Context.create("js").eval("js", expression).asDouble();`
  - Remote (submodule @ branch `javac_`): [DataStructures.java#L1040](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L1040)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `java/codeforces_script/src/main/java/com/example/DataStructures.java`
- [java/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/.github/workflows/main.yml) _(rule R1)_ — covers `java/codeforces_script/src/main/java/com/example/DataStructures.java`
- [java/codeforces_script/.github/workflows/main_kotlin.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/.github/workflows/main_kotlin.yml) _(rule R1)_ — covers `java/codeforces_script/src/main/java/com/example/DataStructures.java`
- [java/codeforces_script/.github/workflows/main_kotlin_gradle.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/.github/workflows/main_kotlin_gradle.yml) _(rule R1)_ — covers `java/codeforces_script/src/main/java/com/example/DataStructures.java`

**Example:**
```java
import org.graalvm.polyglot.*;
int result = (int) Context.create("js").eval("js", "1 + 2 * 3").asDouble();
```

### 9.2 `child_process.spawn/exec("node", …)` from another JS process
**Method:** JS that spawns a fresh `node` subprocess via `child_process`. The parent passes the child code (file path, `-e` string, etc.) and pipes the child's stdout / stderr — one observable execution unit per spawn.

**Locations:**
- [typescript/inputs/shell_java_.js](../typescript/inputs/shell_java_.js#L3) - `spawn("node", ["-e", node_string])`
- [typescript/inputs/nested_child_process.js](../typescript/inputs/nested_child_process.js#L4) - `exec(\`node -e "console.log(2)"\`)` (compiled JS form)

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L271-L274) - inline shim runs `node -e "require('child_process').spawn('node',['-e','console.log(2+3)'])…"` and the analogous `exec(\`node -e "console.log(42)"\`)` form. The two `typescript/inputs/*.js` files in Locations above are not invoked directly (`shell_java_.js` has hardcoded macOS paths in its `child1`), but the §9.2 method itself — `spawn`/`exec` of a fresh `node` subprocess — is exercised end-to-end.

**Example:**
```js
const { spawn } = require('child_process');
const child = spawn('node', ['-e', 'console.log(2 + 3)']);
child.stdout.pipe(process.stdout);
```

### 9.3 Go / Java / Ruby / Objective-C / C++ → `node -e` subprocess
**Method:** Polyglot pattern where a non-JS host (Go's `os/exec`, Java's `ProcessBuilder`, Ruby's `exec`, Objective-C's `NSTask`, C++'s `system`) launches `node -e` with code synthesized at runtime, then reads back the program's stdout.

**Locations:**
- [golang/codeforces_script/execute/1_nested_functions.go](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/execute/1_nested_functions.go#L29) - `exec.Command("node", "-e", string_concat)` (Go → Node)
  - Remote (submodule @ branch `golang_`): [1_nested_functions.go#L29](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/execute/1_nested_functions.go#L29)
- [java/readme.txt](../java/readme.txt#L557) - `processBuilder.command("node", "-e", strings)` (Java → Node)
- [ruby/codeforces_script/execute/child.rb](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/execute/child.rb#L1-L7) - Ruby `exec <<~CMD … node -e "…" … CMD`
  - Remote (submodule @ branch `ruby_`): [child.rb#L1-L7](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/execute/child.rb#L1-L7)
- [objective_c_cpp/codeforces_script/hello.mm](https://github.com/aqwertyuiop48/codeforces_script/blob/objective_c_/hello.mm#L49) - `[NSString stringWithFormat:@"node -e \"%@\"", nodeCommandNSString]` (Objective-C → Node)
  - Remote (submodule @ branch `objective_c_cpp_`): [hello.mm#L49](https://github.com/aqwertyuiop48/codeforces_script/blob/objective_c_cpp_/hello.mm#L49)
- [CPP/codeforces_script/cpp_/trial.cpp](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/trial.cpp#L29) - `node -e "console.log(2+3+' from nodejs');"` invoked via `system(...)` (C++ → Node)
  - Remote (submodule @ branch `cpp_`): [trial.cpp#L29](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/trial.cpp#L29)

**Workflow yml (executes in CI):**
Transitively exercised in CI via the following workflow(s) — the
subsection's documented file(s) are inside submodules/directories
that are built, tested, or referenced by these workflows:

- [.github/workflows/main.yml](../.github/workflows/main.yml) _(rule R2)_ — covers `CPP/codeforces_script/cpp_/trial.cpp`; `golang/codeforces_script/execute/1_nested_functions.go`; `objective_c_cpp/codeforces_script/hello.mm`; `ruby/codeforces_script/execute/child.rb`
- [CPP/codeforces_script/.github/workflows/builds.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/.github/workflows/builds.yml) _(rule R1)_ — covers `CPP/codeforces_script/cpp_/trial.cpp`
- [CPP/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/.github/workflows/main.yml) _(rule R1)_ — covers `CPP/codeforces_script/cpp_/trial.cpp`
- [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml) _(rule R1)_ — covers `golang/codeforces_script/execute/1_nested_functions.go`
- [objective_c_cpp/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/objective_c_/.github/workflows/main.yml) _(rule R1)_ — covers `objective_c_cpp/codeforces_script/hello.mm`
- [ruby/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/ruby_/.github/workflows/main.yml) _(rule R1)_ — covers `ruby/codeforces_script/execute/child.rb`

**Example (Go):**
```go
cmd := exec.Command("node", "-e", `console.log("hi from nodejs")`)
out, _ := cmd.Output()
fmt.Println(string(out))
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `node <file.js>` | Direct interpreter execution | [.github/workflows/mysql_.yml](../.github/workflows/mysql_.yml#L92) |
| `node -e "..."` | Inline JS expression | [CPP/codeforces_script/cpp_/trial.cpp](https://github.com/aqwertyuiop48/codeforces_script/blob/cpp_/cpp_/trial.cpp#L29) |
| `node -e` + shell heredoc | Multi-line inline JS | [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L105) |
| `npm start` / `npm test` / `npm run <s>` | package.json scripts | [javascript/express_/JavaScript-Applications/package.json](https://github.com/aqwertyuiop48/JavaScript-Applications/blob/main/package.json#L8) |
| `npx <pkg>` | Ad-hoc package runner | [solidity__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L31) |
| `next dev` / `next start` | Next.js dev / prod | [javascript/next_/nextjs_app/package.json](https://github.com/aqwertyuiop48/nextjs_app/blob/main/package.json#L7) |
| `nuxt dev` | Nuxt dev | [javascript/nuxt_/nuxtjs-boilerplate/package.json](https://github.com/aqwertyuiop48/nuxtjs-boilerplate/blob/main/package.json#L4) |
| `react-scripts start` | Create React App | [javascript/react_/my_react_app/package.json](https://github.com/aqwertyuiop48/my_react_app/blob/main/package.json#L19) |
| `vite` / `vite preview` | Vite dev / preview | [javascript/vue_/vue_project/package.json](https://github.com/aqwertyuiop48/vue_project/blob/main/package.json#L3) |
| `gatsby develop` / `serve` | Gatsby | [javascript/gatsby_/gatsby/package.json](https://github.com/aqwertyuiop48/gatsby/blob/main/package.json#L4) |
| `remix dev` | Remix | [javascript/remix_/remix/package.json](https://github.com/aqwertyuiop48/remix/blob/main/package.json#L6) |
| `redwood dev` / `rw dev` | RedwoodJS | [javascript/redwood_/netlify-deploy/README.md](https://github.com/aqwertyuiop48/netlify-deploy/blob/main/README.md#L20) |
| `stencil build --dev --watch --serve` | Stencil web components | [javascript/stencil_/stencil/package.json](https://github.com/aqwertyuiop48/stencil/blob/main/package.json#L8) |
| `brunch watch --server` | Brunch | [javascript/brunch_/brunch/package.json](https://github.com/aqwertyuiop48/brunch/blob/main/package.json#L9) |
| `umi dev` | UmiJS | [javascript/umijs_/umijs/package.json](https://github.com/aqwertyuiop48/umijs/blob/main/package.json#L4) |
| `nx serve` | Nx monorepo | [javascript/nx_/nx-monorepo/package.json](https://github.com/aqwertyuiop48/nx-monorepo/blob/main/package.json#L5) |
| `turbo run dev` | Turborepo | [javascript/turborepo-with-hono/package.json](https://github.com/aqwertyuiop48/turborepo-with-hono/blob/main/package.json#L6) |
| `nodemon <file>` | Auto-restart wrapper | [javascript/express_/JavaScript-Applications/package.json](https://github.com/aqwertyuiop48/JavaScript-Applications/blob/main/package.json#L8) |
| `serve` / `npx ws` / `sirv` | Static file servers | [.github/workflows/mains.yml](../.github/workflows/mains.yml#L36)<br/>[javascript/svelte_/svelte/package.json](https://github.com/aqwertyuiop48/svelte/blob/main/package.json#L8) |
| `ember serve` | Ember.js | [javascript/ember_/ember/package.json](https://github.com/aqwertyuiop48/ember/blob/main/package.json#L18) |
| `rollup -c -w` + `sirv` | Svelte legacy dev loop | [javascript/svelte_/svelte/package.json](https://github.com/aqwertyuiop48/svelte/blob/main/package.json#L7) |
| `parcel` | Parcel v2 | [javascript/new_frameworks/parcel/package.json](https://github.com/aqwertyuiop48/parcel/blob/main/package.json#L6) |
| `nitro dev` | Nitro universal server | [javascript/new_frameworks/nitro/package.json](https://github.com/aqwertyuiop48/nitro/blob/main/package.json#L5) |
| `polymer serve` | Polymer CLI | [javascript/new_frameworks/polymer/package.json](https://github.com/aqwertyuiop48/polymer/blob/main/package.json#L9) |
| `preact watch` | Preact CLI | [javascript/new_frameworks/preact/package.json](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L6) |
| `shopify hydrogen dev` | Hydrogen | [javascript/new_frameworks/hydrogen/package.json](https://github.com/aqwertyuiop48/hydrogen/blob/main/package.json#L8) |
| `storybook dev` / `storybook build` | Storybook | [javascript/new_frameworks/storybook/package.json](https://github.com/aqwertyuiop48/storybook/blob/main/package.json#L8) |
| `eleventy --serve` | 11ty | [javascript/new_frameworks/eleventy/package.json](https://github.com/aqwertyuiop48/eleventy/blob/main/package.json#L20) |
| `hexo server` | Hexo blog | [javascript/new_frameworks/hexo/package.json](https://github.com/aqwertyuiop48/hexo/blob/main/package.json#L20) |
| `vuepress dev` | VuePress | [javascript/new_frameworks/vuepress/package.json](https://github.com/aqwertyuiop48/vuepress/blob/main/package.json#L4) |
| `vitepress dev` | VitePress | [javascript/new_frameworks/vitepress/package.json](https://github.com/aqwertyuiop48/vitepress/blob/main/package.json#L11) |
| `docusaurus-start` | Docusaurus v1 | [javascript/new_frameworks/docusaurus/package.json](https://github.com/aqwertyuiop48/docusaurus/blob/main/package.json#L4) |
| `docusaurus start / serve` | Docusaurus v2+ | [javascript/new_frameworks/docusaurus-2/package.json](https://github.com/aqwertyuiop48/docusaurus-2/blob/main/package.json#L5) |
| `jest` | JS test runner | [javascript/new_frameworks/preact/package.json](https://github.com/aqwertyuiop48/preact/blob/main/package.json#L8) |
| `vitest [run]` | Vite-native test runner | [javascript/next_/nextjs_news_search_microservices/.github/workflows/ci.yml](https://github.com/aqwertyuiop48/nextjs_news_search_microservices/blob/main/.github/workflows/ci.yml#L37) |
| `mocha` | Mocha test runner | [solidity__/codeforces_script/package-lock.json](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/package-lock.json#L4888) |
| `npx cypress run` | Headless E2E | [QA/cypress_/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/cypress_testing/.github/workflows/main.yml#L31) |
| `npx playwright screenshot / test` | Browser automation | [golang/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/.github/workflows/main.yml#L98) |
| `npx wdio run` | WebdriverIO | [.github/workflows/webdriver_io.yml](../.github/workflows/webdriver_io.yml#L33) |
| `npx hardhat run / test / node` | Solidity dev hosted on Node | [solidity__/codeforces_script/.github/workflows/main.yml](https://github.com/aqwertyuiop48/codeforces_script/blob/solidity_/.github/workflows/main.yml#L33) |
| `nbb <file.cljs>` / `nbb -e` | ClojureScript on Node | [clojure_/clojure_script_/codeforces_script/package.json](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_script/package.json#L3) |
| Docker (`FROM node:…`) | Containerized Node | [javascript/next_/nextjs_app/Dockerfile](https://github.com/aqwertyuiop48/nextjs_app/blob/main/Dockerfile#L2) |
| Java GraalVM `Context.eval("js", …)` | In-process polyglot | [java/codeforces_script/src/main/java/com/example/DataStructures.java](https://github.com/aqwertyuiop48/codeforces_script/blob/javac_/src/main/java/com/example/DataStructures.java#L1040) |
| `child_process.spawn/exec("node", …)` | JS → Node subprocess | [typescript/inputs/shell_java_.js](../typescript/inputs/shell_java_.js#L3) |
| Go / Java / Ruby / Obj-C / C++ → `node -e` | Cross-language polyglot | [golang/codeforces_script/execute/1_nested_functions.go](https://github.com/aqwertyuiop48/codeforces_script/blob/golang_/execute/1_nested_functions.go#L29) |

---

## Key Frameworks & Tools Integrated

- **Next.js / Nuxt / Remix / Gatsby / RedwoodJS / UmiJS / Hydrogen** — full-stack JS meta-frameworks with single-command dev servers
- **Vue 3 / Vite / Preact / Polymer / Stencil / Brunch / Ember / Svelte / Backbone / Knockout** — front-end framework dev servers
- **Nitro** — Nuxt 3's universal server engine
- **Parcel** — zero-config bundler with built-in dev server
- **Nx / Turborepo** — monorepo task orchestrators
- **Express / Koa / Hapi / Adonis** — Node HTTP frameworks (started with `node`, `nodemon`)
- **Hardhat** — Solidity dev framework, hosted on Node via `npx hardhat`
- **Eleventy / Hexo / VuePress / VitePress / Docusaurus** — static site / documentation generators
- **Storybook** — component explorer + static-site exporter
- **Jest / Vitest / Mocha / Cypress / Playwright / WebdriverIO** — JS test & browser-automation runners
- **nbb** — ClojureScript-on-Node REPL & script runner
- **GraalVM polyglot** — in-process Java → JavaScript execution via `Context.eval("js", …)`
- **`node -e` polyglot bridge** — used from Go, Java, Ruby, Objective-C, C++, and JS itself to embed JavaScript snippets into host programs
- **Docker** — `node:<tag>` base images for containerized Next.js, Angular, Cypress, etc.

> TypeScript-first execution methods and frameworks (Angular, NestJS, Qwik, SolidJS, `ts-node`, `tsx`, `bun`, etc.) are documented in [typescript.md](typescript.md).

---

**Last Updated:** June 9, 2026
**Repository:** /workspaces/programming_languages
