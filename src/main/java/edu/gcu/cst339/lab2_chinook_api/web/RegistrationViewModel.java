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

    /**
     * Creates an empty form. Spring calls this before binding the submitted
     * request parameters, and the GET handler uses it to render a blank form.
     */
    public RegistrationViewModel() {
    }

    /**
     * Returns the username entered on the form.
     *
     * @return the requested username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username entered on the form.
     *
     * @param username the requested username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password entered on the form.
     *
     * @return the submitted password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password entered on the form.
     *
     * @param password the submitted password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password confirmation entered on the form.
     *
     * @return the password confirmation
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the password confirmation entered on the form.
     *
     * @param confirmPassword the password confirmation
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
