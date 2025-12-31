package no.hiof.studytracker.repository;

import no.hiof.studytracker.model.User;

import java.util.ArrayList;

public interface UserRepository {
    void saveUser(User user);
}
