# Branch policy and commit rules

## Branches

| Branch | Purpose | Created from | Merged into |
|--------|---------|--------------|-------------|
| `main` | Stable, tagged releases only | - | - |
| `develop` | Integration branch for finished features | `main` | `main` (at release) |
| `feature/<issue>-<short-name>` | One backlog item | `develop` | `develop` |
| `bugfix/<issue>-<short-name>` | Fix a defect found during development | `develop` | `develop` |
| `hotfix/<short-name>` | Urgent fix on a released version | `main` | `main` and `develop` |
| `docs/<short-name>` | Documentation-only change | `develop` | `develop` |

## Naming rules

- Use lowercase letters, digits and hyphens only. No spaces or underscores.
- Start the name with the type, then the issue number, then 2 to 4 words.
- Examples: `feature/1-alert-entry-form`, `feature/2-search-alerts`, `bugfix/12-negative-amount`, `docs/update-readme`.
- Delete a feature branch after its pull request is merged.

## Commit message format

```
<type>: <short summary in the imperative, max 72 characters>
```

| Type | Use for |
|------|---------|
| `feat` | A new feature |
| `fix` | A bug fix |
| `docs` | Documentation only |
| `test` | Adding or changing tests |
| `chore` | Build, config, folders, dependencies |
| `ci` | Jenkins or pipeline changes |

Examples: `feat: add alert entry form with validation`, `fix: reject negative alert amounts`, `chore: ignore local H2 database files`.

## Pull request rules

1. Open a pull request from a feature branch into `develop`. Never push directly to `main`.
2. Link the issue in the description (for example `Closes #1`).
3. The Jenkins build must pass and the pull request checklist must be complete.
4. Review the changes and leave at least one review comment before merging.
5. Merge with "Create a merge commit" to keep the history of the feature visible.

## Protected branches

- `main`: pull request required, force pushes blocked, deletion blocked.
- `develop`: changes arrive through pull requests.
- Releases are tagged on `main`, for example `v1.0.0`.
