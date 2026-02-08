import {useEffect, useState} from "react";
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
    const [successMessage, setSuccessMessage] = useState("");

    useEffect(() => {
        if (password === "" || confirmPassword === "") {
            setPasswordsMatch(undefined);
        }
        else {
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
                setSuccessMessage(result.data.message);
                console.log(result.data);
            } else {
                // vis feilmelding i UI
                setErrorMessage(result.data.error);
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
                    <fieldset>
                        <legend>Firstname</legend>
                        <input
                            type="text"
                            placeholder="Enter your firstname"
                            value={firstname}
                            onChange={(e) => setFirstname(e.target.value)}
                            autoComplete="given-name"
                            required
                        />
                    </fieldset>

                    <fieldset>
                        <legend>Lastname</legend>
                        <input
                            type="text"
                            placeholder="Enter your lastname"
                            value={lastname}
                            onChange={(e) => setLastname(e.target.value)}
                            required
                        />
                    </fieldset>

                    <fieldset>
                        <legend>Username</legend>
                        <input
                            type="text"
                            placeholder="Enter a username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </fieldset>

                    <fieldset>
                        <legend>Gender</legend>
                        <select
                            value={gender}
                            onChange={(e) => setGender(e.target.value)}
                            required
                            style={{ border: 'none', outline: 'none', height: '100%', background: 'transparent', padding: '0 10px' }}
                        >
                            <option value="" disabled>Select</option>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>
                    </fieldset>

                    <fieldset>
                        <legend>Email</legend>
                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </fieldset>

                    <fieldset
                        className={
                            passwordsMatch === true
                                ? "passTrue"
                                : passwordsMatch === false
                                    ? "passFalse"
                                    : ""
                        }
                    >
                        <legend>Password</legend>
                        <input
                            type="password"
                            placeholder="Create password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </fieldset>

                    <fieldset
                        className={
                            passwordsMatch === true
                                ? "passTrue"
                                : passwordsMatch === false
                                    ? "passFalse"
                                    : ""
                        }
                    >
                        <legend>Confirm password</legend>
                        <input
                            type="password"
                            placeholder="Confirm password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </fieldset>

                    {errorMessage !== "" ?
                        ( <p className="error-msg">{errorMessage}</p>) : null
                    }
                    {successMessage !== "" ?
                        (<p className="success-msg">{successMessage}</p>) : null
                    }

                    <button type="submit" className="signup-btn">
                        Sign up
                    </button>
                </form>
            </div>
        </div>
    );
}
