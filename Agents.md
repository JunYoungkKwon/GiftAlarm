# Android Project AI Rules

This project follows strict architectural and development conventions.
Always follow the rules below when generating or modifying code.

---

## Architecture

- Always follow **Clean Architecture**.
- Separate layers clearly into:
    - UI Layer
    - Domain Layer
    - Data Layer
- Follow **MVVM pattern**.
- Use **ViewModel + LiveData** for state management.
- Always use **Repository pattern**.
- Always use **UseCase pattern** in the Domain layer.
- Do not mix UI logic inside the Data layer.
- Keep Domain layer independent from Android framework.

---

## Tech Stack Rules

- Use **Jetpack Compose** for UI.
- Use **LiveData** as lifecycle-aware observable.
- Use **Hilt** for dependency injection.
- Use **Room** for local database.
- Use **Navigation Compose** for screen navigation.
- Use **Coil** for image loading.
- Avoid deprecated APIs.
- Avoid GlobalScope.
- Prefer clear and readable Kotlin code.

---

## Code Quality

- Follow SOLID principles.
- Keep functions concise and focused.
- Separate business logic from UI logic.
- Add KDoc comments for public classes and functions.
- Avoid unnecessary complexity.

---

## Testing

- Generate unit tests for UseCase and Repository logic.
- Use JUnit for unit testing.
- Mock dependencies when necessary.

---

## Language Rule

- Always explain all changes and analysis in **Korean**.
- Code should remain in English (standard Kotlin naming).

---

## Commit Convention

When suggesting commit messages, strictly follow this format:

<type>: <short description>

Allowed types:

- feat     → New feature
- fix      → Bug fix
- refactor → Code restructuring without behavior change
- style    → Formatting or styling only
- docs     → Documentation changes
- test     → Test code changes
- chore    → Build setup or maintenance tasks
- build    → Gradle or dependency changes
- ci       → CI/CD configuration
- perf     → Performance improvement
- design   → UI or design changes

Examples:
- feat: add gifticon expiration notification
- fix: resolve crash when loading room database
- refactor: separate domain use cases
- design: update main screen layout