# Solidity Execution Methods in Programming Languages Repository

This document catalogues **all distinct Solidity methods** discovered for compiling, testing, and deploying Solidity code throughout the repository. Solidity is a smart-contract language for the Ethereum Virtual Machine (EVM); execution requires (a) a compiler (`solc` directly or via Hardhat's wrapper) and (b) an EVM runtime — typically Hardhat's in-process node or its standalone `npx hardhat node`.

## Table of Contents

1. **Hardhat Toolchain**
   - 1.1 [npx hardhat compile (Compile Contracts)](#11-npx-hardhat-compile-compile-contracts)
   - 1.2 [npx hardhat test (Run Mocha/Chai Tests)](#12-npx-hardhat-test-run-mochachai-tests)
   - 1.3 [npx hardhat node (Long-Running Local EVM)](#13-npx-hardhat-node-long-running-local-evm)
   - 1.4 [npx hardhat run scripts/deploy.js --network localhost (Deploy a Contract)](#14-npx-hardhat-run-scriptsdeployjs---network-localhost-deploy-a-contract)

2. **Direct solc + ethers.js (No Hardhat)**
   - 2.1 [node scripts/solidity_in_js.js (Inline solc.compile + ethers Deploy)](#21-node-scriptssolidity_in_jsjs-inline-solccompile--ethers-deploy)
   - 2.2 [node scripts/solidity_in_js_1.js (Variant with Events)](#22-node-scriptssolidity_in_js_1js-variant-with-events)

3. **Direct Solidity Compiler CLI (No Hardhat, No JS Driver)**
   - 3.1 [solcjs --bin --abi \<file.sol\> (npm-Published solc Wrapper)](#31-solcjs---bin---abi-filesol-npm-published-solc-wrapper)

---

## 1. **Hardhat Toolchain**

### 1.1 npx hardhat compile (Compile Contracts)
**Method:** Hardhat reads [hardhat.config.js](../solidity__/codeforces_script/hardhat.config.js), invokes `solc` against every `.sol` under `contracts/`, and emits ABIs + bytecode under `artifacts/`. The CI compiles both `Lock.sol` and `HelloWorld.sol`.

**Locations:**
- [solidity__/codeforces_script/contracts/Lock.sol](../solidity__/codeforces_script/contracts/Lock.sol) - time-locked withdrawal contract (Hardhat's default sample)
- [solidity__/codeforces_script/contracts/HelloWorld.sol](../solidity__/codeforces_script/contracts/HelloWorld.sol) - "Hello, World!" smart contract
- [solidity__/codeforces_script/hardhat.config.js](../solidity__/codeforces_script/hardhat.config.js) - solc version + networks config
- [solidity__/codeforces_script/package.json](../solidity__/codeforces_script/package.json) - `hardhat`, `@nomicfoundation/hardhat-toolbox`, `ethers`
  - Remote (submodule `solidity__/codeforces_script` @ branch `solidity_`): [contracts/](https://github.com/aqwertyuiop48/codeforces_script/tree/solidity_/contracts)

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L20-L23) - `actions/setup-node@v3` (Node.js 18) + `npm install`
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L25-L26) - `npx hardhat compile`

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L130) — submodule sync that triggers the `solidity_` branch run

**Example:**
```bash
npm install
npx hardhat compile
```

### 1.2 npx hardhat test (Run Mocha/Chai Tests)
**Method:** Hardhat's bundled Mocha runner discovers tests under `test/` and runs each in a fresh in-process EVM (`hardhat` network). Assertions use Chai + `@nomicfoundation/hardhat-toolbox` matchers.

**Locations:**
- [solidity__/codeforces_script/test/Lock.js](../solidity__/codeforces_script/test/Lock.js) - Mocha test suite for `Lock.sol`

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L28-L29) - `npx hardhat test`

**Example:**
```bash
npx hardhat test
```

### 1.3 npx hardhat node (Long-Running Local EVM)
**Method:** Starts a JSON-RPC EVM at `http://localhost:8545` seeded with 20 funded accounts. Long-running process — in CI it is launched in the background with `&` and given `sleep 5` before the next step. Required for `--network localhost` deploys (§1.4) and external clients.

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L32-L34) - `npx hardhat node & sleep 5`

**Example:**
```bash
npx hardhat node &
sleep 5    # let the RPC port come up
```

### 1.4 npx hardhat run scripts/deploy.js --network localhost (Deploy a Contract)
**Method:** Runs an arbitrary Node.js script with Hardhat's ethers.js helpers injected into scope. The standard "deploy" script calls `ethers.getContractFactory(...)`, `.deploy()`, and `.waitForDeployment()`. `--network localhost` targets the node from §1.3.

**Locations:**
- [solidity__/codeforces_script/scripts/deploy.js](../solidity__/codeforces_script/scripts/deploy.js) - deploys `HelloWorld` and `Lock`

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L36-L37) - `npx hardhat run scripts/deploy.js --network localhost`

**Example:**
```bash
npx hardhat run scripts/deploy.js --network localhost
```

---

## 2. **Direct solc + ethers.js (No Hardhat)**

### 2.1 node scripts/solidity_in_js.js (Inline solc.compile + ethers Deploy)
**Method:** A pure Node.js script that:
1. reads `.sol` source as a string,
2. calls `solc.compile(JSON.stringify({language: "Solidity", sources: …}))` to compile **without Hardhat**,
3. extracts ABI + bytecode from the JSON output,
4. uses `ethers.ContractFactory(abi, bytecode, signer).deploy()` against the localhost RPC node (§1.3) to deploy.

This is "Solidity in JS" — the compiler runs in-process and the deploy uses raw ethers.js, no Hardhat helpers.

**Locations:**
- [solidity__/codeforces_script/scripts/solidity_in_js.js](../solidity__/codeforces_script/scripts/solidity_in_js.js) - `solc.compile(...)` + `new ethers.ContractFactory(...).deploy()`

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L38) - `node scripts/solidity_in_js.js`

**Example:**
```bash
node scripts/solidity_in_js.js
```

### 2.2 node scripts/solidity_in_js_1.js (Variant with Events)
**Method:** Same flow as §2.1 — `solc.compile` + ethers deploy — but exercises additional behaviour (event emission / multi-contract compile).

**Locations:**
- [solidity__/codeforces_script/scripts/solidity_in_js_1.js](../solidity__/codeforces_script/scripts/solidity_in_js_1.js) - variant of `solidity_in_js.js`

**Workflow yml (executes in CI):**
- [solidity__/codeforces_script/.github/workflows/main.yml](../solidity__/codeforces_script/.github/workflows/main.yml#L41) - `node scripts/solidity_in_js_1.js`

**Example:**
```bash
node scripts/solidity_in_js_1.js
```

---

## 3. **Direct Solidity Compiler CLI (No Hardhat, No JS Driver)**

### 3.1 solcjs --bin --abi \<file.sol\> (npm-Published solc Wrapper)
**Method:** `solcjs` is the JavaScript build of the Solidity compiler, published as the `solc` npm package. Invoked as a plain CLI: `solcjs --bin --abi <file.sol>` writes `<file>_sol_<Contract>.bin` (creation bytecode) and `<file>_sol_<Contract>.abi` (ABI JSON) to the cwd. No Hardhat, no Node driver script, no EVM — just the compiler.

**Locations:**
No `.sol` source is committed for this method; the workflow embeds a minimal `HelloWorld.sol` inline.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `npm install -g solc` then `solcjs --bin --abi HelloWorld.sol`

**Example:**
```bash
npm install -g solc
solcjs --bin --abi HelloWorld.sol
ls *.bin *.abi
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `npx hardhat compile` | Compile every `contracts/*.sol` | [Lock.sol](../solidity__/codeforces_script/contracts/Lock.sol) |
| `npx hardhat test` | Run Mocha/Chai test suite | [test/Lock.js](../solidity__/codeforces_script/test/Lock.js) |
| `npx hardhat node` | Long-running local EVM at :8545 | [main.yml#L32-L34](../solidity__/codeforces_script/.github/workflows/main.yml#L32-L34) |
| `npx hardhat run scripts/deploy.js --network localhost` | Deploy via ethers + Hardhat helpers | [deploy.js](../solidity__/codeforces_script/scripts/deploy.js) |
| `node scripts/solidity_in_js.js` | Inline `solc.compile` + ethers deploy | [solidity_in_js.js](../solidity__/codeforces_script/scripts/solidity_in_js.js) |
| `node scripts/solidity_in_js_1.js` | Variant of §2.1 | [solidity_in_js_1.js](../solidity__/codeforces_script/scripts/solidity_in_js_1.js) |
| `solcjs --bin --abi <file.sol>` | Direct CLI compile (no Hardhat, no JS driver) | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
