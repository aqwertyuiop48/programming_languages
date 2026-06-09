# Python Execution Methods in Programming Languages Repository

This document catalogues **all distinct Python execution methods** discovered for running `.py` source and `.ipynb` Python notebooks throughout the repository.

Each method takes Python source as input and produces the program's output. Toolchain provisioning (`actions/setup-python`, `UsePythonVersion@0`), package installation (`pip install`, `pip install -e .`), virtual-environment bootstrap (`python -m venv`), notebook conversion (`jupyter nbconvert --to html`), serverless deployment configs (`@vercel/python`), and notebook executions whose kernel is not Python (`jupyter nbconvert --ExecutePreprocessor.kernel_name=kotlin`, `jupyter-console --kernel=java/kotlin`) are **not** Python execution methods and are excluded. If a single command takes Python source and produces program output, it counts as one method.

## Table of Contents

1. **Direct Script Execution**
   - 1.1 [python <file.py> (CPython interpreter)](#11-python-filepy-cpython-interpreter)
   - 1.2 [python3 <file.py> < input (with stdin redirection)](#12-python3-filepy--input-with-stdin-redirection)
   - 1.3 [./script.py via shebang (`#!/usr/bin/env python`)](#13-scriptpy-via-shebang-usrbinenv-python)
   - 1.4 [ipython <file.py> (IPython interpreter & kernel)](#14-ipython-filepy-ipython-interpreter--kernel)

2. **Inline / REPL Execution**
   - 2.1 [python -c "..." (Inline Expression)](#21-python--c--inline-expression)
   - 2.2 [python3 - <<EOF (Stdin Heredoc)](#22-python3---eof-stdin-heredoc)
   - 2.3 [ipython -c "..." (Inline IPython Expression)](#23-ipython--c--inline-ipython-expression)
   - 2.4 [ipython <<EOF (IPython Stdin Heredoc)](#24-ipython-eof-ipython-stdin-heredoc)
   - 2.5 [python -m \<module\> (Stdlib Module Runner)](#25-python--m-module-stdlib-module-runner)
   - 2.6 [echo '...' | python (Piped REPL Stdin)](#26-echo---python-piped-repl-stdin)

3. **Test Runners**
   - 3.1 [python -m unittest discover (Stdlib Test Runner)](#31-python--m-unittest-discover-stdlib-test-runner)
   - 3.2 [pytest / python -m pytest (Pytest)](#32-pytest--python--m-pytest-pytest)
   - 3.3 [behave (Behave BDD)](#33-behave-behave-bdd)
   - 3.4 [pytest-bdd (Gherkin via pytest)](#34-pytest-bdd-gherkin-via-pytest)
   - 3.5 [robot <file.robot> (Robot Framework)](#35-robot-filerobot-robot-framework)

4. **Web Frameworks & Servers**
   - 4.1 [python manage.py runserver (Django dev server)](#41-python-managepy-runserver-django-dev-server)
   - 4.2 [flask run (Flask CLI)](#42-flask-run-flask-cli)
   - 4.3 [uvicorn (ASGI server for FastAPI)](#43-uvicorn-asgi-server-for-fastapi)

5. **Task Queues**
   - 5.1 [celery -A tasks worker (Celery Worker)](#51-celery--a-tasks-worker-celery-worker)

6. **Jupyter Notebooks**
   - 6.1 [jupyter nbconvert --to notebook --execute (Headless Python Notebook Run)](#61-jupyter-nbconvert---to-notebook---execute-headless-python-notebook-run)
   - 6.2 [IPython Kernel Magics (!shell, %%writefile, %magic)](#62-ipython-kernel-magics-shell-writefile-magic)
   - 6.3 [jupyter-console --kernel=python3 <<EOF (Inline Python via Jupyter Console)](#63-jupyter-console---kernelpython3-eof-inline-python-via-jupyter-console)

7. **Containerized Execution**
   - 7.1 [Docker (Python inside a container image)](#71-docker-python-inside-a-container-image)

8. **Embedded / Polyglot Execution**
   - 8.1 [Node.js `child_process.spawn('python3', ...)` (JS → Python)](#81-nodejs-child_processspawnpython3--js--python)
   - 8.2 [Clojure `(clojure.java.shell/sh "python3" "-c" code)` (CLJ → Python)](#82-clojure-clojurejavashellsh-python3--c-code-clj--python)

---

## 1. **Direct Script Execution**

### 1.1 python <file.py> (CPython interpreter)
**Method:** Run a Python script with the `python` (or `python3`) command. The interpreter compiles to bytecode and executes in one step.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [Python/codeforces_script/.github/workflows/main.yml](../Python/codeforces_script/.github/workflows/main.yml#L76) - `python test.py`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L76](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L76)
- [Python/codeforces_script/.github/workflows/main2.yml](../Python/codeforces_script/.github/workflows/main2.yml#L32) - `python shell_script.py`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main2.yml#L32](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main2.yml#L32)
- [.github/workflows/cybersecurity_.yml](../.github/workflows/cybersecurity_.yml#L29) - `python vulnerability_scanner.py --ip 127.0.0.1 ...`
- [.github/workflows/big_data.yml](../.github/workflows/big_data.yml#L40) - `python big_data_processing.py`
- [.github/workflows/tesseract_.yml](../.github/workflows/tesseract_.yml#L31) - `python tess.py`
- [.github/workflows/celery_.yml](../.github/workflows/celery_.yml#L37) - `python worker_test.py`

**Example:**
```bash
python test.py
python vulnerability_scanner.py --ip 127.0.0.1 --start-port 1 --end-port 1024
```

### 1.2 python3 <file.py> < input (with stdin redirection)
**Method:** Pipe a fixture file into a Python script's stdin via shell redirection. Used by the codeforces runner to feed problem input.

**Locations:**
- [Python/codeforces_script/direct/run.sh](../Python/codeforces_script/direct/run.sh#L40-L41) - `python3 "$script" < "$f" | tee "py_output/${testname}.txt"`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/direct/run.sh#L40-L41](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L40-L41)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
python3 1A.py < test1.in
```

### 1.3 ./script.py via shebang (`#!/usr/bin/env python`)
**Method:** A script whose first line declares a Python interpreter (`#!/usr/bin/env python`) and which has the executable bit can be run directly. Django's `manage.py` entrypoint uses this.

**Locations:**
- [Python/django_/vercel_django_example1/manage.py](../Python/django_/vercel_django_example1/manage.py#L1) - `#!/usr/bin/env python`
  - Remote (submodule `Python/django_/vercel_django_example1` @ branch `main`): [Python/django_/vercel_django_example1/manage.py#L1](https://github.com/aqwertyuiop48/vercel_django_example1/blob/main/manage.py#L1)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
chmod +x manage.py
./manage.py runserver
```

### 1.4 ipython <file.py> (IPython interpreter & kernel)
**Method:** Run Python source under [IPython](https://ipython.org/) rather than plain CPython. IPython is the kernel that powers every `.ipynb` Python notebook in this repo and is also a standalone interpreter (`ipython <file.py>`, `ipython -c "..."`, or `ipython` for an enhanced REPL). When `jupyter nbconvert --execute` (§6.1) runs a Python notebook, it dispatches every cell to this same IPython kernel.

What distinguishes IPython execution from plain `python` is access to the IPython runtime API and shell/magic syntax (§6.2). Repo-internal proof points:

- The `ipython` kernel must be installed into the active env before notebooks can run — the canonical command for that is recorded in [Python/readme.ipynb#L1296](../Python/readme.ipynb#L1296) (`ipython kernel install --user --name=venv`).
- A real `.py` source file in the repo calls the IPython runtime API directly: [Python/algorithms/Python/machine_learning/logistic_regression.py#L22](../Python/algorithms/Python/machine_learning/logistic_regression.py#L22) (`get_ipython().run_line_magic('matplotlib', 'inline')`) — this line only executes when the file is run under IPython (`ipython logistic_regression.py`) or imported into an IPython session; under plain `python` it would raise `NameError`.
- Every Python notebook in the repo declares `"name": "ipython"` in its kernel metadata, e.g. [javascript/readme.ipynb#L2142](../javascript/readme.ipynb#L2142) and [java/temporal/edu-101-java-code/temporal_.ipynb#L1574](../java/temporal/edu-101-java-code/temporal_.ipynb#L1574) — i.e. their entire executable content is run by IPython.

**Locations:**
- [Python/readme.ipynb](../Python/readme.ipynb#L1296) - `ipython kernel install --user --name=venv`
- [Python/algorithms/Python/machine_learning/logistic_regression.py](../Python/algorithms/Python/machine_learning/logistic_regression.py#L22) - `get_ipython().run_line_magic('matplotlib', 'inline')`
  - Remote (submodule `Python/algorithms/Python` @ branch `master`): [Python/algorithms/Python/machine_learning/logistic_regression.py#L22](https://github.com/aqwertyuiop48/Python/blob/master/machine_learning/logistic_regression.py#L22)
- [Python/codeforces_script/execute/ipython_.ipynb](../Python/codeforces_script/execute/ipython_.ipynb) - sample notebook of IPython cells (`!ls`, `!pwd`, `print(2222)`)
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/ipython_.ipynb](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/ipython_.ipynb)
- [javascript/readme.ipynb](../javascript/readme.ipynb#L2142) - `"name": "ipython"` kernel metadata
- [java/temporal/edu-101-java-code/temporal_.ipynb](../java/temporal/edu-101-java-code/temporal_.ipynb#L1574) - `"name": "ipython"` kernel metadata
  - Remote (submodule `java/temporal/edu-101-java-code` @ branch `main`): [java/temporal/edu-101-java-code/temporal_.ipynb#L1574](https://github.com/aqwertyuiop48/edu-101-java-code/blob/main/temporal_.ipynb#L1574)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
ipython kernel install --user --name=venv      # register the IPython kernel
ipython Python/algorithms/Python/machine_learning/logistic_regression.py
ipython -c "print(2 + 3); !ls"                 # inline, with shell escape
ipython                                        # enhanced REPL
```

---

## 2. **Inline / REPL Execution**

### 2.1 python -c "..." (Inline Expression)
**Method:** Evaluate a Python statement / expression supplied as a shell argument. Used heavily as the polyglot bridge from JS, Go, and shell into Python.

**Locations:**
- [typescript/inputs/final_inputs.js](../typescript/inputs/final_inputs.js#L6) - `spawn("python", ["-c", \`...\`])` (Node → Python inline)
- [javascript/mysql_connector_/mysql_connector_.js](../javascript/mysql_connector_/mysql_connector_.js#L44) - `spawnSync('python3', ['-c', pythonCode], ...)`
- [golang/6_go_movies_crud_1/main_2.js](../golang/6_go_movies_crud_1/main_2.js#L117) - `runner.execFile("python", ["-c", python_go_main], ...)`

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
python -c "print(2 + 3)"
```

### 2.2 python3 - <<EOF (Stdin Heredoc)
**Method:** Pass `-` as the script argument so the interpreter reads source from stdin; supply a shell heredoc to inline a multi-line Python program.

**Locations:**
- [clojure_/codeforces_script/testing.sh](../clojure_/codeforces_script/testing.sh#L5) - `python3 - <<'EOF'` (shell heredoc piping multi-line Python)
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/testing.sh#L5](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.sh#L5)
- [clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj](../clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L6) - `"python3 - <<'EOF'..."` (Clojure-built shell string)
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L6](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/src/my_clojure_app/cc.clj#L6)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
python3 - <<'EOF'
import sys
print("Multi-line raw Python!")
print("sys.version:", sys.version)
EOF
```

### 2.3 ipython -c "..." (Inline IPython Expression)
**Method:** Evaluate one or more Python statements supplied as a shell argument under the IPython interpreter (§1.4) rather than plain CPython. Unlike `python -c` (§2.1), the code runs inside an IPython session and therefore has access to the IPython runtime (`get_ipython()`, line/cell magics, shell escapes like `!ls`).

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L56-L59) - `ipython -c 'print("Hello from IPython inline!"); import sys; print("Python version: " + sys.version); ...'`

**Example:**
```bash
pip install ipython
ipython -c 'print("Hello from IPython inline!"); import sys; print(sys.version)'
ipython -c 'print(2 + 3); !ls'   # shell escape works because we're in IPython
```

### 2.4 ipython <<EOF (IPython Stdin Heredoc)
**Method:** Pipe a shell heredoc straight into `ipython`'s stdin so a multi-line Python program is fed to the IPython interpreter in one step. Direct IPython analog of `python3 - <<EOF` (§2.2) and the inline cousin of `jupyter-console --kernel=python3 <<EOF` (§6.3); the difference vs §6.3 is no kernel client/server hop — IPython evaluates stdin directly.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L63-L71) - `ipython <<EOF ... print("Hello from IPython inline again!") ... EOF`

**Example:**
```bash
pip install ipython

ipython <<EOF
print("Hello from IPython inline again!")
import sys
print("Python version: " + sys.version)
print("Python platform: " + sys.platform)
print("Python home: " + sys.prefix)
EOF
```

### 2.5 python -m \<module\> (Stdlib Module Runner)
**Method:** Run a stdlib (or installed) module as `__main__` via Python's `-m` flag. The general form of `python -m unittest discover` (§3.1) and `python -m pytest` (§3.2) — here applied to introspection / micro-benchmark modules (`timeit`, `platform`, `site`) bundled with CPython.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L174-L178) - `python -m timeit -n 1000 -s 'x = list(range(100))' 'sum(x)'`, `python -m platform`, `python -m site`

**Example:**
```bash
python -m timeit -n 1000 -s 'x = list(range(100))' 'sum(x)'
python -m platform
python -m site
```

### 2.6 echo '...' | python (Piped REPL Stdin)
**Method:** Pipe a Python program into the interpreter's stdin via a shell pipe — the lightweight sibling of §2.2's heredoc. CPython reads the program from stdin when no script path is supplied, so any shell command whose stdout is Python source can drive an interpreter run in one line.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L180-L182) - `echo 'import sys; print("Hello from python via pipe!"); print("Python version: " + sys.version)' | python`

**Example:**
```bash
echo 'import sys; print("Hello!"); print(sys.version)' | python
printf '%s\n' 'for i in range(3):' '    print(i)' | python
```

---

## 3. **Test Runners**

### 3.1 python -m unittest discover (Stdlib Test Runner)
**Method:** Use the `unittest` module's CLI to discover and run all matching test modules under a directory. End-to-end: discovers, loads, runs, reports.

**Locations:**
- [Python/PyUnit_/test_example.py](../Python/PyUnit_/test_example.py#L17) - `if __name__ == "__main__": unittest.main()` (also runnable via `python test_example.py`)

**Workflow yml (executes in CI):**
- [.github/workflows/pyunit_.yml](../.github/workflows/pyunit_.yml#L26) - `python -m unittest discover -s . -p "test_*.py"`
- [Python/codeforces_script/.github/workflows/selenium_.yml](../Python/codeforces_script/.github/workflows/selenium_.yml#L63) - `python -m unittest discover -s Python/selenium_ -p local_.py`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/selenium_.yml#L63](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/selenium_.yml#L63)

**Example:**
```bash
python -m unittest discover -s . -p "test_*.py"
```

### 3.2 pytest / python -m pytest (Pytest)
**Method:** Run [pytest](https://docs.pytest.org/) directly — it auto-discovers `test_*.py` / `*_test.py` files and runs functions named `test_*`. The `python -m pytest` form forces the same interpreter as the active environment.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L27) - `pytest && python app/decorator.py && pytest -m slow`
- [Python/selenium_/selenium_python_example/.github/workflows/daily-test.yml](../Python/selenium_/selenium_python_example/.github/workflows/daily-test.yml#L36) - `pytest -v --html=results/report.html`
  - Remote (submodule `Python/selenium_/selenium_python_example` @ branch `main`): [Python/selenium_/selenium_python_example/.github/workflows/daily-test.yml#L36](https://github.com/aqwertyuiop48/selenium_python_example/blob/main/.github/workflows/daily-test.yml#L36)
- [Python/selenium_/SeleniumBase/azure-pipelines.yml](../Python/selenium_/SeleniumBase/azure-pipelines.yml#L69) - `python -m pytest examples/boilerplates/boilerplate_test.py --browser=chrome --headless ...`
  - Remote (submodule `Python/selenium_/SeleniumBase` @ branch `main`): [Python/selenium_/SeleniumBase/azure-pipelines.yml#L69](https://github.com/aqwertyuiop48/SeleniumBase/blob/main/azure-pipelines.yml#L69)

**Example:**
```bash
pytest                       # discover and run all tests
pytest -m slow               # filter by marker
pytest -v --html=report.html # verbose + HTML report
python -m pytest tests/      # equivalent, pinned to active interpreter
```

### 3.3 behave (Behave BDD)
**Method:** Run [behave](https://behave.readthedocs.io/) against `.feature` files and step-definition modules under `features/steps/`. End-to-end: parses Gherkin, dispatches to Python steps, executes, reports.

**Locations:**
- [Python/cucumber_/features/steps/api_steps.py](../Python/cucumber_/features/steps/api_steps.py#L2) - `from behave import given, when, then`
- [Python/cucumber_/features/api.feature](../Python/cucumber_/features/api.feature) - Gherkin feature

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L35) - `behave`

**Example:**
```bash
cd Python/cucumber_
behave
```

### 3.4 pytest-bdd (Gherkin via pytest)
**Method:** [pytest-bdd](https://pytest-bdd.readthedocs.io/) wires `.feature` files into pytest test functions; invoked via `pytest`.

**Locations:**
- [Python/cucumber_/features/test_api_pytest_bdd_steps.py](../Python/cucumber_/features/test_api_pytest_bdd_steps.py#L1-L6) - `from pytest_bdd import scenarios, given, when, then`

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L38) - `pytest` after `pip install ... pytest-bdd`

**Example:**
```python
from pytest_bdd import scenarios, given, when, then
scenarios('./api.feature')
```
```bash
pytest    # runs the .feature via pytest-bdd
```

### 3.5 robot <file.robot> (Robot Framework)
**Method:** Run a [Robot Framework](https://robotframework.org/) `.robot` test suite with the `robot` CLI; pulls in libraries declared under `*** Settings ***`.

**Locations:**
- [Python/robot_/search_test.robot](../Python/robot_/search_test.robot#L1-L5) - `*** Settings ***` block declaring `RequestsLibrary`
- [Python/robot_/requirements.txt](../Python/robot_/requirements.txt#L1) - `robotframework==...`

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
robot search_test.robot
```

---

## 4. **Web Frameworks & Servers**

### 4.1 python manage.py runserver (Django dev server)
**Method:** Django's bootstrap CLI runs `manage.py`, which dispatches `runserver` to launch the dev WSGI server using `vercel_app/settings.py`.

**Locations:**
- [Python/django_/vercel_django_example1/manage.py](../Python/django_/vercel_django_example1/manage.py#L1-L22) - `execute_from_command_line(sys.argv)`
  - Remote (submodule `Python/django_/vercel_django_example1` @ branch `main`): [Python/django_/vercel_django_example1/manage.py#L1-L22](https://github.com/aqwertyuiop48/vercel_django_example1/blob/main/manage.py#L1-L22)
- [Python/django_/vercel_django_example1/readme.md](../Python/django_/vercel_django_example1/readme.md#L12) - `python manage.py runserver`

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
python manage.py runserver
```

### 4.2 flask run (Flask CLI)
**Method:** Flask CLI inspects `FLASK_APP` (env var) and starts the app's dev WSGI server. Source is a `.py` module exposing a `Flask` instance.

**Locations:**
- [Python/flask_/vercel_flask_app/README.md](../Python/flask_/vercel_flask_app/README.md#L19) - `FLASK_APP=index.py flask run`
  - Remote (submodule `Python/flask_/vercel_flask_app` @ branch `main`): [Python/flask_/vercel_flask_app/README.md#L19](https://github.com/aqwertyuiop48/vercel_flask_app/blob/main/README.md#L19)
- [Python/flask_/flask_pythonanywhere/readme2.md](../Python/flask_/flask_pythonanywhere/readme2.md#L29) - `FLASK_APP=flask_app.py flask run`
  - Remote (submodule `Python/flask_/flask_pythonanywhere` @ branch `main`): [Python/flask_/flask_pythonanywhere/readme2.md#L29](https://github.com/aqwertyuiop48/flask_pythonanywhere/blob/main/readme2.md#L29)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
FLASK_APP=index.py flask run
```

### 4.3 uvicorn (ASGI server for FastAPI)
**Method:** [uvicorn](https://www.uvicorn.org/) runs an ASGI application; invoked either via the CLI (`uvicorn module:app`) or programmatically (`uvicorn.run(...)`).

**Locations:**
- [Python/fastapi_/fastapi_python/main.py](../Python/fastapi_/fastapi_python/main.py#L1-L4) - `import uvicorn; uvicorn.run("server.api:app", host="0.0.0.0", port=8001, reload=True)`
  - Remote (submodule `Python/fastapi_/fastapi_python` @ branch `main`): [Python/fastapi_/fastapi_python/main.py#L1-L4](https://github.com/aqwertyuiop48/fastapi_python/blob/main/main.py#L1-L4)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```bash
python main.py                                           # runs uvicorn.run(...) programmatically
uvicorn server.api:app --reload --host 0.0.0.0 --port 8001
```

---

## 5. **Task Queues**

### 5.1 celery -A tasks worker (Celery Worker)
**Method:** Run a [Celery](https://docs.celeryq.dev/) worker process bound to the `tasks` app; the broker / backend are declared inside the app module. End-to-end: loads source, registers tasks, starts worker.

**Locations:**
- [Python/celery_/tasks.py](../Python/celery_/tasks.py#L1-L6) - `from celery import Celery; app = Celery('tasks', broker='redis://localhost:6379/0')`

**Workflow yml (executes in CI):**
- [.github/workflows/celery_.yml](../.github/workflows/celery_.yml#L36) - `nohup celery -A tasks worker --loglevel=info &`

**Example:**
```bash
celery -A tasks worker --loglevel=info
```

---

## 6. **Jupyter Notebooks**

### 6.1 jupyter nbconvert --to notebook --execute (Headless Python Notebook Run)
**Method:** Execute every cell in a `.ipynb` file with the Python kernel and write the executed version out. Single command takes notebook source → produces output.

**Locations:**
- [Python/codeforces_script/execute/notebook.ipynb](../Python/codeforces_script/execute/notebook.ipynb) - the input notebook
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook.ipynb](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook.ipynb)

**Workflow yml (executes in CI):**
- [Python/codeforces_script/.github/workflows/main.yml](../Python/codeforces_script/.github/workflows/main.yml#L81) - `jupyter nbconvert --to notebook --execute notebook.ipynb --output executed_notebook.ipynb`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main.yml#L81](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L81)
- [Python/codeforces_script/.github/workflows/main_release.yml](../Python/codeforces_script/.github/workflows/main_release.yml#L47) - same pattern targeting `outputs/`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/main_release.yml#L47](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main_release.yml#L47)

**Example:**
```bash
jupyter nbconvert --to notebook --execute notebook.ipynb \
  --output executed_notebook.ipynb
```

### 6.2 IPython Kernel Magics (!shell, %%writefile, %magic)
**Method:** Cells inside an `.ipynb` are executed by the IPython kernel, which extends standard Python with three syntaxes that still count as a single source→output step when the notebook is run:

- `!cmd` — shell escape (runs `cmd` in a subshell and captures output).
- `%line_magic` — single-line magic (e.g. `%time`, `%matplotlib`, `%load_ext`); programmatic form is `get_ipython().run_line_magic('name', 'args')`.
- `%%cell_magic` — whole-cell magic (e.g. `%%writefile filename` writes the rest of the cell to disk; `%%bash` runs the cell as bash).

When executed via `jupyter nbconvert --execute` (§6.1) or directly in the notebook UI, the IPython kernel evaluates these alongside regular Python in one pass.

**Locations:**
- [Python/codeforces_script/execute/notebook.ipynb](../Python/codeforces_script/execute/notebook.ipynb#L18) - `%%writefile HelloWorld.java` cell magic
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook.ipynb#L18](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook.ipynb#L18)
- [Python/codeforces_script/execute/notebook.ipynb](../Python/codeforces_script/execute/notebook.ipynb#L32-L33) - `!javac HelloWorld.java` / `!java HelloWorld` shell escapes
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook.ipynb#L32-L33](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook.ipynb#L32-L33)
- [Python/codeforces_script/execute/notebook.ipynb](../Python/codeforces_script/execute/notebook.ipynb#L42) - second `%%writefile HelloWorld1.java` cell
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook.ipynb#L42](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook.ipynb#L42)
- [Python/codeforces_script/execute/notebook.ipynb](../Python/codeforces_script/execute/notebook.ipynb#L75-L76) - `!javac HelloWorld1.java` / `!java HelloWorld1`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/notebook.ipynb#L75-L76](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook.ipynb#L75-L76)
- [Python/codeforces_script/execute/ipython_.ipynb](../Python/codeforces_script/execute/ipython_.ipynb) - sample cells: `!ls`, `!pwd`, `print(2222)`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/execute/ipython_.ipynb](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/ipython_.ipynb)
- [Python/algorithms/Python/machine_learning/logistic_regression.py](../Python/algorithms/Python/machine_learning/logistic_regression.py#L22) - `get_ipython().run_line_magic('matplotlib', 'inline')` (the programmatic form of `%matplotlib inline`)
  - Remote (submodule `Python/algorithms/Python` @ branch `master`): [Python/algorithms/Python/machine_learning/logistic_regression.py#L22](https://github.com/aqwertyuiop48/Python/blob/master/machine_learning/logistic_regression.py#L22)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example (notebook cells executed by `nbconvert --execute`):**
```python
# cell 1 — cell magic that writes a file
%%writefile HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello from %%writefile!");
    }
}

# cell 2 — shell escapes that compile + run it
!javac HelloWorld.java
!java HelloWorld

# cell 3 — line magic + programmatic equivalent
%matplotlib inline
get_ipython().run_line_magic('matplotlib', 'inline')
```

### 6.3 jupyter-console --kernel=python3 <<EOF (Inline Python via Jupyter Console)
**Method:** [`jupyter-console`](https://jupyter-console.readthedocs.io/) opens a terminal frontend to a Jupyter kernel. With `--kernel=python3` (the default IPython kernel) and a shell heredoc, a full multi-line Python program is fed in and executed in a single step — source → kernel → output. Direct Python analog of java.md §5.1's `jupyter-console --kernel=java <<EOF`.

**Locations:**
None tracked outside the workflow citations below.

**Workflow yml (executes in CI):**
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L44-L52) - `jupyter-console --kernel=python3 <<EOF ... print("Hello from Python inline!") ... EOF`
- [.github/workflows/pytest_.yml](../.github/workflows/pytest_.yml#L42) - `pip install jupyter jupyter_client jupyter-console` (prereq)

**Example:**
```bash
pip install jupyter jupyter_client jupyter-console

jupyter-console --kernel=python3 <<EOF
print("Hello from Python inline!")
import sys
print("Python version: " + sys.version)
print("Python platform: " + sys.platform)
print("Python home: " + sys.prefix)
EOF
```

---

## 7. **Containerized Execution**

### 7.1 Docker (Python inside a container image)
**Method:** Build a Docker image whose `Dockerfile` installs Python and copies in source; run the image and the entrypoint executes the Python program. End-to-end: source → image → running program → artifacts copied out.

**Locations:**
- [Python/codeforces_script/Dockerfile](../Python/codeforces_script/Dockerfile#L1-L2) - `FROM ubuntu:20.04`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/Dockerfile#L1-L2](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/Dockerfile#L1-L2)

**Workflow yml (executes in CI):**
- [Python/codeforces_script/.github/workflows/docker_.yml](../Python/codeforces_script/.github/workflows/docker_.yml#L20-L26) - `docker build -t my-app .` then `docker run --name my-container -d my-app`
  - Remote (submodule `Python/codeforces_script` @ branch `python_`): [Python/codeforces_script/.github/workflows/docker_.yml#L20-L26](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/docker_.yml#L20-L26)

**Example:**
```bash
docker build -t my-app .
docker run --name my-container -d my-app
docker cp my-container:/app/output/output.txt ./output.txt
```

---

## 8. **Embedded / Polyglot Execution**

### 8.1 Node.js `child_process.spawn('python3', ...)` (JS → Python)
**Method:** Node.js spawns CPython (with `-c` for inline source or a file path) to delegate computation to Python. Single end-to-end call: Python source string → program output captured via stdout.

**Locations:**
- [javascript/main_/codeforces_script/fixed_code.js](../javascript/main_/codeforces_script/fixed_code.js#L9) - `const pythonProcess = spawn('python3', pythonArgs);`
- [javascript/mysql_connector_/mysql_connector_.js](../javascript/mysql_connector_/mysql_connector_.js#L44) - `spawnSync('python3', ['-c', pythonCode], { ... })`
- [typescript/inputs/final_inputs.js](../typescript/inputs/final_inputs.js#L6-L70) - multiple `spawn("python", ["-c", \`...\`])` calls
- [golang/6_go_movies_crud_1/main_2.js](../golang/6_go_movies_crud_1/main_2.js#L117) - `runner.execFile("python", ["-c", python_go_main], ...)`

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```javascript
const { spawn } = require('child_process');
const child = spawn('python3', ['-c', 'print("from Node")']);
child.stdout.on('data', d => process.stdout.write(d));
```

### 8.2 Clojure `(clojure.java.shell/sh "python3" "-c" code)` (CLJ → Python)
**Method:** Clojure builds a Python program as a string and dispatches it to `python3 -c` via `clojure.java.shell`. Output is returned in the `:out` key of the resulting map.

**Locations:**
- [clojure_/codeforces_script/testing.sh](../clojure_/codeforces_script/testing.sh#L23-L32) - `(def python-code ...) (def result (shell/sh "python3" "-c" python-code))`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/testing.sh#L23-L32](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.sh#L23-L32)
- [clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj](../clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L46-L65) - `(def python-code ...) (def result (shell/sh "python3" "-c" python-code))`
  - Remote (submodule `clojure_/codeforces_script` @ branch `clojure_`): [clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L46-L65](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/src/my_clojure_app/cc.clj#L46-L65)

**Workflow yml (executes in CI):**
None — no GitHub Actions workflow exercises this method end-to-end in this repository. Invoked manually per the example below.

**Example:**
```clojure
(require '[clojure.java.shell :as shell])
(def python-code "print(2 + 3)")
(def result (shell/sh "python3" "-c" python-code))
(println (:out result))
```

---

## Summary Table

| Method | Primary Use | Example Location |
|--------|-------------|-------------------|
| python <file.py> | Run script | [main.yml](../Python/codeforces_script/.github/workflows/main.yml#L76)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L76) |
| python3 <file> < input | Stdin redirection | [run.sh](../Python/codeforces_script/direct/run.sh#L41)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/direct/run.sh#L41) |
| ./script.py (shebang) | Executable script | [manage.py](../Python/django_/vercel_django_example1/manage.py#L1)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/vercel_django_example1/blob/main/manage.py#L1) |
| ipython <file.py> | IPython interpreter & kernel | [logistic_regression.py](../Python/algorithms/Python/machine_learning/logistic_regression.py#L22)<br/>[remote @ `master`](https://github.com/aqwertyuiop48/Python/blob/master/machine_learning/logistic_regression.py#L22) |
| python -c "..." | Inline expression | [mysql_connector_.js](../javascript/mysql_connector_/mysql_connector_.js#L44) |
| python3 - <<EOF | Stdin heredoc | [testing.sh](../clojure_/codeforces_script/testing.sh#L5)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/testing.sh#L5) |
| python -m unittest | Stdlib test runner | [pyunit_.yml](../.github/workflows/pyunit_.yml#L26) |
| pytest / python -m pytest | Pytest runner | [pytest_.yml](../.github/workflows/pytest_.yml#L27) |
| behave | BDD runner | [pytest_.yml](../.github/workflows/pytest_.yml#L35) |
| pytest-bdd | BDD via pytest | [test_api_pytest_bdd_steps.py](../Python/cucumber_/features/test_api_pytest_bdd_steps.py#L1) |
| robot <file.robot> | Robot Framework | [search_test.robot](../Python/robot_/search_test.robot#L1) |
| python manage.py runserver | Django dev server | [manage.py](../Python/django_/vercel_django_example1/manage.py#L1)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/vercel_django_example1/blob/main/manage.py#L1) |
| flask run | Flask CLI | [README.md](../Python/flask_/vercel_flask_app/README.md#L19)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/vercel_flask_app/blob/main/README.md#L19) |
| uvicorn | ASGI server | [main.py](../Python/fastapi_/fastapi_python/main.py#L1)<br/>[remote @ `main`](https://github.com/aqwertyuiop48/fastapi_python/blob/main/main.py#L1) |
| celery -A worker | Task queue | [celery_.yml](../.github/workflows/celery_.yml#L36) |
| jupyter nbconvert --execute | Run Python notebook | [main.yml](../Python/codeforces_script/.github/workflows/main.yml#L81)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/main.yml#L81) |
| IPython !shell / %%magic / %magic | Notebook cell magics | [notebook.ipynb](../Python/codeforces_script/execute/notebook.ipynb#L18)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/execute/notebook.ipynb#L18) |
| jupyter-console --kernel=python3 <<EOF | Inline Python via Jupyter console | [pytest_.yml](../.github/workflows/pytest_.yml#L44-L52) |
| docker build + run | Container | [docker_.yml](../Python/codeforces_script/.github/workflows/docker_.yml#L20)<br/>[remote @ `python_`](https://github.com/aqwertyuiop48/codeforces_script/blob/python_/.github/workflows/docker_.yml#L20) |
| Node spawn('python3') | JS → Python | [fixed_code.js](../javascript/main_/codeforces_script/fixed_code.js#L9) |
| Clojure shell/sh "python3" | CLJ → Python | [cc.clj](../clojure_/codeforces_script/my-clojure-app/src/my_clojure_app/cc.clj#L46)<br/>[remote @ `clojure_`](https://github.com/aqwertyuiop48/codeforces_script/blob/clojure_/my-clojure-app/src/my_clojure_app/cc.clj#L46) |

---
