package no.hiof.studytracker.DTOs;

import no.hiof.studytracker.model.Session;

public class RecentStudySessionsDTO {
    private Session session;

    public RecentStudySessionsDTO() {}

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
