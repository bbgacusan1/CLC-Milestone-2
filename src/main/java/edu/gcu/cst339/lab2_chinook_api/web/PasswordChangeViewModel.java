package edu.gcu.cst339.lab2_chinook_api.web;

import jakarta.validation.constraints.NotBlank;

/**
 * Form data for the password-change page. Binds the browser form instead of
 * the {@code User} entity.
 */
public class PasswordChangeViewModel {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Please confirm the new password")
    private String confirmPassword;

    /** Creates an empty form for Spring to bind. */
    public PasswordChangeViewModel() {
    }

    /** @return the current password entered */
    public String getCurrentPassword() { return currentPassword; }

    /** @param currentPassword the current password entered */
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    /** @return the new password entered */
    public String getNewPassword() { return newPassword; }

    /** @param newPassword the new password entered */
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    /** @return the confirmation entered */
    public String getConfirmPassword() { return confirmPassword; }

    /** @param confirmPassword the confirmation entered */
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
