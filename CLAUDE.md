# CLAUDE.md — FB-Yahoo Helper

## Project Overview

- FB Helper is a local-first Yahoo Fantasy Basketball helper designed to ingest, normalize, and analyze fantasy league data to support better roster, matchup, and season-long decisions. The project emphasizes correctness, extensibility, and developer ergonomics over quick hacks or opaque automation.

- The system is intentionally built as an engineering learning project, with production-grade patterns (schema design, ingestion pipelines, sync state, migrations, and testing) rather than a throwaway script.


## Critical Invariants (Do Not Violate)
- **Manual OAuth:** No Spring OAuth2 Client; use custom flow in `YahooOAuthController` & `YahooOAuthService`.
- **HTTPS Only:** Yahoo requires HTTPS. Local: `https://localhost:8443` (set via `server.port` & `server.ssl` in `application.properties`).
- **OAuth State:** Stored in `HttpSession` under key `"state"`. Do not "optimize" to stateless without updating callback.
- **Database:** Managed by **Flyway**. Hibernate must NOT alter schema (`ddl-auto=validate`).
- **Token Model:** Single-row persistent storage in `oauth_token`. Enforcement: `TokenService` uses `findTopByOrderByIdAsc()` and updates existing row.
- **Secrets:** Load from `.env` (imported in `application.properties`). Never commit secrets or keystores.
- **CSRF:** Currently disabled in `SecurityConfig`; do not enable without updating OAuth endpoints/tests.

## Repo Map
- `src/main/java/com/example/fbyahoo/controller`: OAuth & Home controllers.
- `src/main/java/com/example/fbyahoo/service`: Business logic (OAuth protocol, Token management).
- `src/main/java/com/example/fbyahoo/config`: Spring configuration (Security, Properties, HTTP client).
- `src/main/resources/db/migration`: Flyway SQL migrations.
- `./docker-compose.yml`: Infrastructure (Postgres).
- `./application.properties`: Main config (Server, DB, Yahoo URLs).

## Tech Stack
Java 21, Spring Boot 3+, Spring Security, WebClient, Postgres 16, Flyway, Gradle.

## Core Commands
- **Run DB:** `docker compose up -d db`
- **Run App:** `./gradlew bootRun`
- **Test:** `./gradlew test`
- **Nuke DB:** `docker compose down -v && docker compose up -d db`

## Architecture & Flow
- **Flow:** `GET /oauth/yahoo/login` (state/session) → Yahoo → `GET /oauth/yahoo/callback` (validate/exchange) → Save → `/`.
- **Ownership:**
  - `YahooOAuthService`: Protocol/API calls (exchange/refresh).
  - `TokenService`: Persistence + auto-refresh (30s buffer).
  - `SecurityConfig`: Public: `/`, `/oauth/**`, `/actuator/**`.
- **Yahoo API calls:** Use `WebClient` + `tokenService.getValidAccessToken()`.
- **Token Table:** `oauth_token` columns: `access_token`, `refresh_token`, `expires_at`.

## Logging & Errors
- **Standard:** Use `log.info`/`log.warn` with event names.
- **Privacy:** Never log raw tokens or sensitive `.env` values.
- **OAuth Failures:** Handled by `OAuthExceptionHandler` via `OAuthFlowException`.

## Definition of Done (DoD)
- **Verification:** After any code change, propose running `./gradlew test` (ask first).
- **Commands:** Do not run `docker`, `gradle`, or `git` commands without explicit approval.

## Quick Troubleshooting
- **TLS errors:** Use `https://`, not `http://`.
- **OAuth failures:** Ensure `.env` matches Yahoo Developer console exactly.
- **Port 8443 busy:** Update `server.port` and Yahoo console redirect URI.
