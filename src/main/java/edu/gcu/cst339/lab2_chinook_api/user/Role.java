package edu.gcu.cst339.lab2_chinook_api.user;

/**
 * Roles an application user can hold. Roles are stored as data in
 * Milestone 4 and are not enforced as authorities until Spring Security
 * is added in Milestone 5.
 */
public enum Role {
    /** Standard user; the default for self-registered accounts. */
    USER,
    /** Administrator who can manage other users. */
    ADMIN
}
