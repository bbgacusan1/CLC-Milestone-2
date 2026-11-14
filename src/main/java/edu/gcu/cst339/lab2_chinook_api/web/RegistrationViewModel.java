package edu.gcu.cst339.lab2_chinook_api.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form data for the registration page. The browser form binds to this
 * ViewModel instead of the {@code User} entity, so a request cannot set
 * fields such as role or enabled status.
 */
public class RegistrationViewModel {

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must be 50 characters or fewer")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 100, message = "Password must be 100 characters or fewer")
    private String password;

    @NotBlank(message = "Please confirm the password")
    private String confirmPassword;

    /** @return the requested username */
    public String getUsername() {
        return username;
    }

    /** @param username the requested username */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the submitted password */
    public String getPassword() {
        return password;
    }

    /** @param password the submitted password */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return the password confirmation */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /** @param confirmPassword the password confirmation */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
