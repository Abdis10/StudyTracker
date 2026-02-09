import {useState} from "react";
import { AuthContext } from "./AuthContext"

function AuthProvider( {children} ) {
    const [isAuth, setIsAuth] = useState(false);
    const [user, setUser] = useState(null);

    const value = {
        isAuth,
        user,
        setIsAuth,
        setUser
    };

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