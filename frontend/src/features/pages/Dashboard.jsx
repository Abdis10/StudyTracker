import Navbar from "../components/Navbar.jsx";
import Card from "../components/Card.jsx";
import StudySessionCard from "../components/StudySessionCard.jsx";
import "../css/dashboard.css";
import RecentStudySessionsCard from "../components/RecentStudySessionsCard.jsx";
import useAuth from "../auth/useAuth.js";
import LogoutCard from "../components/LogoutCard.jsx";
import {useState} from "react";

function Dashboard() {
    const { setIsAuth, user } = useAuth();
    return (
        <>
            <div className="dashboard-container">
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
        </>
    )
}


export default Dashboard;