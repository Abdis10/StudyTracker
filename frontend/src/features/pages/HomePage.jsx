import { BookOpenCheck } from "lucide-react";
import { NavLink } from "react-router-dom";
import "../css/homePage.css";
import Footer from "../components/Footer.jsx";

function HomePage() {

    // Helper function for smooth scroll
    const scrollToSection = (e, id) => {
        e.preventDefault();
        const element = document.getElementById(id);
        if (element) {
            element.scrollIntoView({ behavior: "smooth" });
        }
    };

    return (
        <div className="home-page">
            {/* NAVBAR */}
            <nav className="home-page-navbar">
                <div className="home-nav-left">
                    <BookOpenCheck size={32} className="logo-icon-svg" />
                    <span className="logo-text">StudyTracker</span>
                </div>

                <div className="home-right-navbar">
                    <ul className="home-navbar-links">
                        <li><a href="#home" onClick={(e) => scrollToSection(e, 'home')} className="navbar-link">Home</a></li>
                        {/* Smooth Scroll Links */}
                        <li><a href="#features" onClick={(e) => scrollToSection(e, 'features')} className="navbar-link">Features</a></li>
                        <li><a href="#contact" onClick={(e) => scrollToSection(e, 'contact')} className="navbar-link">Contact</a></li>

                        <li><NavLink to="/login" className="home-login-btn">Log In</NavLink></li>
                        <li><NavLink to="/signup" className="home-signup-btn">Sign Up</NavLink></li>
                    </ul>
                </div>
            </nav>

            {/* HERO */}
            <section id="home" className="home-hero">
                <div className="home-content">
                    <h1 className="motto">Enhance Your Study Habits with StudyTracker</h1>
                    <p className="home-p">
                        Track your study sessions, review your productivity, and achieve your academic goals with ease.
                    </p>
                    <NavLink to="/login" className="home-login-btn">
                        Get started
                    </NavLink>
                    <p className="home-subtext">Get started for free. No credit card required.</p>
                </div>

                {/* Hero Illustration Placeholder */}
                <div className="home-hero-image">
                    <img src="/home-page-picture.png" alt="Study Illustration" />
                </div>
            </section>

            {/* WAVE SEPARATOR (Optional CSS shape) */}
            <div className="wave-divider"></div>

            {/* INTERACTIVE DASHBOARD PREVIEW */}
            <section id="features" className="dashboard-preview">
                <div className="preview-row">
                    <div className="preview-text">
                        <span className="badge">Insightful Analytics</span>
                        <h2>Visualize Your Progress</h2>
                        <p>
                            Get a high-level overview of your study habits with intuitive line charts and
                            productivity breakdowns. Stay on top of your weekly and monthly goals.
                        </p>
                        <ul className="preview-list">
                            <li>✅ Daily study hour tracking</li>
                            <li>✅ Productivity score trends</li>
                            <li>✅ Monthly summary cards</li>
                        </ul>
                    </div>
                    <div className="preview-image shadow-right">
                        <img src="/dashboard-analytics.png" alt="StudyTracker Dashboard" />
                    </div>
                </div>

                <div className="preview-row reverse">
                    <div className="preview-image shadow-left">
                        <img src="/Skjermbilde%202026-03-29%20003607.png" alt="Log Study Session" />
                    </div>
                    <div className="preview-text">
                        <span className="badge">Easy Logging</span>
                        <h2>Seamless Session Entry</h2>
                        <p>
                            Logging your work shouldn't be a chore. Our intuitive modal allows you to
                            record time, set focus levels, and leave notes in seconds.
                        </p>
                        <ul className="preview-list">
                            <li>✅ Quick-entry duration picker</li>
                            <li>✅ Focus intensity slider</li>
                            <li>✅ Detailed session comments</li>
                        </ul>
                    </div>
                </div>
            </section>

            {/* FEATURES */}
            <section className="features-section">
                <h2 className="section-title">Track Your Study Progress</h2>
                <p className="section-subtitle">Stay Motivated and Achieve Your Goals</p>

                <div className="features-grid">
                    <div className="feature-card">
                        <div className="icon-box blue">📝</div>
                        <h3>Log Study Sessions</h3>
                        <p>Easily log your study sessions, including time spent and focus levels, to better understand your study habits.</p>
                    </div>

                    <div className="feature-card">
                        <div className="icon-box green">📊</div>
                        <h3>Analyze Productivity</h3>
                        <p>Review your productivity with insightful analytics. See your progress and identify areas for improvement.</p>
                    </div>

                    <div className="feature-card">
                        <div className="icon-box purple">🎯</div>
                        <h3>Reach Your Goals</h3>
                        <p>Set study goals and track your achievements. Stay consistent and motivated to reach academic excellence.</p>
                    </div>
                </div>

                <NavLink to="/login" className="monitor-login-btn">
                    Monitor Your Study Habits Effectively &rarr;
                </NavLink>
            </section>

            {/* CONTACT SECTION */}
            <section id="contact" className="contact-section">
                <div className="contact-container">
                    <div className="contact-info">
                        <span className="badge">Get in Touch</span>
                        <h2>How can we help you?</h2>
                        <p>
                            Have questions about StudyTracker or need help setting up your dashboard?
                            Our team is here to support your academic journey.
                        </p>

                        <div className="contact-details">
                            <div className="contact-item">
                                <span className="contact-icon">📧</span>
                                <div>
                                    <h4>Email us</h4>
                                    <p>support@studytracker.com</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <form className="contact-form" onSubmit={(e) => e.preventDefault()}>
                        <div className="form-group">
                            <label>Full Name</label>
                            <input type="text" placeholder="Jo Doe..." />
                        </div>
                        <div className="form-group">
                            <label>Email Address</label>
                            <input type="email" placeholder="Jodoe@example.com" />
                        </div>
                        <div className="form-group">
                            <label>Message</label>
                            <textarea rows="4" placeholder="How can we help?"></textarea>
                        </div>
                        <button type="submit" className="home-btn-primary full-width">
                            Send Message
                        </button>
                    </form>
                </div>
            </section>
            <Footer />
        </div>
    );
}

export default HomePage;