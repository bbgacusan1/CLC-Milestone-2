package edu.gcu.cst339.lab2_chinook_api.web;

import edu.gcu.cst339.lab2_chinook_api.user.User;

/**
 * One row of the user-administration table. It deliberately has no password
 * field, so a password can never be rendered into the page.
 */
public class UserRowViewModel {

    private final Long id;
    private final String username;
    private final String role;
    private final boolean enabled;
    private final boolean passwordChangeRequired;

    /**
     * Creates a row.
     *
     * @param id the user identifier
     * @param username the username
     * @param role the role name
     * @param enabled whether the account is enabled
     * @param passwordChangeRequired whether a password change is required
     */
    public UserRowViewModel(Long id, String username, String role,
                            boolean enabled, boolean passwordChangeRequired) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.enabled = enabled;
        this.passwordChangeRequired = passwordChangeRequired;
    }

    /**
     * Copies the displayable fields of a user, leaving the password behind.
     *
     * @param user the user to convert
     * @return the row for that user
     */
    public static UserRowViewModel from(User user) {
        return new UserRowViewModel(user.getId(), user.getUsername(),
                user.getRole().name(), user.isEnabled(), user.isPasswordChangeRequired());
    }

    /** @return the user identifier */
    public Long getId() { return id; }

    /** @return the username */
    public String getUsername() { return username; }

    /** @return the role name */
    public String getRole() { return role; }

    /** @return true if the account is enabled */
    public boolean isEnabled() { return enabled; }

    /** @return true if a password change is required */
    public boolean isPasswordChangeRequired() { return passwordChangeRequired; }
}