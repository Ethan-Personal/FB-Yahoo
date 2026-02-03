# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/com/example/fbyahoo/`: Spring Boot application code (controllers, services, config, models).
- `src/main/resources/`: app config (`application.properties`) and Flyway migrations (`db/migration/`).
- `src/test/java/com/example/fbyahoo/`: JUnit tests.
- `Dockerfile` and `docker-compose.yml`: local container setup for app + PostgreSQL.

## Build, Test, and Development Commands
- `./gradlew build`: compile and run all checks.
- `./gradlew bootRun`: run the HTTPS Spring Boot app locally on port 8443.
- `./gradlew test`: run JUnit tests.
- `docker compose up -d db`: start only PostgreSQL for local dev.
- `docker compose up --build`: run app + database in Docker (don’t run with `bootRun` on the same port).

## Coding Style & Naming Conventions
- Java 21, Spring Boot MVC, package root `com.example.fbyahoo`.
- Indentation: 4 spaces; keep classes in their layer folders (`controller/`, `service/`, `config/`, `repo/`, `model/`).
- Naming: `*Controller`, `*Service`, `*Repository`, `*Config`; use descriptive, domain-specific names.
- Formatting/linting: no explicit formatter configured; keep code consistent with existing style.

## Testing Guidelines
- Framework: JUnit Platform via Gradle (`tasks.named('test')` uses `useJUnitPlatform()`).
- Tests live in `src/test/java/com/example/fbyahoo/`.
- Naming: `*Tests` (e.g., `FbYahooApplicationTests`).
- Run a single test class: `./gradlew test --tests "com.example.fbyahoo.FbYahooApplicationTests"`.

## Commit & Pull Request Guidelines
- Commits in history use short, sentence-style messages (e.g., “added debug and info logs, cleaned up code”).
- Keep commits focused and descriptive; mention the main behavior change.
- PRs should include: summary of changes, how to run/verify, and any config/env updates.

## Security & Configuration Tips
- Never commit `.env`, keystores, or certs; keep secrets in local files.
- Yahoo OAuth redirect URI must match exactly (`https://localhost:8443/oauth/yahoo/callback` by default).
- HTTPS is required for OAuth; ensure `SSL_KEYSTORE_PASSWORD` and keystore path are set.
