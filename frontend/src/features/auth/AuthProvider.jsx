import { useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";
import { validateSession } from "../../api/authApi.js";
import { logger } from "../utils/Logger.js";

function AuthProvider({ children }) {
    const [isAuth, setIsAuth] = useState(false);
    const [isChecking, setIsChecking] = useState(true);
    const [user, setUser] = useState(null);

    const value = {
        isAuth,
        user,
        isChecking,
        setIsAuth,
        setUser,
        setIsChecking,
    };

    useEffect(() => {
        const checkAuth = async () => {
            setIsChecking(true);
            const token = localStorage.getItem("token");

            if (!token) {
                setIsAuth(false);
                setUser(null);
                setIsChecking(false);
                return;
            }

            try {
                const result = await validateSession(token);

                if (result.success) {
                    setIsAuth(true);
                    setUser(result.data);
                    logger.log("User session validated.");
                } else {
                    // token invalid
                    localStorage.removeItem("token");
                    setIsAuth(false);
                    setUser(null);
                    logger.warn("Invalid session token.");
                }
            } catch (e) {
                logger.error("Session validation failed:", e);
                localStorage.removeItem("token");
                setIsAuth(false);
                setUser(null);
            } finally {
                setIsChecking(false);
            }
        };

        checkAuth();
    }, []);

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthProvider;