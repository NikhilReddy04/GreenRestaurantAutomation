package org.epam.devbistro.model;

public class SignInPayload {
    private String email;
    private String password;

    public SignInPayload(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignInPayload{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
