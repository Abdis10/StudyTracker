import "../css/subjectSummaryBar.css";
import {useMemo} from "react";

function SubjectSummaryBar({ sessionData = [], subjectData = []} ) {
    const summary = useMemo(() => {
        const validSessions = Array.isArray(sessionData) ? sessionData : [];
        const validSubjects = Array.isArray(subjectData) ? subjectData : [];
        const usedSubjectIds = new Set(
            validSessions.map((session) => session.subjectId).filter((id) => id != null)
        );
        const uncategorizedSessions = validSessions.filter(
            (session) => session.subjectId == null
        ).length;


        return {
            totalSubjects: validSubjects.length,
            subjectsUsed: usedSubjectIds.size,
            uncategorizedSessions,
        };
    }, [subjectData, sessionData]);

    return (
        <div className="subjects-summary">
            <h2>Subjects summary</h2>
            <div className="summary-bar">
                <div className="summary-card">
                    <p className="summary-label">Total Subjects</p>
                    <h3>{summary.totalSubjects}</h3>
                </div>

                <div className="summary-card">
                    <p className="summary-label">Subjects Used</p>
                    <h3>{summary.subjectsUsed}</h3>
                </div>

                <div className="summary-card">
                    <p className="summary-label">Uncategorized Sessions</p>
                    <h3>{summary.uncategorizedSessions}</h3>
                </div>
            </div>
        </div>
    );
}

export default SubjectSummaryBar;
