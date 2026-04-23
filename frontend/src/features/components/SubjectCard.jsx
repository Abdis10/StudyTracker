import { useMemo } from "react";

function formatDate(date) {
    if (!date) {
        return null;
    }

    const parsedDate = new Date(date);
    if (Number.isNaN(parsedDate.getTime())) {
        return date;
    }

    return parsedDate.toLocaleDateString("en-US", {
        year: "numeric",
        month: "short",
        day: "numeric",
    });
}

function SubjectCard({ subject, sessions }) {
    const stats = useMemo(() => {
        if (!sessions.length) {
            return {
                totalSessions: 0,
                totalHours: 0,
                avgProductivity: 0,
                lastStudied: null,
            };
        }

        const totalSessions = sessions.length;

        const totalHours = sessions.reduce(
            (sum, s) => sum + (Number(s.hours) || 0),
            0
        );

        const avgProductivity =
            sessions.reduce((sum, s) => sum + (Number(s.productivityScore) || 0), 0) /
            totalSessions;

        const lastStudied = sessions.reduce((latest, s) => {
            return new Date(s.date) > new Date(latest) ? s.date : latest;
        }, sessions[0].date);

        return {
            totalSessions,
            totalHours,
            avgProductivity,
            lastStudied,
        };
    }, [sessions]);

    const productivityLabel =
        stats.avgProductivity >= 7 ? "Strong" :
        stats.avgProductivity >= 5 ? "Steady" :
        stats.totalSessions ? "Needs attention" : "No data";

    return (
        <div className="subject-card">
            <div className="subject-header">
                <div>
                    <p className="subject-card-label">Subject</p>
                    <h3>{subject?.name || "Unnamed subject"}</h3>
                </div>
                <span className="subject-status-pill">{productivityLabel}</span>
            </div>

            <div className="subject-stats">
                <div className="subject-stat">
                    <p>Sessions</p>
                    <h4>{stats.totalSessions}</h4>
                </div>

                <div className="subject-stat">
                    <p>Hours</p>
                    <h4>{stats.totalHours.toFixed(1)}</h4>
                </div>

                <div className="subject-stat">
                    <p>Avg Productivity</p>
                    <h4>{stats.avgProductivity.toFixed(1)}/10</h4>
                </div>
            </div>

            <div className="subject-footer">
                <div>
                    <p className="subject-footer-label">Last studied</p>
                    <p>{stats.lastStudied ? formatDate(stats.lastStudied) : "No sessions yet"}</p>
                </div>
            </div>
        </div>
    );
}

export default SubjectCard;
