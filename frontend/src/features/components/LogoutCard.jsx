import "../css/logout.css";
import useAuth from "../auth/useAuth.js";

function LogoutCard( {onClickLogout} ) {
    const { setIsAuth, user, setUser } = useAuth();

    return (
        <div className="logout-card">
            <h3>Are you sure you want to logout?</h3>
            <div className="button-group">
                <button className="cancel" onClick={() => onClickLogout(false)}>Cancel</button>
                <button className="logout" onClick={() => {
                    localStorage.removeItem("token");
                    setIsAuth(false);
                    setUser(null);
                }}>Logout</button>
            </div>
        </div>

    );
}

export default LogoutCard;

