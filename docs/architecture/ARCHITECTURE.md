# Architecture

## System Overview

Three-layer Spring Boot application: Web → Service → Data, with external Yahoo Fantasy API integration.

## Components

### Web Layer
- **Controllers**: HTTP endpoints and OAuth flow
- **SecurityConfig**: Public access for OAuth and home page

### Service Layer
- **YahooOAuthService**: OAuth 2.0 protocol (exchange, refresh)
- **TokenService**: Token persistence with 30s expiry buffer

### Data Layer
- **PostgreSQL**: Persistent storage
- **Flyway**: Schema migrations
- **JPA Repositories**: Data access

### External
- **Yahoo Fantasy API**: OAuth + fantasy data
- **WebClient**: HTTP client

## Key Flows

1. **OAuth**: `/oauth/yahoo/login` → Yahoo → `/oauth/yahoo/callback` → token exchange → DB
2. **API Calls**: Controller → TokenService.getValidAccessToken() → WebClient → Yahoo API
3. **Token Refresh**: TokenService checks expiry → YahooOAuthService → DB update

## Design Patterns

- **Session-based OAuth state**: `HttpSession` stores CSRF state during OAuth
- **Single-token model**: One row in `oauth_token` table (single-user)
- **Fail-fast schema validation**: Hibernate DDL=validate enforces Flyway migrations
