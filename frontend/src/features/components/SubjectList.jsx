import { useMemo } from "react";
import SubjectCard from "./SubjectCard.jsx";
import { groupSessionsBySubject } from "../utils/groupSessionsBySubject";
import "../css/subjectList.css";

function SubjectList({ subjects = [], sessions = [] }) {
    const organizedSubjects = useMemo(() => {
        const grouped = groupSessionsBySubject(sessions);

        return subjects
            .map((subject) => {
                const subjectSessions = grouped.get(subject.id) || [];
                const lastStudiedTimestamp = subjectSessions.reduce((latest, session) => {
                    const timestamp = new Date(session.date).getTime();
                    return Number.isNaN(timestamp) ? latest : Math.max(latest, timestamp);
                }, 0);

                return {
                    subject,
                    sessions: subjectSessions,
                    totalSessions: subjectSessions.length,
                    lastStudiedTimestamp,
                };
            })
            .sort((a, b) => {
                if (b.totalSessions !== a.totalSessions) {
                    return b.totalSessions - a.totalSessions;
                }

                if (b.lastStudiedTimestamp !== a.lastStudiedTimestamp) {
                    return b.lastStudiedTimestamp - a.lastStudiedTimestamp;
                }

                return (a.subject?.name || "").localeCompare(b.subject?.name || "");
            });
    }, [subjects, sessions]);

    if (!subjects.length) {
        return (
            <section className="subject-list-section">
                <div className="subject-list-header">
                    <div className="subject-list-heading">
                        <p className="subject-list-eyebrow">Subjects</p>
                        <h2>Your subject overview</h2>
                        <p className="subject-list-description">
                            Track how each subject is progressing at a glance.
                        </p>
                    </div>
                </div>
                <div className="subject-list-empty">
                    <p>No subjects created yet.</p>
                </div>
            </section>
        );
    }

    return (
        <section className="subject-list-section">
            <div className="subject-list-header">
                <div className="subject-list-heading">
                    <p className="subject-list-eyebrow">Subjects</p>
                    <h2>Your subject overview</h2>
                    <p className="subject-list-description">
                        Sorted by the subjects you are using the most right now.
                    </p>
                </div>
                <div className="subject-list-meta">
                    <span className="subject-list-count">
                        {subjects.length} {subjects.length === 1 ? "subject" : "subjects"}
                    </span>
                </div>
            </div>

            <div className="subject-list-grid">
                {organizedSubjects.map(({ subject, sessions: subjectSessions }) => (
                    <SubjectCard
                        key={subject.id}
                        subject={subject}
                        sessions={subjectSessions}
                    />
                ))}
            </div>
        </section>
    );
}

export default SubjectList;
