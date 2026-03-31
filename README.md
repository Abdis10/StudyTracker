# 📚 StudyTracker – Fullstack Web App

StudyTracker is a fullstack web application designed to help users track their study sessions, measure productivity, and gain insights into their learning habits.

The goal of this project was not just to build a working application, but to understand how fullstack systems are designed, connected, and deployed in real-world scenarios.

---

## 🚀 Live Demo

* 🌐 Frontend: https://your-netlify-url.netlify.app
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

While building this project, I gained practical experience with:

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

This project uses environment variables for secure configuration.

Create a `.env` file in the root of the project:

```env
DATABASE_URL=your_database_url
DATABASE_USER=your_database_user
DATABASE_PASSWORD=your_database_password
```

⚠️ Note: `.env` is not committed to version control. Use `.env.example` as a reference.

---

## 📦 Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/your-username/studytracker.git
cd studytracker
```

### 2. Set up environment variables

Create a `.env` file as described above.

### 3. Start backend

Run the application from your IDE (e.g., IntelliJ)

### 4. Start frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🧱 Project Structure

```
backend/
 ├── controllers/
 ├── service/
 ├── repository/
 ├── database/
 └── Application.java

frontend/
 ├── components/
 ├── pages/
 ├── api/
 └── main.jsx
```

---

## 🔍 Key Design Decisions

* Built with Javalin to understand backend fundamentals without heavy abstraction
* Implemented custom session-token authentication
* Designed database schema manually and handled migrations at application startup

---

## 🚧 Future Improvements

* Add subjects & reports feature
* Introduce Flyway for versioned database migrations
* Implement connection pooling (HikariCP)
* Upgrade authentication with Spring Security / JWT
* Improve UI/UX design

---

## 📬 Contact

If you have feedback or questions, feel free to reach out or open an issue.

---

## ⭐ Final Note

This project represents my journey from learning backend fundamentals to building and deploying a complete fullstack application.

---
