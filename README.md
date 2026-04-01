# 📚 StudyTracker – Fullstack Web App

StudyTracker is a fullstack web application that helps users track study sessions, measure productivity, and gain insights into their learning habits.

This project was built to understand how real-world applications are designed, connected, and deployed — from backend architecture to frontend integration and production debugging.

---

## 🚀 Live Demo

* 🌐 Frontend: https://sparkly-churros-d400a3.netlify.app/
* ⚙️ Backend: https://studytracker-cfmz.onrender.com/

---

## ✨ Features

* 🔐 User authentication (custom session-based tokens)
* 📝 Log study sessions (date, hours, productivity, comments)
* 📊 Visualize productivity using charts
* 📅 Track study activity over time
* 🔄 Session validation on refresh
* 🗑️ Update and delete study sessions

---

## 🧠 What I Learned

* Designing REST APIs
* Handling authentication and session management
* Connecting a React frontend with a Java backend
* Debugging real production issues (SQLite → PostgreSQL migration)
* Managing environment variables securely
* Structuring a scalable backend (Controller → Service → Repository)

---

## 🛠️ Tech Stack

### Backend

* Java 21
* Javalin
* PostgreSQL
* JDBC

### Frontend

* React (Vite)
* JavaScript (ES6+)
* CSS

### Deployment

* Frontend: Netlify
* Backend: Render

---

## 🔐 Environment Variables

Create a `.env` file in the root of the project:

```env
DATABASE_URL=your_database_url
DATABASE_USER=your_database_user
DATABASE_PASSWORD=your_database_password
```

---

## 📦 Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/your-username/studytracker.git
cd studytracker
```

---

### 2. Database Setup (PostgreSQL)

#### Option 1: Local Installation

* Create a database named `studytracker`
* Update your `.env` file

#### Option 2 (Recommended): Docker

```bash
docker run --name studytracker-postgres \
  -e POSTGRES_DB=studytracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres
```

---

### 3. Start Backend

Run the application from your IDE (e.g., IntelliJ)

---

### 4. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🧱 Project Structure

```
backend/src/main/java/no.hiof.studytracker
 ├── controllers/
 ├── service/
 ├── repository/
 ├── database/
 └── Application.java

frontend/src/
 ├── components/
 ├── pages/
 ├── api/
 └── main.jsx
```

---

## 🔍 Key Design Decisions

* Built with Javalin to understand backend fundamentals without heavy abstraction
* Implemented custom session-based authentication instead of JWT
* Designed database schema manually with migrations at application startup
* Used environment variables instead of hardcoded configuration

---

## 🚧 Future Improvements

* Add subjects & reports feature
* Introduce Flyway for versioned database migrations
* Implement connection pooling (HikariCP)
* Upgrade authentication using Spring Security
* Improve UI/UX design

---

## ⭐ Final Note

This project represents my transition from learning backend fundamentals to building and deploying a complete fullstack application in production.

---
