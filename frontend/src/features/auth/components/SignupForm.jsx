import { useState } from "react";
import { signup } from "../../../api/authApi.js";

export default function SignupForm() {
    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [username, setUsername] = useState("");
    const [gender, setGender] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = {
            firstname,
            lastname,
            username,
            gender,
            email,
            password,
        };

        if (password === confirmPassword) {
            const result = await signup(formData);

            if (result.success) {
                // naviger videre / vis suksess
                //console.log(result.data);
            } else {
                // vis feilmelding
                const data = await result.data;
                console.log(data)
            }


        } else {
            console.log("Couldn't confirm password");
        }

    };



    return (
        <div className="signup-page-container">
            <div className="signup-box">
                <div className="welcome-msg">
                    <h1 className="signup-header-msg">Create Account</h1>
                    <h4 className="subtitle">Start tracking your study journey</h4>
                </div>

                <form onSubmit={handleSubmit}>
                    <input
                        type="firstname"
                        placeholder="Enter your firstname"
                        value={firstname}
                        onChange={(e) => setFirstname(e.target.value)}
                    />

                    <input
                        type="lastname"
                        placeholder="Enter your lastname"
                        value={lastname}
                        onChange={(e) => setLastname(e.target.value)}
                    />

                    <input
                        type="username"
                        placeholder="Enter a username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <input
                        type="gender"
                        placeholder="Your gender (F/M)"
                        value={gender}
                        onChange={(e) => setGender(e.target.value)}
                    />

                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        type="password"
                        placeholder="Create password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <input
                        type="password"
                        placeholder="Confirm password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />

                    <button type="submit" className="signup-btn">
                        Sign up
                    </button>
                </form>
            </div>
        </div>
    );
}
