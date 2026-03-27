import {BookOpenCheck} from "lucide-react";
import {NavLink} from "react-router-dom";
import "../css/homePage.css";


function HomePage() {
    return (
        <div className="home-page">

            {/* NAVBAR */}
            <div className="home-page-navbar">
                <div className="home-nav-left">
                    <BookOpenCheck size={32} className="logo-icon-svg" />
                    <span className="logo-text">StudyTracker</span>
                </div>

                <div className="home-right-navbar">
                    <ul className="home-navbar-links">
                        <li>
                            <NavLink to="/home" end
                                     className={({ isActive }) =>
                                         isActive ? "navbar-link active" : "navbar-link"
                                     }>
                                Home
                            </NavLink>
                        </li>

                        <li>
                            <NavLink to="/features"
                                     className={({ isActive }) =>
                                         isActive ? "navbar-link active" : "navbar-link"
                                     }>
                                Features
                            </NavLink>
                        </li>

                        <li>
                            <NavLink to="/contact"
                                     className={({ isActive }) =>
                                         isActive ? "navbar-link active" : "navbar-link"
                                     }>
                                Contact
                            </NavLink>
                        </li>

                        <button className="home-login-btn">
                            <NavLink to="/login">Login</NavLink>
                        </button>

                        <button className="home-signup-bnt">
                            <NavLink to="/signup">Signup</NavLink>
                        </button>
                    </ul>
                </div>
            </div>

            {/* HERO */}
            <section className="home-hero">
                <div className="home">
                    <h1 className="motto">
                        Enhance Your Study Habits with StudyTracker
                    </h1>

                    <p className="home-p">
                        Track your study sessions, review your productivity, and achieve your academic goals with ease.
                    </p>

                    <button className="home-btn">
                        Get started for free
                    </button>
                </div>

                <div className="home-image">
                    <img src="/home-page-picture.png" alt="Study illustration" />
                </div>
            </section>

            {/* FEATURES */}
            <section className="features-section">
                <h2>Track Your Study Progress</h2>
                <p>Stay motivated and improve daily</p>

                <div className="features-grid">
                    <div className="feature-card">
                        <h3>Log Study Sessions</h3>
                        <p>Easily track your study time.</p>
                    </div>

                    <div className="feature-card">
                        <h3>Analyze Productivity</h3>
                        <p>Visualize performance with charts.</p>
                    </div>

                    <div className="feature-card">
                        <h3>Reach Your Goals</h3>
                        <p>Build strong study habits.</p>
                    </div>
                </div>
            </section>

            {/* HOW */}
            <section className="how-section">
                <h2>How it works</h2>

                <div className="steps">
                    <div className="step">
                        <span>1</span>
                        <p>Log session</p>
                    </div>

                    <div className="step">
                        <span>2</span>
                        <p>Track productivity</p>
                    </div>

                    <div className="step">
                        <span>3</span>
                        <p>Improve</p>
                    </div>
                </div>
            </section>

            {/* CTA */}
            <section className="cta-section">
                <h2>Start improving today</h2>
                <button className="home-btn">Get Started</button>
            </section>

        </div>
    )
}

export default HomePage;