package no.hiof.studytracker.DTOs;

import no.hiof.studytracker.model.Session;

import java.util.List;

public class RecentStudySessionsDTO {
    private List<SessionResponseDTO> sessions;

    public RecentStudySessionsDTO() {}

    public List<SessionResponseDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionResponseDTO> sessions) {
        this.sessions = sessions;
    }
}
