## Android CI/CD pipeline architecture

<p align="center">
  <img src="https://github.com/TharunBalaji2004/android-ci-cd/assets/95350584/d8ce9e0a-7e4c-4698-a555-ac8e512c81fb" height="180px" width="180px" />
  &nbsp;&nbsp;
  <img src="https://github.com/TharunBalaji2004/android-ci-cd/assets/95350584/b0483d4b-fb48-4c73-825a-cd92abada39e" height="150px" width="160px" />
  &nbsp;&nbsp;
  <img src="https://github.com/TharunBalaji2004/android-ci-cd/assets/95350584/aa67c11e-6563-4189-85d8-3ca50d7dcb50" height="150px" width="150px" />
  &nbsp;&nbsp;
  <img src="https://github.com/TharunBalaji2004/android-ci-cd/assets/95350584/d2fbfc7a-a42e-41dc-b5af-f44b945ab60d" height="180px" width="200px" />
</p>


<p align="center">
<img alt="GitHub" src="https://img.shields.io/github/license/TharunBalaji2004/android-ci-cd">
<img alt="CI Badge" src="https://img.shields.io/badge/CI%20(main)-passing-brightgreen?logo=github"> 
<img alt="Android Package" src="https://img.shields.io/badge/apk%20package-v1.0-brightgreen?logo=android">
<img alt="GitHub last commit (branch)" src="https://img.shields.io/github/last-commit/TharunBalaji2004/android-ci-cd/main?color=%238A2BE2">
</p>
<p align="center">
<img alt="Gradle Plugin Portal" src="https://img.shields.io/gradle-plugin-portal/v/org.sonarqube?label=sonarqube%20-%20org.gradle">
</p>

### What is meant by CI ?

CI stands for **_Continuous Integration_**, which is a development practice that delivers software to the end user with production reliability. The Continuous Integration (CI) is an **_automated integration process_** which generates a build and runs automates tests against it. Usually, a CI is attached with a Repository or Codebase and all the changes are merged before starting it.

### What is meant by CD ?

CD stands for **_Continuous Delivery_**, which is an automated process of deploying and making the application available successfully to use. The CD process is started only when the application has passed through the integration process and tested with no critical issues.

### CI/CD for Android App

![Pasted image 20230626231334](https://github.com/TharunBalaji2004/android-ci-cd/assets/95350584/b5456db2-a977-49c6-a616-a4714ce6410d)
<p align="center"><i>(Figure: CI/CD Pipeline Architecture for Android)</i></p>
Now let us design our CI/CD flow so that we are clear what we want to achieve. For any Android project I would recommend the following steps:

- Android Lint Check
- Unit Tests
- Instrumentation Tests
- Static Code Analysis
- Packaging
- Functional Tests
- Deployment

## 1. Setup GitHub Actions for repository

To add GitHub Actions to your repository you need to create a yaml file `repo/.github/workflows/ci.yaml`

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  sample:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout the code
        uses: actions/checkout@v2

      - name: Run sample script
        uses: echo Hello, world
```

- **name** - refers the action name
- **on push, pull_request** - It states the branch to be used for CI process when push or pull_request to the specified branch happens.
- **jobs** - used for specifying the jobs to be performed
- **sample** - the name of job to be performed
- **runs-on** - it specifies on which serves should the process be performed *say ubuntu*
- **steps** **(name, uses)** - Each step has its own name and uses, and the first step should be to checkout the code

## 2. Perform Android Lint check

Now that our basic configuration is in place, we will add Lint check as our first job. Let us understand what the following configuration does.

**Step 1:** `runs-on: ubuntu-latest` tells to run the job on latest ubuntu machine.  
**Step 2:** `actions/checkout@v2` action checks out the codebase on the machine  
**Step 3:** Once we have the codebase on the machine, run `./gradlew lintDebug`  
**Step 4:** Publish the lint report as a github artifact

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout the code
        uses: actions/checkout@v2

      - name: Run lint
        uses: ./gradlew lintDebug

      - name: Upload html test report
        uses: actions/upload-artifact@v2
        with:
          name: lint.html
          path: app/build/reports/lint-results-debug.html
```

- **with** - it uploads the artifact as the specified name to the path

🤔 _What is meant by Lint ?_

😎 _The lint tool checks your Android project source files for potential bugs and optimization improvements for correctness, security, performance, usability, accessibility, and internationalization. Basically it's an basic code correction and suggestion tool_

## 3. Perform Android Unit Tests

Our second job would be to run the unit tests. This job will run after the `lint` job and that is why you see `needs: [lint]` in the below config.

**Step 1:** `runs-on: ubuntu-latest` tells to run the job on latest ubuntu machine.  
**Step 2:** `actions/checkout@v2` action checks out the codebase on the machine  
**Step 3:** Run `./gradlew test` will run the unit tests
**Step 4:** Publish the test report folder as a github artifact

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout the code
        uses: actions/checkout@v2

      - name: Run lint
        uses: ./gradlew lintDebug

      - name: Upload html test report
        uses: actions/upload-artifact@v2
        with:
          name: lint.html
          path: app/build/reports/lint-results-debug.html

  unit-test:
    needs: [lint]
    runs-on: ubuntu-latest
    steps:
      - name: Checkout the code
        uses: actions/checkout@v2

      - name: Run tests
        uses: ./gradlew test

      - name: Upload test report
        uses: actions.upload-artifact@v2
        with:
          name: unit_test_report
          path: app/build/reports/test/testDebugUnitTest/
```

- **needs** - the keyword states that the current job as to be executed only when the specified job is been completed _say lint_
