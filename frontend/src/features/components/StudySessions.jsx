import {MoreVertical, Plus} from "lucide-react";
import "../css/studySessions.css";
import {useEffect, useState} from "react";
import useAuth from "../auth/useAuth.js";
import {deleteSession, getSessions, registerSession, updateSession} from "../../api/sessionApi.js";
import LogSessionCard from "./LogSessionCard.jsx";
import {toast, Toaster} from "react-hot-toast";
import {Pie, PieChart, Tooltip} from "recharts";

function StudySessions() {
    const { isAuth } = useAuth();
    const [sessions, setSessions] = useState([]);
    const [showCard, setShowCard] = useState(false);
    const [openMenuId, setOpenMenuId] = useState(null);
    const [editingSession, setEditingSession] = useState(null);
    const [sessionUpdator, setSessionUpdator] = useState(false);
    const [data, setData] = useState({
        high: [],
        medium: [],
        low: []
    });

    useEffect(() => {
        if (isAuth) {
            const sessionData =  async () => {
                try {
                    const token = localStorage.getItem("token");
                    const result = await getSessions(token);

                    if (result.success) {
                        setSessions(result.data);
                        console.log(sessions);
                    }
                } catch (e) {
                    console.error(e);
                }
            }
            sessionData();
        }

    }, [sessionUpdator]);


    const getProductivityClass = (score) => {
        if (score >= 7) return "high";
        if (score <= 4) return "low";
        return "medium";
    }

    console.log(sessions);

    const handleEdit = (id) => {
        const sessionToEdit = sessions.find( s => s.id === id);
        console.log(sessionToEdit);
        setEditingSession(sessionToEdit);
        setShowCard(true);
        setOpenMenuId(null);
    };

    const handleDelete = async (id) =>  {
        try {
            const token = localStorage.getItem("token");
            const result = await deleteSession(id, token);
            if (result.success) {
                toast.success("Session is successfully deleted.");
                console.log(result);
                setSessionUpdator(true);
            } else {
                toast.error("Couldn't delete session!");
                setSessionUpdator(false);
            }
        } catch (e) {
            console.error("Network error", e);
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
                console.log(result.data.message);
                setSessionUpdator(true);
            } else {
                toast.error(result.data.message);
                setSessionUpdator(false);
            }
        } catch (e) {
            console.error("Network error", e);
            toast.error("Something went wrong with the connection to the server!");
        }
    };


    const handleUpdateSession =  (sessionData, id) => {
        try {
            const token = localStorage.getItem("token");
            const result = updateSession(sessionData, id, token);
            if (result.success) {
                toast.success(result.data.message);
                console.log(result.data.message);
                setSessionUpdator(true);
            } else {
                toast.error(result.data.message);
                setSessionUpdator(false);
            }
        } catch (e) {
            console.error("Network error", e);
            toast.error("Something went wrong with the connection to the server!");
        }
    };

    const addScore = (category, score) => {
        setData(prev => ({
            ...prev, [category]: [...prev[category], score]
        }));
    };

    const calculateStats = (sessions) => {
        const initialStats = { high: [], medium: [], low: [] };

        const newStats = sessions.reduce( (acc, session) => {
         const category = getProductivityClass(session.productivityScore);

         return {
             ...acc,
            [category]: [...acc[category], session.productivityScore]
         };
        }, initialStats);
        setData(newStats);
    }

    useEffect(() => {
        if (sessions && sessions.length > 0) {
            calculateStats(sessions);
        }
    }, [sessions]);

    const COLORS = ["#22C55E", "#ffd400", "#EF4444"];
    const dataWithColors = Object.keys(data).map((key, index) => ({
      name: key,
      value: data[key].length,
      fill: COLORS[index % COLORS.length]
    })).filter(item => item.value > 0);

    return (
        <div className="sessions-page-container">
            <div><Toaster/></div>
            <div className="study-sessions">
                <div className="sessions-header">
                    <h2>Study Sessions</h2>
                    <button className="log-btn" onClick={() => {
                        setEditingSession(null);
                        setShowCard(true);
                    }}>
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
                                                    <button onClick={() => {
                                                        setSessionUpdator(false);
                                                        handleEdit(id);
                                                    }}>Edit</button>
                                                    <button
                                                        className="danger"
                                                        onClick={()=> {
                                                            setSessionUpdator(true);
                                                            handleDelete(id);
                                                        }}
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
                <PieChart width={400} height={400}>
                    <Pie
                        data={dataWithColors}
                        dataKey="value"
                        nameKey="name"
                        outerRadius={150}
                        innerRadius={60}
                    />
                    <Tooltip />
                </PieChart>

            </div>
            {showCard && (
                <LogSessionCard initialData={editingSession}
                                onClose={() => {
                                    setShowCard(false);
                                    setEditingSession(null);
                                }}
                                onSave={ async (updatedSession) => {
                                    // Update existing session
                                    if (editingSession) {
                                        try {
                                            const editedSessionId = editingSession.id;
                                            updatedSession.id = editingSession.id;
                                            handleUpdateSession(updatedSession, editedSessionId);
                                            setSessions(prev =>
                                                prev.map(s => s.id === updatedSession.id ? updatedSession : s));
                                        } catch (e) {
                                            setErrorMsg(e.message);
                                        }

                                    } else {
                                        // Create new session
                                        try {
                                            await handleSessionRegistration(updatedSession);
                                            setSessions(prev => [...prev, updatedSession]);
                                        } catch (e) {
                                            setMessage(e);
                                        }
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
