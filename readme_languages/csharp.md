# C# Execution Methods in Programming Languages Repository

This document catalogues **all distinct C# methods** discovered for compiling and running C# code throughout the repository. C# in this repo is .NET 6-based: project entry points use `dotnet build` / `dotnet run --project`, while ad-hoc scripts use the `dotnet-script` global tool (which exposes a `csi`-style file/heredoc runner without a project file).

## Table of Contents

1. **Project-Based Execution**
   - 1.1 [dotnet build \<Project.csproj\> (Compile a Project)](#11-dotnet-build-projectcsproj-compile-a-project)
   - 1.2 [dotnet run --project \<Project.csproj\> (Compile and Run)](#12-dotnet-run---project-projectcsproj-compile-and-run)

2. **Script-Based / Inline Execution**
   - 2.1 [dotnet tool install -g dotnet-script (Toolchain Setup)](#21-dotnet-tool-install--g-dotnet-script-toolchain-setup)
   - 2.2 [echo '…' | dotnet-script (Inline via Pipe)](#22-echo---dotnet-script-inline-via-pipe)
   - 2.3 [dotnet-script <<EOF (Heredoc Multi-Line Script)](#23-dotnet-script-eof-heredoc-multi-line-script)

3. **Polyglot Embedding (C# ➜ Other)**
   - 3.1 [ProcessStartInfo ➜ python -c "…" (C# ➜ Python)](#31-processstartinfo--python--c--c--python)

4. **Script-File Execution (dotnet-script)**
   - 4.1 [dotnet-script \<file.csx\> (Script File Mode)](#41-dotnet-script-filecsx-script-file-mode)

---

## 1. **Project-Based Execution**

### 1.1 dotnet build \<Project.csproj\> (Compile a Project)
**Method:** `dotnet build` reads the named `.csproj`, restores NuGet packages, runs the C# compiler, and produces a `bin/<Config>/<TFM>/<asm>.dll`. The CI uses .NET 6.0.

**Locations:**
- [csharp/codeforces_script/HelloWorld/HelloWorld.csproj](../csharp/codeforces_script/HelloWorld/HelloWorld.csproj) - `OutputType=Exe`, `TargetFramework=net6.0`, `StartupObject=HelloWorld.Program`
- [csharp/codeforces_script/HelloWorld/Program.cs](../csharp/codeforces_script/HelloWorld/Program.cs) - `Main(args)` entry point
- [csharp/codeforces_script/HelloWorld/first.cs](../csharp/codeforces_script/HelloWorld/first.cs) - companion class `HelloWorld.first`
- [csharp/codeforces_script/HelloWorld/child_process.cs](../csharp/codeforces_script/HelloWorld/child_process.cs) - companion class with `ProcessStartInfo` ➜ Python (see §3.1)
- [csharp/codeforces_script/standalone.cs](../csharp/codeforces_script/standalone.cs) - standalone single-file (commented-out `csc standalone.cs` route in the workflow)
  - Remote (submodule `csharp/codeforces_script` @ branch `c_sharp`): [HelloWorld/](https://github.com/aqwertyuiop48/codeforces_script/tree/c_sharp/HelloWorld)

**Workflow yml (executes in CI):**
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L18-L21) - `actions/setup-dotnet@v1` with `dotnet-version: '6.x'`
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L23-L24) - `dotnet build HelloWorld/HelloWorld.csproj`

Transitively exercised in CI via the following workflow(s):

- [.github/workflows/main.yml](../.github/workflows/main.yml#L127) — submodule sync that triggers the `c_sharp` branch run

**Example:**
```bash
dotnet build HelloWorld/HelloWorld.csproj
```

### 1.2 dotnet run --project \<Project.csproj\> (Compile and Run)
**Method:** Combines build + execute. Restores dependencies, compiles, then invokes the produced executable's `Main`. Most common way to run a .NET console app from source.

**Locations:**
Same project files as §1.1.

**Workflow yml (executes in CI):**
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L26-L27) - `dotnet run --project HelloWorld/HelloWorld.csproj`

**Example:**
```bash
dotnet run --project HelloWorld/HelloWorld.csproj
```

---

## 2. **Script-Based / Inline Execution**

### 2.1 dotnet tool install -g dotnet-script (Toolchain Setup)
**Method:** Install the `dotnet-script` global .NET tool, which provides a script runner for `.csx` files and inline / heredoc C# (no `.csproj` required). After install, the `dotnet-script` binary lives under `$HOME/.dotnet/tools` and must be on `$PATH`.

**Workflow yml (executes in CI):**
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L29-L30) - `echo "/home/runner/.dotnet" >> $GITHUB_PATH`
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L40-L43) - `dotnet tool install -g dotnet-script --version 1.5.0 && export PATH="$PATH:/home/runner/.dotnet/tools"`

**Example:**
```bash
dotnet tool install -g dotnet-script --version 1.5.0
export PATH="$PATH:$HOME/.dotnet/tools"
```

### 2.2 echo '…' | dotnet-script (Inline via Pipe)
**Method:** `dotnet-script` accepts C# source on stdin. Piping a single-line script through it is the closest equivalent to `node -e` / `python -c` — no file, no project.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L45-L47) - `echo 'Console.WriteLine("Hello from C# Interactive in GitHub Actions!");Environment.Exit(0);' | dotnet-script`

**Example:**
```bash
echo 'Console.WriteLine("Hi from C#!");Environment.Exit(0);' | dotnet-script
```

### 2.3 dotnet-script <<EOF (Heredoc Multi-Line Script)
**Method:** Same `dotnet-script` runner, fed a multi-line bash heredoc. Lets you declare `using` directives, classes, and a `public static void Main()` inline. Same `dotnet-script` reads the heredoc on stdin, compiles it as a script, and executes.

**Locations:**
None tracked as separate `.csx` files — the snippets are embedded directly inside the workflow.

**Workflow yml (executes in CI):**
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L49-L62) - `dotnet-script <<EOF` with `using System; public class Program { … } Program.Main(); Environment.Exit(0); EOF`

**Example:**
```bash
dotnet-script <<EOF
using System;
public class Program
{
    public static void Main()
    {
        Console.WriteLine("Hello from C# heredoc!");
    }
}
Program.Main();
Environment.Exit(0);
EOF
```

---

## 3. **Polyglot Embedding (C# ➜ Other)**

### 3.1 ProcessStartInfo ➜ python -c "…" (C# ➜ Python)
**Method:** Use `System.Diagnostics.ProcessStartInfo` with `FileName = "python"` and `Arguments = "-c \"<py code>\""` to spawn a Python subprocess. `RedirectStandardOutput = true` + `UseShellExecute = false` lets the parent C# program capture the child's stdout/stderr. Canonical C# → Python polyglot.

**Locations:**
- [csharp/codeforces_script/HelloWorld/child_process.cs](../csharp/codeforces_script/HelloWorld/child_process.cs) - full `ProcessStartInfo` pattern; multi-line Python string with `print(f'The sum of {x} and {y} is {x + y}.')`
- [csharp/codeforces_script/HelloWorld/Program.cs](../csharp/codeforces_script/HelloWorld/Program.cs#L9-L11) - `Main` calls `child_process.Main();` so the polyglot fires during `dotnet run`

**Workflow yml (executes in CI):**
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L26-L27) - the project-mode `dotnet run --project HelloWorld/HelloWorld.csproj` invokes `Program.Main` → `child_process.Main` → spawns `python -c`
- [csharp/codeforces_script/.github/workflows/main.yml](../csharp/codeforces_script/.github/workflows/main.yml#L64-L111) - **also** exercised heredoc-style via `dotnet-script <<EOF … ProcessStartInfo … python -c … EOF` (the multi-line nested-C# step)

**Example:**
```csharp
using System.Diagnostics;
var psi = new ProcessStartInfo {
    FileName = "python",
    Arguments = "-c \"print('Hello from Python!')\"",
    RedirectStandardOutput = true,
    UseShellExecute = false,
    CreateNoWindow = true
};
using var p = Process.Start(psi);
Console.WriteLine(p.StandardOutput.ReadToEnd());
p.WaitForExit();
```

---

## 4. **Script-File Execution (dotnet-script)**

### 4.1 dotnet-script \<file.csx\> (Script File Mode)
**Method:** The same `dotnet-script` tool used in §2.2 / §2.3 also accepts a `.csx` file path as its positional argument. Unlike heredoc/pipe modes, a `.csx` file can be syntax-highlighted by editors, version-controlled separately, and re-run repeatedly without re-typing.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest2_.yml](../.github/workflows/pytest2_.yml) - `dotnet tool install -g dotnet-script` then `dotnet-script /tmp/hello.csx`

**Example:**
```csharp
// hello.csx
using System;
using System.Linq;
Console.WriteLine("Hello from dotnet-script file!");
var nums = new[] { 1, 2, 3, 4, 5 };
Console.WriteLine($"Sum: {nums.Sum()}");
```
```bash
dotnet-script hello.csx
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| `dotnet build <Project.csproj>` | Compile a .NET project | [HelloWorld.csproj](../csharp/codeforces_script/HelloWorld/HelloWorld.csproj) |
| `dotnet run --project <…>` | Compile + execute a .NET project | [main.yml#L26](../csharp/codeforces_script/.github/workflows/main.yml#L26) |
| `dotnet tool install -g dotnet-script` | Script-runner toolchain | [main.yml#L40-L43](../csharp/codeforces_script/.github/workflows/main.yml#L40-L43) |
| `echo '…' \| dotnet-script` | Inline C# via stdin pipe | [main.yml#L45-L47](../csharp/codeforces_script/.github/workflows/main.yml#L45-L47) |
| `dotnet-script <<EOF … EOF` | Multi-line C# heredoc | [main.yml#L49-L62](../csharp/codeforces_script/.github/workflows/main.yml#L49-L62) |
| `ProcessStartInfo` → `python -c` | C# → Python polyglot | [child_process.cs](../csharp/codeforces_script/HelloWorld/child_process.cs) |
| `dotnet-script <file.csx>` | Script-file mode | [pytest2_.yml](../.github/workflows/pytest2_.yml) |
