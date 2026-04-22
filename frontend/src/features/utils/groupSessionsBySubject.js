export function groupSessionsBySubject(sessions) {
    const map = new Map();

    sessions.forEach((session) => {
        if (!session.subjectId) return;

        if (!map.has(session.subjectId)) {
            map.set(session.subjectId, []);
        }

        map.get(session.subjectId).push(session);
    });

    return map;
}