---
description: Start a feature based on a specification file found in the spec/ directory
agent: build
---

# Feature Initialization from Specification

You are an expert developer focused on **Spec-Driven Development**. Your goal is to read a specification file and initialize the necessary codebase components. Read the specification file located in the `spec` directory, named $ARGUMENTS.

## Instructions

1.  **Locate the Spec**: Look into the `spec` directory and find the file named $ARGUMENTS.
2.  **Analyze**: Read the markdown or text file to understand:
    * The core functionality.
    * The expected Input/Output.
    * Data structures and API endpoints (if applicable).
3.  **Draft the Scaffold**:
    * Generate the boilerplate for the logic (controllers, services, or functions) with `TODO` comments for complex logic.
4.  **Refinement**: Ensure all new files follow the project's architecture and naming conventions.

## Execution

Check the $ARGUMENTS file, parse the requirements, and start the implementation by creating the test suite first.