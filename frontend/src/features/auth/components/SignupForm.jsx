import {useEffect, useState} from "react";
import { signup } from "../../../api/authApi.js";
import {useNavigate} from "react-router-dom";

export default function SignupForm() {
    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [username, setUsername] = useState("");
    const [gender, setGender] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordsMatch, setPasswordsMatch] = useState(undefined);
    const [formStatus, setFormStatus] = useState("idle");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

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

    const handleInputChange = (setter) => (e) => {
        setter(e.target.value);

        if (formStatus !== "idle") {
            setFormStatus("idle");
            setMessage("");
        }
    };


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
                setFormStatus("success");
                setMessage("🎉 Your account has been created. You will be redirected to login in a moment…");

                const timoutId = setTimeout( () => {
                    navigate("/login", {replace: true});
                }, 6000);
                return () => clearTimeout(timoutId);
            } else {
                // vis feilmelding i UI
                setFormStatus("error");
                setMessage(result.data?.error ?? "Signup failed");
            }


        } else {
            setFormStatus("error");
            setMessage("Passwords do not match");
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
                            onChange={handleInputChange(setFirstname)}
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
                            onChange={handleInputChange(setLastname)}
                            required
                        />
                    </fieldset>

                    <fieldset>
                        <legend>Username</legend>
                        <input
                            type="text"
                            placeholder="Enter a username"
                            value={username}
                            onChange={handleInputChange(setUsername)}
                            required
                        />
                    </fieldset>

                    <fieldset>
                        <legend>Gender</legend>
                        <select
                            value={gender}
                            onChange={handleInputChange(setGender)}
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
                            onChange={handleInputChange(setEmail)}
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
                            onChange={handleInputChange(setPassword)}
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
                            onChange={handleInputChange(setConfirmPassword)}
                            required
                        />
                    </fieldset>

                    {formStatus === "error" && (
                        <p className="error-msg">{message}</p>
                    )}

                    {formStatus === "success" && (
                        <p className="success-msg">{message}</p>
                    )}

                    <button type="submit" className="signup-btn">
                        Sign up
                    </button>
                </form>
            </div>
        </div>
    );
}
