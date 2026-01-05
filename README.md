# StudyTracker – Fullstack Web App

StudyTracker er en webapplikasjon som lar brukeren:
- registrere studieøkter (dato, timer, produktivitet, kommentar)
- skrive notater
- publisere artikler/blogginnlegg
- logge inn og administrere egen profil

Prosjektet består av en Java-basert backend (Javalin + SQLite) og en frontend (React eller HTML/JS).

> 🚧 **Status: Sprint 1 – Prosjektoppsett**  
> Koden er ikke ferdig ennå. Dette README viser dagens og planlagt prosjektstruktur.

---

## 📁 Prosjektstruktur

studytracker/
├── backend/
│ ├── src/main/java/no/hiof/studytracker/
│ │ ├── controllers/ # HTTP-endepunkter (API)
│ │ ├── services/ # Forretningslogikk
│ │ ├── database/ # SQLite-tilkobling (DB-klasser)
│ │ └── models/ # Dataobjekter (Session, User, etc.)
│ ├── src/main/resources/
│ └── pom.xml # Maven-konfigurasjon
│
├── frontend/
│ ├── public/ # HTML + statiske filer
│ ├── src/ # React/JS-kode
│ └── package.json
│
├── .gitignore
└── README.md


---

## 🧱 Backend (Java + Maven)

Backend bygges i Java med følgende lagdeling:

- **controllers/**  
  Håndterer HTTP-requests (f.eks. POST /sessions).

- **services/**  
  Inneholder logikk som validering, beregning og koordinering av data.

- **database/**  
  SQLite-tilkobling, PreparedStatements, migration scripts.

- **models/**  
  Klassenes datarepresentasjon (Session, User, Article, etc.).

---

## 🎨 Frontend
Frontend-mappen vil inneholde enten:

- `src/` → komponenter, pages, hooks  
- `public/` → index.html  
- `package.json` → avhengigheter

## 📌 Kommende oppgaver (Sprint 1)

- Implementere enkel auth (session-token)
- Opprette API-endepunkt for å registrere studieøkter
- Koble SQLite-database
- Sette opp enkel frontend-side (login + oppgave-registrering)

## 📚 Teknologistack

| Lag | Teknologi |
|-----|-----------|
| Backend | Java, Maven, Javalin, SQLite |
| Frontend | React |
| Versjonskontroll | Git + GitHub |

📘 API documentation can be found in `API.md`

