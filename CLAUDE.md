# CLAUDE.md — FB-Yahoo Helper

> **SSOT Reference:** Architecture in `docs/architecture/ARCHITECTURE.md`, decisions in `docs/decisions/DECISIONS.md`

## Project Overview
Local-first Yahoo Fantasy Basketball helper for data ingestion, normalization, and analysis. Emphasizes correctness, extensibility, and production-grade patterns (Flyway migrations, sync state, testing).

## Critical Invariants (Do Not Violate)
- **Manual OAuth:** Custom flow (see `docs/decisions/DECISIONS.md`). State in `HttpSession["state"]`.
- **HTTPS Only:** `https://localhost:8443` (Yahoo requirement).
- **Database:** Flyway-managed schema. Hibernate `ddl-auto=validate`.
- **Token Model:** Single-row `oauth_token` table (`TokenService.findTopByOrderByIdAsc()`).
- **Secrets:** Load from `.env`. Never commit.
- **CSRF:** Disabled. Don't enable without updating OAuth endpoints.

## Core Commands
- `docker compose up -d db` — Run PostgreSQL
- `./gradlew bootRun` — Run app (HTTPS:8443)
- `./gradlew test` — Run tests
- `docker compose down -v && docker compose up -d db` — Nuke DB

## Logging & Errors
- Use `log.info`/`log.warn` with event names.
- Never log raw tokens or `.env` values.
- OAuth failures: `OAuthExceptionHandler` via `OAuthFlowException`.

## Definition of Done
- Propose `./gradlew test` after code changes (ask first).
- No `docker`/`gradle`/`git` without approval.
