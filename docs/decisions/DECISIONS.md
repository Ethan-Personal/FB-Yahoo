# Engineering Decisions

This is an append-only log of durable engineering decisions. Use the `record-decision` skill to add new entries.

---

## Manual OAuth Flow (not Spring OAuth2 Client)

**Context:** Yahoo Fantasy API integration requires OAuth 2.0.

**Decision:** Implement custom OAuth flow using `YahooOAuthController` and `YahooOAuthService` rather than Spring Security OAuth2 Client.

**Rationale:** Provides full control over OAuth state management, token storage, and refresh logic. Simplified for single-user local application.

**Consequences:** Must maintain OAuth protocol implementation manually. State stored in HTTP session.

---

## HTTPS Required for Local Development

**Context:** Yahoo OAuth requires HTTPS redirect URIs.

**Decision:** Run Spring Boot on HTTPS (port 8443) using self-signed certificate for local development.

**Rationale:** Yahoo API enforces HTTPS callback URLs even in development.

**Consequences:** Requires keystore setup, `.env` configuration for `SSL_KEYSTORE_PASSWORD`. Browser security warnings on localhost.

---

## Flyway for Schema Management

**Context:** Database schema evolution needed for PostgreSQL.

**Decision:** Use Flyway migrations (`src/main/resources/db/migration/`). Hibernate DDL set to `validate` only.

**Rationale:** Explicit, version-controlled schema changes. Prevents accidental schema drift.

**Consequences:** All schema changes must be in Flyway SQL scripts. No auto-generation of tables.

---

## Single-Row Token Storage

**Context:** OAuth tokens need persistence.

**Decision:** Store tokens in `oauth_token` table with single-row enforcement via `findTopByOrderByIdAsc()`.

**Rationale:** Simple model for single-user local application. Token service auto-refreshes with 30s buffer.

**Consequences:** Not suitable for multi-user scenarios without refactoring.

---
