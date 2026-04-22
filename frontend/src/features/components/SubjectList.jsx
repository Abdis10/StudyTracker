import SubjectCard from "./SubjectCard";
import { groupSessionsBySubject } from "../utils/groupSessionsBySubject";

function SubjectList({ subjects, sessions }) {
    const grouped = groupSessionsBySubject(sessions);

    return (
        <div className="subject-list">
            {subjects.map((subject) => (
                <SubjectCard
                    key={subject.id}
                    subject={subject}
                    sessions={grouped.get(subject.id) || []}
                />
            ))}
        </div>
    );
}

export default SubjectList;