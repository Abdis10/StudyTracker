import {useState} from "react";
import {login} from "../../../api/authApi.js";
import useAuth from "../useAuth.js";
import { useNavigate} from "react-router-dom";

export default function LoginForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    let { setIsAuth, user } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // login logic…
        const userData = {
            "email": email,
            "password": password
        }
        const result = await login(userData);
        console.log(result);

        if (result.success) {
            // naviger videre
            setIsAuth(true);
            console.log("LOGIN SUCCESS – navigating");
            navigate("/dashboard", {replace: true}); // replace gjør at brukeren ikke kan gå tilbake til login
            const data = result.data;
            localStorage.removeItem("token");
            localStorage.setItem("token", data.token);
            console.log(data);
        }
    }

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
                    />

                    <input
                        type="password"
                        placeholder="Password here"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <button type="submit" className="login-btn">Login</button>
                </form>
            </div>
            <div className="signup-bar">
                <h1 className="sidebar-header">Hello, Welcome!</h1>
                <p className="sidebar-p">Do you want to track Your Journey, start it Here</p>
                <a href="/signup"><button className="btn" type="submit">Sign up</button></a>
            </div>
        </div>
    );
}
