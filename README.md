# FB Helper (Yahoo Fantasy)

FB Helper is a **local-first Yahoo Fantasy Sports helper** built for learning, experimentation, and personal use.

This project is designed to be:
- Open-source
- Runnable entirely on your own machine
- Safe to publish publicly
- Configured with **your own Yahoo account and database**

Nothing is hosted. Nothing is collected. All data stays local.

---

## Prerequisites

You will need:

- Java 21
- Docker & Docker Compose
- A Yahoo Developer account (to create your own OAuth app)

---

## Environment Variables

This project uses **environment variables** for all credentials and configuration.

This ensures:
- No secrets are committed to the repository
- Each user provides their **own** credentials
- The project is safe to publish publicly

---

## Database Configuration (PostgreSQL)

The application uses **PostgreSQL via Docker** for local development.

Each user creates their **own database** the first time they run the project.

### Required environment variables

You must define the following variables:

#### POSTGRES_DB
The name of the database that will be created inside Postgres.

#### POSTGRES_USER
The database user the application will connect as.

#### POSTGRES_PASSWORD
The password for that user.

These credentials are: 
- local to your machine
- not shared with anyone else
- safe to choose freely

## Yahoo Developer App Setup

This project requires a **Yahoo Developer App** so the application can authenticate with the Yahoo Fantasy Sports API.

Each user must create **their own** Yahoo app.

---

### Step 1: Open the Yahoo Developer Portal

Visit: https://developer.yahoo.com/apps/


Log in with your Yahoo account.

---
### Step 2: Create a New App

Click **Create an App** and fill out the form as follows:

- **Application Name**  
  Any name you like (e.g. `FB Helper (Local)`)

- **Description**  
  Optional (e.g. `Local-only Yahoo Fantasy Sports helper`)

- **Homepage URL**  
  https://localhost:8443

- **Redirect URIs**
  https://localhost:8443/oauth/yahoo/callback (may require some refactoring if you change)

- **OAuth CLient Type**  
  Confidential Client

- **API Permissions / Scopes**  
  Enable **Fantasy Sports (Read)**
  Click **Create App**.

- Copy the **Client ID** and **Client Secret** values. You will need these later.

### Step 3:

Navigate to the .env.example file and copy the contents into a new file called .env

Replace the values in the .env file with your own:
- Client ID and Client Secret found in previous step)
- Make sure to use the same Redirect URI as you configured in the Yahoo Developer Portal
- Database(Postgres) credentials (These are local to your machine and the database will be created using these credentials)


Start DB
docker compose up -d

docker ps

docker compose logs db


