package no.hiof.studytracker.DTOs;

public class SignupDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String gender;

    public SignupDTO() {}

    public SignupDTO(String firstname, String lastname, String username, String email, String password, String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public SignupDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
