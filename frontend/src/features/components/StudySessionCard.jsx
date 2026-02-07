import "../css/studySessionCard.css"

function StudySessionCard() {
    return (
        <div className="study-session-card">
            <div className="card-body">
                <p className="label">Today's Study Time</p>
                <h1 className="time">2h 30m</h1>

                <hr />

                <div className="summary">
                    <p>
                        This Week: <span className="highlight">8h 45m</span>
                    </p>
                    <p>
                        This Month: <span className="highlight">22h 15m</span>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default StudySessionCard;
