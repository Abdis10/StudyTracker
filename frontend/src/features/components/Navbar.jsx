import "../css/navbar.css";
import useAuth from "../auth/useAuth.js";
import { BookOpenCheck, LogOut } from 'lucide-react';
import { NavLink } from "react-router-dom";

function Navbar( { onClickLogout } ) {
    const { user} = useAuth();

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
                <li>
                    <NavLink to="/dashboard"
                             end
                             className={({ isActive }) =>
                                 isActive ? "navbar-link active" : "navbar-link"
                             }
                    >
                        Dashboard
                    </NavLink>
                </li>
                <li>
                    <NavLink
                        to="/sessions"
                        className={({ isActive }) =>
                            isActive ? "navbar-link active" : "navbar-link"
                        }
                    >
                        Sessions
                    </NavLink>

                </li>
                <li>
                    <NavLink
                        to="/subjects"
                        className={({ isActive }) =>
                            isActive ? "navbar-link active" : "navbar-link"
                        }
                    >
                        Subjects
                    </NavLink>
                </li>
                <li>
                    <NavLink
                        to="/reports"
                        className={({ isActive }) =>
                            isActive ? "navbar-link active" : "navbar-link"
                        }
                    >
                        Reports
                    </NavLink>
                </li>
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
