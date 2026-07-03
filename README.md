<!-- ⚠️ ARCHIVED — Read-only as of 2026-07-03 -->

# 📚 StudyTracker – Fullstack Study Tracking App

> ⚠️ This repository was archived and set to read-only on 2026-07-03. The code remains available for viewing and forking. If you want to continue development or submit changes, please fork this repository and work in your fork.

StudyTracker is a fullstack web application for logging and analyzing study sessions. It was built to learn end-to-end application design: a Java (Javalin) backend with a React frontend, custom session-based authentication, and simple data visualizations to help users understand study habits.

---

## Summary

- Purpose: let users record study sessions (date, duration, productivity, notes) and view visual summaries (charts, trends) to improve learning routines.
- Audience: learners who want to track time and productivity, and developers studying a small fullstack example.

---

## Key features

- User accounts and custom session-based authentication (backend-managed sessions)
- Create, read, update, delete (CRUD) study session records
- Session fields: date, hours, productivity score, optional comments
- Dashboard charts showing time spent and productivity over time
- Session validation and persistence across page refreshes
- Simple export and filtering (by date range)
- Backend migrations: example migration from SQLite to PostgreSQL during development

---

## High-level architecture

- Backend: Java (Javalin) with a Controller → Service → Repository structure. Persistence via JDBC to PostgreSQL (or SQLite during local development).
- Frontend: React (Vite) single-page app that calls the backend REST API and renders charts.
- Deployment examples in this project used Netlify (frontend) and Render (backend).

---

## API (overview)

The backend exposes a small REST API used by the frontend. Typical endpoints include:

- POST /api/auth/login — login and create a session
- POST /api/auth/register — create an account
- POST /api/sessions — create a study session
- GET /api/sessions — list user sessions (supports date range filters)
- GET /api/sessions/:id — retrieve a single session
- PUT /api/sessions/:id — update a session
- DELETE /api/sessions/:id — delete a session

(Use the server logs or backend controller files to confirm exact paths and request/response shapes.)

---

## Data model (example)

A study session typically contains:

- id: integer/UUID
- user_id: integer (owner of the session)
- date: ISO date
- hours: number (duration in hours)
- productivity: integer or float (user score)
- comments: text

---

## Development setup (quick)

Note: This repo is archived. Clone or fork first.

1. Clone

```bash
git clone https://github.com/Abdis10/StudyTracker.git
cd StudyTracker
```

2. Backend

- Inspect the backend build file (pom.xml or build.gradle) in the repository root to see how to build and run the server.
- Typical commands (adjust if your project uses Maven/Gradle):

```bash
# Maven
mvn clean package
java -jar target/*.jar

# or Gradle
./gradlew build
java -jar build/libs/*.jar
```

3. Database

- Create a PostgreSQL database (or use Docker):

```bash
docker run --name studytracker-postgres \
  -e POSTGRES_DB=studytracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 -d postgres
```

- Configure DB connection using environment variables (see below).

4. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open the frontend URL printed by Vite and sign in / register to start logging sessions.

---

## Environment variables

Create a `.env` file (root) and add the backend DB and secret settings. Example variables used by the project:

```env
DATABASE_URL=postgres://user:pass@localhost:5432/studytracker
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres
# SESSION_SECRET=your_secret_here
```

Check the backend code/config files for exact variable names the app expects.

---

## Running tests

If there are test suites, run them with the project build tool (Maven/Gradle for backend, npm/yarn for frontend). See `pom.xml`/`build.gradle` and `frontend/package.json` for test scripts.

---

## Contribution & archive policy

This repository is archived and set to read-only. Pull requests will not be accepted while archived. To contribute or continue development:

- Fork this repository and submit PRs against your fork
- Or contact the repository owner for transfer/collaboration options

---

## License

See the `LICENSE` file at the project root for license details.

---

## Contact

Maintainer: Abdis10

---

## Archive details

- Archived by: Abdis10
- Archive date: 2026-07-03
