package edu.gcu.cst339.lab2_chinook_api.web;

import jakarta.validation.constraints.NotBlank;

/**
 * Form data for the login page. The browser form binds to this ViewModel
 * instead of the {@code User} entity. It supports a manual credential check
 * only; this is not secure authentication until Spring Security is added.
 */
public class LoginViewModel {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Creates an empty form. Spring calls this before binding the submitted
     * request parameters, and the GET handler uses it to render a blank form.
     */
    public LoginViewModel() {
    }

    /**
     * Returns the username entered on the form.
     *
     * @return the submitted username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username entered on the form.
     *
     * @param username the submitted username
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
}
