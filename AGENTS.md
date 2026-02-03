# Agent Guidelines

> See CLAUDE.md for critical invariants and architecture. This file provides agent-specific workflow guidance.

## Quick Commands
- `./gradlew bootRun` — Run HTTPS app on port 8443
- `docker compose up -d db` — Start PostgreSQL only  
- `./gradlew test --tests "FullyQualifiedClassName"` — Run single test class

## Coding Conventions
- 4-space indentation, layer folders (`controller/`, `service/`, `config/`, `repo/`, `model/`)
- Naming: `*Controller`, `*Service`, `*Repository`, `*Config`
- Test naming: `*Tests`

## Workflow
- Commits: short, sentence-style (e.g., "added debug logs, cleaned up code")
- Never commit `.env`, keystores, or certs
