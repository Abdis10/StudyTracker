import Navbar from "../components/Navbar.jsx";
import Card from "../components/Card.jsx";
import StudySessionCard from "../components/StudySessionCard.jsx";
import "../css/dashboard.css";
import RecentStudySessionsCard from "../components/RecentStudySessionsCard.jsx";
import useAuth from "../auth/useAuth.js";
import {useNavigate} from "react-router-dom";
import LogoutCard from "../components/LogoutCard.jsx";
import {useState} from "react";

function Dashboard() {
    const { setIsAuth, user } = useAuth();
    const [logoutIsClicked, setLogoutIsClicked] = useState(false);
    console.log(logoutIsClicked);

    return (
        <>
            <div className={`${logoutIsClicked ? "container-blended" : "container"}`}>
                <div className="nav">
                    <Navbar onClickLogout={setLogoutIsClicked} />
                </div>

                <div className="welcome-section">
                    <h1>Welcome Back, {user.firstname} !</h1>
                    <p className="msg">
                        Track your study progress and stay on top of your productivity.
                    </p>
                </div>

                <div className="study-summary-section">
                    <Card title="Study Summary">
                        <StudySessionCard />
                    </Card>
                </div>

                <div className="recent-s-sessions">
                    <Card title="Recent Study Sessions">
                        <RecentStudySessionsCard />
                    </Card>
                </div>

                <div className="weekly-chart">
                    <Card title="Weekly Progress" />
                </div>
            </div>

            {/* ✅ Modal OUTSIDE grid */}
            {logoutIsClicked && (
                <div className="logout-overlay">
                    <div className="logout-box">
                        <LogoutCard />
                    </div>
                </div>
            )}
        </>
    )
}


export default Dashboard;