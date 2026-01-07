---
description: Validates a feature by reading its spec, generating tests, docs, and committing.
agent: build
---

# Feature Validation from Spec

You are an expert developer. Your goal is to finalize and validate the feature based on the specification file: spec/$ARGUMENTS

## Step 1: Requirements Analysis
- Read the specification file: $ARGUMENTS.
- Identify all requirements, edge cases, and expected behaviors defined in the spec.

## Step 2: Test-Driven Validation
- Create automated test files that cover every requirement listed in the spec.
- Ensure tests pass against the current implementation.
- If the implementation is missing parts of the spec, scaffold the missing logic.

## Step 3: Documentation & Cleanup
- Generate or update the technical documentation reflecting the final implementation. Generate the documentation file in the folder generated-documentation.
- Ensure the code follows project standards and naming conventions.

## Step 4: Finalize
- Stage all related files (code, tests, and docs).
- Create a git commit with the message: "feat: validate and implement $ARGUMENTS according to spec".