import "../css/navbar.css";

function Navbar() {
    return (
        <nav className="navbar">
            {/* Left: Logo */}
            <img src="" />
            <div className="navbar-left">
                <div className="logo">
                    📘 <span>StudyTracker</span>
                </div>
            </div>

            {/* Center: Navigation links */}
            <ul className="navbar-links">
                <li className="active">Dashboard</li>
                <li>Tasks</li>
                <li>Subjects</li>
                <li>Reports</li>
            </ul>

            {/* Right: User actions */}
            <div className="navbar-right">
                <button className="icon-btn">🔔</button>

                <div className="user-menu">
                    <span className="username">Hi, James!</span>
                    <span className="caret">▾</span>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;
