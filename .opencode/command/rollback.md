---
description: Revert all uncommitted changes and restore the workspace to the last commit state.
agent: build
---

# Rollback Workspace

You are an expert developer. Your task is to discard all local changes and return the repository to its clean, last-committed state.

## Step 1: Safety Check
- Identify all modified, deleted, or staged files.
- Ensure that files ignored by `.gitignore` remain untouched.

## Step 2: Rollback Actions
- Execute a hard reset to the current HEAD (`git reset --hex HEAD`).
- Clean the working directory of any untracked files that are NOT in `.gitignore` (`git clean -fd`).
- Ensure all files are restored to their exact state as of the last commit.

## Step 3: Confirmation
- Once finished, provide a brief summary of the files that were reverted.
- Confirm that the working directory is now clean.

## Execution
Proceed with the rollback immediately to restore the project state.