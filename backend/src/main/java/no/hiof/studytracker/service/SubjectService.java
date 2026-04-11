package no.hiof.studytracker.service;

import no.hiof.studytracker.model.Subject;
import no.hiof.studytracker.repository.UserDataRepository;

import java.util.List;

public class SubjectService {
    private UserDataRepository userDataRepository;
    public SubjectService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public Subject createSubject(String name, int userId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Subject name cannot be empty");
        }

        return userDataRepository.insertSubject(name, userId);
    }

    public List<Subject> getSubjects(int userId) {
        return userDataRepository.getSubjectsByUser(userId);
    }
}