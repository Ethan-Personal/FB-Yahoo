# FB Helper (Yahoo Fantasy)

FB Helper is a **local-first Yahoo Fantasy Sports helper** built for learning, experimentation, and personal use. It is primarily for basketball now, but may expand later. 

This project is designed to be:
- Open-source
- Runnable entirely on your own machine
- Safe to publish publicly (no secrets committed)
- Configured with **your own Yahoo account and your own local database**

Nothing is hosted. Nothing is collected. All data stays local.

---

## What this project uses

- **Spring Boot (MVC)** for the backend server
- **Yahoo OAuth 2.0** for authentication (your Yahoo account)
- **PostgreSQL** (Docker) for local persistence
- **Flyway** for database migrations (automatically applies database migrations on startup)
- **Local HTTPS** (mkcert + PKCS12 keystore) because Yahoo requires HTTPS redirect URIs

---

## Prerequisites

You will need:

- **Java 21**
- **Docker & Docker Compose**
- **A Yahoo Developer account**
- **mkcert** (for local HTTPS certificates)

### Install mkcert (macOS)

```bash
brew install mkcert
brew install nss
mkcert -install
```

### Generate localhost certificates

From the project root, run:

```bash
mkcert localhost 127.0.0.1 ::1
```

This will generate files similar to:
- `localhost+2.pem`
- `localhost+2-key.pem`

These files are local-only and must not be committed.

### Convert certificates to a PKCS12 keystore

Spring Boot requires a `.p12` keystore.

Create a directory for secrets:

```bash
mkdir -p secrets
```

Convert the certs:

```bash
openssl pkcs12 -export \
  -in localhost+2.pem \
  -inkey localhost+2-key.pem \
  -out secrets/keystore.p12 \
  -name local-https \
  -password pass:changeit
```

You may choose a different password if you want. Make sure to update `.env` accordingly.

Spring Boot is configured to read this keystore at startup to enable HTTPS (via `server.ssl.enabled=true` and related properties in `application.properties`).

### Update .gitignore

Ensure the following entries exist:

```
.env
secrets/
*.pem
*.p12
```

---

## Yahoo Developer App Setup

Each user must create their own Yahoo OAuth app.

### Create a Yahoo App

1. Visit: https://developer.yahoo.com/apps/
2. Click **Create an App**
3. Use the following values:
   - **Application Name**: Any name you like (e.g. `FB Helper Local`)
   - **Homepage URL**: `https://localhost:8443`
   - **Redirect URI(s)**: `https://localhost:8443/oauth/yahoo/callback` (must match exactly, including protocol and port)
     - if you change this, match the changes in .env file and YahooOAuthController.java endpoint
   - **OAuth Client Type**: Confidential Client
   - **API Permissions / Scopes**: Enable Fantasy Sports (Read)
4. Create the app and copy:
   - **Client ID**
   - **Client Secret**

---

## Database Setup (PostgreSQL via Docker)

The application uses PostgreSQL in Docker. Each user runs their own local database.

### Required Database Variables

You will configure these in `.env`:
- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `POSTGRES_HOST`
- `POSTGRES_PORT`

These credentials are:
- Local-only
- Not shared
- Safe to choose freely

### Create the .env File

Copy `.env.example` to `.env` and fill in all values.

### Start the Database

Start PostgreSQL using Docker:

```bash
docker compose up -d db
```

Verify it is running:

```bash
docker ps
```

---

## Run the Application

### Option A: Run locally with Gradle

```bash
./gradlew bootRun
```

The app will start at: `https://localhost:8443`

### Option B: Run with Docker Compose

```bash
docker compose up --build
```

**Note:** Do not run `bootRun` and the app container at the same time on the same port.

When running with Docker Compose, the HTTPS keystore must be mounted into the app container. This is already configured in `docker-compose.yml`.

---

## Yahoo OAuth Login Flow

1. Open: `https://localhost:8443/oauth/yahoo/login`
2. Log in to Yahoo and approve access
3. Yahoo redirects back to: `https://localhost:8443/oauth/yahoo/callback`
4. The application exchanges the code for tokens and stores them in your local database
5. After a successful login, you should be redirected to `/`

---

## Troubleshooting

### HTTPS / TLS parsing errors (0x16 0x03)

This means HTTPS traffic is hitting an HTTP server.

**Fix:**
- Ensure SSL is enabled
- Ensure keystore path and password are correct
- Ensure you are using `https://localhost:8443`

### Port already in use

Change:
- `SERVER_PORT`
- Yahoo Redirect URI
- `YAHOO_REDIRECT_URI`

All must match exactly.

### Database connection errors

Ensure:
- Docker is running
- The Postgres container is running
- `.env` values match Docker Compose config

---

## Security Notes

- **Never commit** `.env`
- **Never commit** keystores or certificates
- **Yahoo Client Secret** must remain private
- Redirect URI is not a secret, but must match exactly

---

## Summary

This project is designed to be:
- Easy to clone
- Safe to publish
- Fully local
- Educational

Every user runs it on their own machine with their own credentials.
