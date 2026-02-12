import {use, useEffect, useState} from "react";
import { AuthContext } from "./AuthContext"
import {validateSession} from "../../api/authApi.js";

function AuthProvider( {children} ) {
    const [isAuth, setIsAuth] = useState(false);
    const [isChecking, setIsChecking] = useState(false);
    const [user, setUser] = useState(null);

    const value = {
        isAuth,
        user,
        isChecking,
        setIsAuth,
        setUser,
        setIsChecking
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

            const result = await validateSession(token);
            console.log(result);
            if (result.success) {
                setIsAuth(true);
                setUser(result.data);
            } else {
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