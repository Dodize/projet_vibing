# AGENTS.md: Repository Guidelines

This file outlines essential commands and conventions for agents operating within this Android project.

## 1. Build, Lint, and Test Commands

All commands should be executed from the root directory using `./gradlew`.

| Task | Command | Notes |
| :--- | :--- | :--- |
| **Build** | `./gradlew build` | Compiles and packages the application. |
| **Lint** | `./gradlew lint` | Runs static code analysis. |
| **Unit Tests** | `./gradlew test` | Runs all unit tests in `app/src/test`. |
| **Single Unit Test** | `./gradlew :app:test --tests "com.example.vibing.ExampleUnitTest"` | Replace `ExampleUnitTest` with the target test class. |
| **Instrumentation Tests** | `./gradlew connectedAndroidTest` | Runs tests on connected devices/emulators. |

## 2. Code Style and Conventions

Adhere strictly to established Android/Kotlin conventions:

*   **Formatting:** Use 4-space indentation. Follow standard Android Studio formatting profiles.
*   **Naming:** Use `camelCase` for methods/variables and `PascalCase` for classes/interfaces.
*   **Types:** Maintain strong typing (Kotlin preferred).
*   **Error Handling:** Use idiomatic `try-catch` blocks for I/O, network (Firebase), and location operations.
*   **Commits:** Never commit without explicit user authorization.
*   **Bug Fixes:** Do not consider a bug fixed until explicitly confirmed by the user.
*   **Testing:** Do not run tests automatically; wait for user instruction.
