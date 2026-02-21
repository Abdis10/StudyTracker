import {MoreVertical, Plus} from "lucide-react";
import "../css/studySessions.css";
import {useEffect, useState} from "react";
import useAuth from "../auth/useAuth.js";
import {getSessions, registerSession} from "../../api/sessionApi.js";
import LogSessionCard from "./LogSessionCard.jsx";

function StudySessions() {
    const { isAuth } = useAuth();
    const [sessions, setSessions] = useState([]);
    const [showCard, setShowCard] = useState(false);
    const [data, setData] = useState({});
    const [openMenuId, setOpenMenuId] = useState(null);
    useEffect(() => {
        if (!showCard) {
            const sessionRegistration = async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await registerSession(data, token);
                    console.log(result.message);
                } catch (e) {
                    console.error(e);
                }
            }

            sessionRegistration();
        }
    }, [data]);

    useEffect(() => {
        if (isAuth) {
            const sessionData =  async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await getSessions(token);
                    setSessions(result.data);
                } catch (e) {
                    console.error(e);
                }
            }
            sessionData();
        }

    }, [isAuth]);


    const getProductivityClass = (score) => {
        if (score >= 7) return "high";
        if (score <= 4) return "low";
        return "medium";
    }

    console.log(sessions);

    const handleEdit = (id) => {
        console.log("Edit session:", id);
        setOpenMenuId(null);
    };

    const handleDelete = (id) => {
        console.log("Delete session:", id);
        setOpenMenuId(null);
    };

    return (
        <div className="sessions-page-container">
            <div className="study-sessions">
                <div className="sessions-header">
                    <h2>Study Sessions</h2>
                    <button className="log-btn" onClick={() => setShowCard(true)}>
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
                                <th>Actions</th>
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

                                    <td>
                                        <div className="menu-wrapper">
                                            <button onClick={() =>
                                                setOpenMenuId(openMenuId === id ? null : id)
                                            }>
                                                <MoreVertical size={18} />
                                            </button>

                                            {openMenuId === id && (
                                                <div className="dropdown">
                                                    <button onClick={() => handleEdit(id)}>Edit</button>
                                                    <button
                                                        className="danger"
                                                        onClick={() => handleDelete(id)}
                                                    >
                                                        Delete
                                                    </button>
                                                </div>
                                            )}
                                        </div>
                                    </td>
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
            {showCard && (
                <LogSessionCard onClose={() => setShowCard(false)} onSave={(newSession) => {
                setSessions(prev => [...prev, newSession]);
                setShowCard(false);
                setData(newSession);
                }} />
            )}
        </div>
    );
}

export default StudySessions;
