import "../css/logout.css";

function LogoutCard() {


    return (
        <div className="logout-card">
            <h3>Are you sure you want to logout?</h3>
            <div className="button-group">
                <button className="cancel">Cancel</button>
                <button className="logout">Logout</button>
            </div>
        </div>

    );
}

export default LogoutCard;

