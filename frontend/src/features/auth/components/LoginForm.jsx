import { useState } from "react";
import { login } from "../../../api/authApi.js";
import useAuth from "../useAuth.js";
import { useNavigate } from "react-router-dom";
import { logger } from "../../utils/Logger.js";

export default function LoginForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const { setIsAuth, setUser } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const userData = {
            email,
            password
        };

        try {
            const result = await login(userData);

            if (result.success) {
                const data = result.data;

                // Lagre token først
                localStorage.setItem("token", data.token);

                // Sett auth state
                setUser(data.user ?? data); // fallback hvis du ikke har user-objekt separat
                setIsAuth(true);

                logger.log("Login successful");

                navigate("/dashboard", { replace: true });
            } else {
                logger.warn("Login failed:", result.data?.message);
            }
        } catch (e) {
            logger.error("Login network error:", e);
        }
    };

    return (
        <div className="login-page-container">
            <div className="login-box">
                <div className="welcome-msg">
                    <h1 className="login-header-msg">Login to StudyTracker</h1>
                    <h4 className="subtitle">Track Your Study Habits With Ease</h4>
                </div>

                <form onSubmit={handleSubmit}>
                    <input
                        type="email"
                        placeholder="Enter your email here"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <input
                        type="password"
                        placeholder="Password here"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <button type="submit" className="login-btn">
                        Login
                    </button>
                </form>
            </div>

            <div className="signup-bar">
                <h1 className="sidebar-header">Hello, Welcome!</h1>
                <p className="sidebar-p">
                    Do you want to track your journey? Start it here.
                </p>

                <button
                    className="btn"
                    onClick={() => navigate("/signup")}
                >
                    Sign up
                </button>
            </div>
        </div>
    );
}