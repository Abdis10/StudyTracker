import "../css/recentStudySessionsCard.css";

function RecentStudySessionsCard() {
    return (
        <div className="recent-sessions-card">
            <ul className="sessions-list">

                <li className="session-item">
                    <div className="session-main">
                        <span className="date">2025-01-01</span>
                        <span className="comment">Bra dag</span>
                    </div>

                    <div className="session-meta">
                        <span className="hours">3h</span>
                        <span className="productivity high">7/10</span>
                    </div>
                </li>

                <li className="session-item">
                    <div className="session-main">
                        <span className="date">2024-12-31</span>
                        <span className="comment">Greit fokus</span>
                    </div>

                    <div className="session-meta">
                        <span className="hours">2h</span>
                        <span className="productivity medium">5/10</span>
                    </div>
                </li>

            </ul>
        </div>
    );
}

export default RecentStudySessionsCard;
