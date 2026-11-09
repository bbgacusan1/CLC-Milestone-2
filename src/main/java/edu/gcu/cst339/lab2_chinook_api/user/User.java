package edu.gcu.cst339.lab2_chinook_api.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing an application user stored in the {@code users} table.
 * The password is stored as plain text in Milestone 4 only; Milestone 5
 * replaces this with BCrypt hashing.
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column (name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column (name = "password", nullable = false, length = 100) 
    private String password;

    @Enumerated(EnumType.STRING)
    @Column (name = "role", nullable = false, length = 20)
    private Role role = Role.USER;

    @Column (name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column (name = "password_change_required", nullable = false)
    private boolean passwordChangeRequired = false;

    /** Required by JPA. */
    protected User() {}

    /**
     * Creates a user with the default {@link Role#USER} role, enabled, with no
     * password change required.
     *
     * @param username the unique username
     * @param password the password (plain text until Milestone 5)
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** @return the generated identifier, or null if not yet saved */
    public Long getId() { return id; }

    /** @return the unique username */
    public String getUsername() { return username; }

    /** @param username the new username */
    public void setUsername(String username) { this.username = username; }

    /** @return the stored password */
    public String getPassword() { return password; }

    /** @param password the new password */
    public void setPassword(String password) { this.password = password; }

    /** @return the user's role */
    public Role getRole() { return role; }

    /** @param role the new role */
    public void setRole(Role role) { this.role = role; }

    /** @return true if the account is enabled */
    public boolean isEnabled() { return enabled; }

    /** @param enabled whether the account is enabled */
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    /** @return true if the user must change the password */
    public boolean isPasswordChangeRequired() { return passwordChangeRequired; }

    /** @param passwordChangeRequired whether a password change is required */
    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }
}
