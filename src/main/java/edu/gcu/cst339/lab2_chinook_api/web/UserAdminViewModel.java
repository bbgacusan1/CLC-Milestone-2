package edu.gcu.cst339.lab2_chinook_api.web;

/**
 * Form data for the administration actions. Each form submits only the field
 * it changes; the others stay null. Boolean wrappers are used so a missing
 * value can be told apart from "false".
 */
public class UserAdminViewModel {

    private String role;
    private Boolean enabled;
    private Boolean passwordChangeRequired;

    /** Creates an empty command for Spring to bind. */
    public UserAdminViewModel() {
    }

    /** @return the requested role name, or null */
    public String getRole() { return role; }

    /** @param role the requested role name */
    public void setRole(String role) { this.role = role; }

    /** @return the requested enabled status, or null */
    public Boolean getEnabled() { return enabled; }

    /** @param enabled the requested enabled status */
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    /** @return the requested password-change flag, or null */
    public Boolean getPasswordChangeRequired() { return passwordChangeRequired; }

    /** @param passwordChangeRequired the requested flag value */
    public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }
}
