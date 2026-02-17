import {use, useEffect, useState} from "react";
import { AuthContext } from "./AuthContext"
import {validateSession} from "../../api/authApi.js";

function AuthProvider( {children} ) {
    const [isAuth, setIsAuth] = useState(false);
    const [isChecking, setIsChecking] = useState(true);
    const [user, setUser] = useState(null);
    const [activeSection, setActiveSection] = useState(null);
    const [highlightSection, setHighlightSection] = useState(null);

    useEffect(() => {
        if (activeSection === "dashboard") {
            setHighlightSection("dashboard");
        } else if (activeSection === "sessions") {
            setHighlightSection("sessions");
        } else if (activeSection === "subjects") {
            setHighlightSection("subjects");
        } else {
            setHighlightSection("reports");
        }
    }, [activeSection]);

    const value = {
        isAuth,
        user,
        isChecking,
        activeSection,
        highlightSection,
        setIsAuth,
        setUser,
        setIsChecking,
        setActiveSection,
        setHighlightSection
    };


    useEffect(() => {
        const checkAuth = async () => {
            setIsChecking(true);
            const token = localStorage.getItem("token");

            if (token === null) {
                setIsAuth(false);
                setIsChecking(false);
                return;
            }

            try {
                const result = await validateSession(token);
                console.log(result);

                if (result.success) {
                    setIsAuth(true);
                    setUser(result.data);
                }
            } catch (e) {
                localStorage.removeItem("token");
                setIsAuth(false);
                setUser(null);
            }


            setIsChecking(false);
        }
        checkAuth();
    }, []);


    return (
        /*
        AuthContext er bare en “kanal”
        Provider er senderen
        value er det som sendes
        */

        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthProvider;