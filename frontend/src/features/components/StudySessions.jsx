import { Plus } from "lucide-react";
import "../css/studySessions.css";
import {useEffect, useState} from "react";
import useAuth from "../auth/useAuth.js";
import {getSessions} from "../../api/sessionApi.js";

function StudySessions() {
    const { isAuth } = useAuth();
    const [sessions, setSessions] = useState([]);
    useEffect(() => {
        if (isAuth) {
            const sessionData =  async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await getSessions(token);
                    setSessions(result.data);
                } catch (e) {
                    console.log(e);
                }
            }
            sessionData();
        }

    }, []);


    const getProductivityClass = (score) => {
        if (score >= 7) return "high";
        if (score <= 4) return "low";
        return "medium";
    }

    console.log(sessions);

    return (
        <div className="sessions-page-container">
            <div className="study-sessions">
                <div className="sessions-header">
                    <h2>Study Sessions</h2>
                    <button className="log-btn">
                        <Plus size={18} />
                        Log Session
                    </button>
                </div>

                <div className="session-display">
                    {sessions.length > 0 ? (
                        <table>
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Hours</th>
                                <th>Productivity</th>
                                <th>Comment</th>
                            </tr>
                            </thead>
                            <tbody>
                            {sessions.map(({ id, date, hours, productivityScore, comment }) => (
                                <tr key={id}>
                                    <td>{date}</td>
                                    <td>{hours}</td>
                                    <td>
                                        <span className={`productivity ${getProductivityClass(productivityScore)}`}>
                                        {productivityScore}/10
                                    </span>
                                    </td>
                                    <td>{comment}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <div className="empty-state">
                            <h3>Hi, you have no sessions registered now... 🔎</h3>
                        </div>
                    )}
                </div>
            </div>

            <div className="session-diagram">
                {/* Chart later */}
            </div>
        </div>
    );
}

export default StudySessions;
