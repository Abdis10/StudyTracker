import "../css/recentStudySessionsCard.css";
import {useState} from "react";

function RecentStudySessionsCard( {recentStudySessions} ) {
    const [latestSession, setLatestSessions] = useState([]);



    const getProductivityClass = (score) => {
        if (score >= 7) return "high";
        if (score <= 4) return "low";
        return "medium";
    };

    const util = (data) => {
        let num = data;
        let hours = Math.floor(num);
        let minutes = (Math.ceil(num) - num)* 60;
        return hours + "h" + " " + minutes + "m";
    }

    return (
        <div className="recent-sessions-card">
            <ul className="sessions-list">

                {recentStudySessions.length > 0 ?
                    recentStudySessions.map( session =>
                        <li className="session-item">
                            <div className="session-main">
                                <span className="date">{session.date}</span>
                                <span className="comment">{session.comment}</span>
                            </div>

                            <div className="session-meta">
                                <span className="hours">{util(session.hours)}</span>
                                <span className={`productivity ${getProductivityClass(session.productivityScore)}`}>{session.productivityScore}/10</span>
                            </div>
                        </li>
                     ) : ""

                }
            </ul>
        </div>
    );
}

export default RecentStudySessionsCard;
