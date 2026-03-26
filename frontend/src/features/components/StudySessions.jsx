import { MoreVertical, Plus } from "lucide-react";
import "../css/studySessions.css";
import { useEffect, useState } from "react";
import useAuth from "../auth/useAuth.js";
import { deleteSession, getSessions, registerSession, updateSession } from "../../api/sessionApi.js";
import LogSessionCard from "./LogSessionCard.jsx";
import { toast, Toaster } from "react-hot-toast";
import { Pie, PieChart, Tooltip, ResponsiveContainer } from "recharts";
import { logger } from "../utils/Logger.js";

function StudySessions() {
    const { isAuth } = useAuth();
    const [sessions, setSessions] = useState([]);
    const [showCard, setShowCard] = useState(false);
    const [openMenuId, setOpenMenuId] = useState(null);
    const [editingSession, setEditingSession] = useState(null);
    const [sessionUpdator, setSessionUpdator] = useState(false);
    const [data, setData] = useState({ high: [], medium: [], low: [] });
    const [currentPage, setCurrentPage] = useState(1);
    const sessionsPerPage = 3;



    useEffect(() => {
        if (isAuth) {
            const sessionData = async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await getSessions(token);

                    if (result.success) {
                        setSessions(result.data);
                    }
                } catch (e) {
                    logger.error("Fetching sessions failed:", e);
                }
            };
            sessionData();
        }
    }, [sessionUpdator, isAuth]);


    const getProductivityClass = (score) => {
        if (score >= 7) return "high";
        if (score <= 4) return "low";
        return "medium";
    };

    const handleEdit = (id) => {
        const sessionToEdit = sessions.find(s => s.id === id);
        logger.log("Editing session:", sessionToEdit);
        setEditingSession(sessionToEdit);
        setShowCard(true);
        setOpenMenuId(null);
    };

    const handleDelete = async (id) => {
        try {
            const token = localStorage.getItem("token");
            const result = await deleteSession(id, token);

            if (result.success) {
                toast.success("Session is successfully deleted.");
                logger.log("Delete result:", result);
                setSessionUpdator(prev => !prev);

                if (currentSessions.length === 1 && currentPage > 1) {
                    setCurrentPage(prev => prev -1);
                }

            } else {
                toast.error("Couldn't delete session!");
            }
        } catch (e) {
            logger.error("Delete network error:", e);
            toast.error("Something went wrong with the connection to the server!");
        }
        setOpenMenuId(null);
    };

    const handleSessionRegistration = async (sessionData) => {
        try {
            const token = localStorage.getItem("token");
            const result = await registerSession(sessionData, token);

            if (result.success) {
                toast.success(result.data.message);
                logger.log("Register message:", result.data.message);
                setSessionUpdator(prev => !prev);
            } else {
                toast.error(result.data.message);
            }
        } catch (e) {
            logger.error("Register network error:", e);
            toast.error("Something went wrong with the connection to the server!");
        }
    };

    const handleUpdateSession = async (sessionData, id) => {
        try {
            const token = localStorage.getItem("token");
            const result = await updateSession(sessionData, id, token);

            if (result.success) {
                toast.success(result.data.message);
                logger.log("Update message:", result.data.message);
                setSessionUpdator(prev => !prev);
            } else {
                toast.error(result.data.message);
            }
        } catch (e) {
            logger.error("Update network error:", e);
            toast.error("Something went wrong with the connection to the server!");
        }
    };

    const calculateStats = (sessions) => {
        const initialStats = { high: [], medium: [], low: [] };

        const newStats = sessions.reduce((acc, session) => {
            const category = getProductivityClass(session.productivityScore);
            return {
                ...acc,
                [category]: [...acc[category], session.productivityScore]
            };
        }, initialStats);

        setData(newStats);
    };

    useEffect(() => {
        if (sessions.length > 0) {
            calculateStats(sessions);
        }
    }, [sessions]);

    const COLORS = ["#22C55E", "#ffd400", "#EF4444"];

    const dataWithColors = Object.keys(data)
        .map((key, index) => ({
            name: key,
            value: data[key].length,
            fill: COLORS[index % COLORS.length]
        }))
        .filter(item => item.value > 0);

    // beregn indekser
    const indexOfLastItem = currentPage * sessionsPerPage;
    const indexOfFirstItem = indexOfLastItem - sessionsPerPage;

    // Hent ut kun de elementene som skal vises på nåværende side
    const currentSessions = sessions.slice(indexOfFirstItem, indexOfLastItem);

    // beregn antall sider totalt
    const totalPages = (Math.ceil(sessions.length / sessionsPerPage));

    return (
        <div className="sessions-page-container">
            <Toaster />

            <div className="study-sessions">
                <div className="sessions-header">
                    <h2>Study Sessions</h2>
                    <button
                        className="log-btn"
                        onClick={() => {
                            setEditingSession(null);
                            setShowCard(true);
                        }}
                    >
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
                            {currentSessions.map(({ id, date, hours, productivityScore, comment }) => (
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
                {sessions.length > 0 ?
                    ( <div className="pagination">
                    <button disabled={currentPage === 1} onClick={() => setCurrentPage( prev => prev - 1 )}>Previous</button>
                    <span> Side {currentPage} av {totalPages || 1} </span>
                    <button disabled={currentPage === totalPages} onClick={() => setCurrentPage(prev => prev + 1)}>Next</button>
                </div> ) : ( <h3>Start logging sessions!</h3> )}
            </div>

            <div className="session-diagram">
                <h3>Productivity rate</h3>
                <div className="chart-shell">
                    <ResponsiveContainer width="100%" height="100%">
                        <PieChart>
                            <Pie
                                data={dataWithColors}
                                dataKey="value"
                                nameKey="name"
                                outerRadius="70%"
                                innerRadius="35%"
                                paddingAngle={4}
                                cornerRadius={8}
                                label={({ percent }) => `${(percent * 100).toFixed(0)}%`}
                            />
                            <Tooltip
                                formatter={(value, name) => {
                                    const total = dataWithColors.reduce((sum, entry) => sum + entry.value, 0);
                                    const percent = ((value / total) * 100).toFixed(0);
                                    return [`${value} (${percent}%)`, name];
                                }}
                            />
                        </PieChart>
                    </ResponsiveContainer>
                </div>

                <div className="pie-legend">
                    {dataWithColors.map(item => (
                        <div key={item.name} className="legend-item">
                            <span
                                className="legend-color"
                                style={{ backgroundColor: item.fill }}
                            />
                            <span className="legend-text">
                                {item.value} {item.name.charAt(0).toUpperCase() + item.name.slice(1)}
                            </span>
                        </div>
                    ))}
                </div>
            </div>

            {showCard && (
                <LogSessionCard
                    initialData={editingSession}
                    onClose={() => {
                        setShowCard(false);
                        setEditingSession(null);
                    }}
                    onSave={async (updatedSession) => {
                        if (editingSession) {
                            const editedSessionId = editingSession.id;
                            await handleUpdateSession(updatedSession, editedSessionId);
                        } else {
                            await handleSessionRegistration(updatedSession);
                        }

                        setShowCard(false);
                        setEditingSession(null);
                    }}
                />
            )}
        </div>
    );
}

export default StudySessions;
