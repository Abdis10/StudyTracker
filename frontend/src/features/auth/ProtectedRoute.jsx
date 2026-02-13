import useAuth from "./useAuth.js";
import {Navigate} from "react-router-dom";
import Loading from "../components/Loading.jsx";

function ProtectedRoute( {children} ) {
    const { isAuth, isChecking } = useAuth();

    if (isChecking) {
        return <Loading />;
    } else if (!isAuth) {
        return <Navigate to="/login" replace />;
    } else {
        return children;
    }
}

export default ProtectedRoute;