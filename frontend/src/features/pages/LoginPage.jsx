import { useState } from 'react'
import LoginForm from "../auth/components/LoginForm.jsx";
import "../auth/css/LoginForm.css"

function LoginPage() {
    return (
        <div className="login-page-container">
            <div className="login-box">
                <h1 className="welcome-msg">Sign in to StudyTracker</h1>
                <p className="subtitle">Track your study habits and goals with ease</p>
                <LoginForm/>
            </div>
        </div>
    )
}

export default LoginPage;
