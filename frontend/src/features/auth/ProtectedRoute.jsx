import useAuth from "./useAuth.js";
import {Navigate} from "react-router-dom";

function ProtectedRoute( {children} ) {
    const { isAuth } = useAuth();

    if (!isAuth) {
        return <Navigate to="/login" replace />;
    }
    return children;
}

export default ProtectedRoute;