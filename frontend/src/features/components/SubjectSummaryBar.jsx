import "../css/subjectSummaryBar.css";
import {useEffect, useMemo, useState} from "react";

function SubjectSummaryBar({ sessionData = [], subjectData = []} ) {
    const subjectsLength = subjectData?.length || 0;
    console.log(sessionData);
    console.log(subjectData);
    const summary = useMemo(() => {
        const subjectIdsUsed = new Set(
            sessionData.map( s => s.subjectId).filter(id => id != null)
        );


        return {
            subjectUsed: subjectIdsUsed.size
        }
    }, [subjectData, sessionData]);

    return (
        <div className="subjects-summary">
            <h2>Subjects summary</h2>
            <div className="summary-bar">
                <div className="summary-card">
                    <p className="summary-label">Total Subjects {subjectsLength}</p>
                    <h3>{}</h3>
                </div>

                <div className="summary-card">
                    <p className="summary-label">Subjects Used {summary.subjectIdsUsed}</p>
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