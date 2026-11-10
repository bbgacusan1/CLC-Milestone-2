package edu.gcu.cst339.lab2_chinook_api.user;

/**
 * Thrown when a user-management business rule is violated. Controllers
 * inspect {@link #getReason()} to decide which form error to display.
 */
public class UserRuleException extends RuntimeException {
    /** The specific rule that was violated. */
    public enum Reason {
       /** A required value was blank or missing. */
        INVALID_INPUT,
        /** The username is already registered. */
        DUPLICATE_USERNAME,
        /** Password and confirmation do not match. */
        PASSWORD_MISMATCH,
        /** The supplied current password is wrong. */
        INVALID_CURRENT_PASSWORD,
        /** The role value is not USER or ADMIN. */
        INVALID_ROLE,
        /** No user exists with the given identifier or username. */
        USER_NOT_FOUND,
        /** The change would remove the only enabled administrator. */
        LAST_ADMINISTRATOR
    }

    private final Reason reason; 

    /**
     * Creates the exception.
     *
     * @param reason the violated rule
     * @param message a human-readable explanation
     */
    public UserRuleException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    /** @return the violated rule */
    public Reason getReason() {
        return reason;
    }
}
