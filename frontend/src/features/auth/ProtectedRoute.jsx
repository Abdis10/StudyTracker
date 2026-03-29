import useAuth from "./useAuth.js";
import {Navigate, Outlet} from "react-router-dom";
import Loading from "../components/Loading.jsx";

function ProtectedRoute( {children} ) {
    const { isAuth, isChecking } = useAuth();

    if (isChecking) {
        return <Loading />;
    } else if (!isAuth) {
        return <Navigate to="/home" replace />;
    } else {
        return <Outlet />
    }
}

export default ProtectedRoute;