# NBA Fantasy

2025-26 player stats explorer.

![NBA Fantasy UI](docs/ui.png)

## Features

- Browse players by team
- Browse players by position
- Search players by name

## Tech Stack

- Backend: Spring Boot (Maven)
- Frontend: React + Vite

## Data + Storage
Player seed data starts from a CSV and is imported into a MySQL database on the backend (the app reads from MySQL at runtime).

## Run Locally

### Backend

```powershell
./mvnw spring-boot:run
```

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

