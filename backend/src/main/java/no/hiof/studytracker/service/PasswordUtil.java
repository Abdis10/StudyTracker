package no.hiof.studytracker.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPw(String pw) {
        String hashPw = BCrypt.hashpw(pw, BCrypt.gensalt(12));
        return hashPw;
    }
}
