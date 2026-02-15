import "../css/navbar.css";
import useAuth from "../auth/useAuth.js";
import { LogOut } from 'lucide-react';
import {useState} from "react";

function Navbar( {onClickLogout} ) {
    const {user} = useAuth();

    return (
        <nav className="navbar">
            {/* Left: Logo */}
            <img className="user-img"  src="" />
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
                    <span className="username">Hi, {user.username}!</span>
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
