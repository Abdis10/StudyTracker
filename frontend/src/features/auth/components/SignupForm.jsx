import {use, useEffect, useState} from "react";
import { signup } from "../../../api/authApi.js";

export default function SignupForm() {
    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [username, setUsername] = useState("");
    const [gender, setGender] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordsMatch, setPasswordsMatch] = useState(null);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        if (password === "" || confirmPassword === "") {
            setPasswordsMatch(undefined);
        }
        else if (password ===! "" && confirmPassword ===! "") {
            if (password === confirmPassword) {
                setPasswordsMatch(true);
            }
            else {
                setPasswordsMatch(false);
            }
        }
    }, [password, confirmPassword]);

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
                setErrorMessage(data.error);
                console.log(data.error);
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
                    <label> Firstname
                        <input
                            type="firstname"
                            placeholder="Enter your firstname"
                            value={firstname}
                            onChange={(e) => setFirstname(e.target.value)}
                            required
                        />
                    </label>

                    <label> Lastname
                    <input
                        type="lastname"
                        placeholder="Enter your lastname"
                        value={lastname}
                        onChange={(e) => setLastname(e.target.value)}
                        required
                    />
                    </label>

                    <label> Username
                    <input
                        type="username"
                        placeholder="Enter a username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    </label>

                    <label> Gender
                        <input
                            type="gender"
                            placeholder="Your gender (F/M)"
                            value={gender}
                            onChange={(e) => setGender(e.target.value)}
                            required
                        />
                    </label>

                    <label> Email
                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </label>

                    <label> Password
                        <input
                            className={passwordsMatch === undefined ? "" : passwordsMatch ?  "passTrue" : "passFalse"}
                            type="password"
                            placeholder="Create password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </label>

                    <label> Confirm password
                        <input
                            className={passwordsMatch === undefined ? "" : passwordsMatch ?  "passTrue" : "passFalse"}
                            type="password"
                            placeholder="Confirm password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </label>

                    {errorMessage !== "" ? (<p className="error-msg">{errorMessage}</p>) : null}

                    <button type="submit" className="signup-btn">
                        Sign up
                    </button>
                </form>
            </div>
        </div>
    );
}
