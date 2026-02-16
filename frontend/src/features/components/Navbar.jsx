import "../css/navbar.css";
import useAuth from "../auth/useAuth.js";
import { BookOpenCheck, LogOut } from 'lucide-react';
function Navbar( { onClickLogout } ) {
    const { user, setActiveSection, highlightSection } = useAuth();

    return (
        <nav className="navbar">
            <div className="navbar-left">
                <div className="logo">
                    <BookOpenCheck size={32} className="logo-icon-svg" />
                    <span className="logo-text">StudyTracker</span>
                </div>
            </div>

            {/* Center: Navigation links */}
            <ul className="navbar-links">
                <li className={highlightSection === "dashboard" ? "active" : ""} onClick={(e) => setActiveSection("dashboard")}>Dashboard</li>
                <li className={highlightSection === "sessions" ? "active" : ""} onClick={(e) => setActiveSection("sessions")}>Sessions</li>
                <li className={highlightSection === "subjects" ? "active" : ""} onClick={(e) => setActiveSection("subjects")}>Subjects</li>
                <li className={highlightSection === "reports" ? "active" : ""} onClick={(e) => setActiveSection("reports")}>Reports</li>
            </ul>

            {/* Right: User actions */}
            <div className="navbar-right">
                <button className="icon-btn">🔔</button>

                <div className="user-menu">
                <span className="username">Hi, {user?.username ?? "Ahmed"}!</span>
                    <div className="logout">
                        <LogOut onClick={() => {
                            onClickLogout(true);
                        }} />
                    </div>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;
