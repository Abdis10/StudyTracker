import Navbar from "../components/Navbar.jsx";
import Card from "../components/Card.jsx";
import StudySessionCard from "../components/StudySessionCard.jsx";
import "../css/dashboard.css";
import RecentStudySessionsCard from "../components/RecentStudySessionsCard.jsx";
import useAuth from "../auth/useAuth.js";
import LogoutCard from "../components/LogoutCard.jsx";
import {useEffect, useState} from "react";
import {getSessions} from "../../api/sessionApi.js";
import {logger} from "../utils/Logger.js";
import {getDashboardData} from "../../api/dashboardApi.js";

function Dashboard() {
    const { isAuth, user } = useAuth();
    const [studySummaryData, setStudySummaryData] = useState([]);
    const [recentStudySessions, setRecentStudySessions] = useState([]);
    const [weeklyProgressData, setWeeklyProgressData] = useState([]);

    useEffect(() => {
        if (isAuth) {
            const dashboardData = async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await getDashboardData(token);

                    if (result.success) {
                        console.log(result.data);
                        setStudySummaryData(result.data.studySummaryDTO);
                        setRecentStudySessions(result.data.recentStudySessionsDTO);
                        setWeeklyProgressData(result.data.weeklyProgressDTO);
                    }
                } catch (e) {
                    logger.error("Fetching sessions failed:", e);
                }
            };
            dashboardData();
        }
    }, [isAuth]);


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