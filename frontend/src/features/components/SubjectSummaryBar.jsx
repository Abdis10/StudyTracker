import "../css/subjectSummaryBar.css";

function SubjectSummaryBar() {
    return (
        <div className="subjects-summary">
            <h2>Subjects summary</h2>
            <div className="summary-bar">
                <div className="summary-card">
                    <p className="summary-label">Total Subjects</p>
                    <h3>{}</h3>
                </div>

                <div className="summary-card">
                    <p className="summary-label">Subjects Used</p>
                    <h3>{}</h3>
                </div>

                <div className="summary-card">
                    <p className="summary-label">Uncategorized Sessions</p>
                    <h3>{}</h3>
                </div>
            </div>
        </div>
    );
}

export default SubjectSummaryBar;