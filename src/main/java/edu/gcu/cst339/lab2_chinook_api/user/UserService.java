package edu.gcu.cst339.lab2_chinook_api.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException.Reason;

/**
 * Business rules for application-user management. MVC controllers call this
 * service and never access {@link UserRepository} directly. Passwords are
 * compared as plain text in Milestone 4; Milestone 5 replaces this with
 * Spring Security and BCrypt.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates the service.
     *
     * @param userRepository the user repository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a self-service account. The role is always USER regardless of
     * what the caller supplies elsewhere.
     *
     * @param username the requested username
     * @param password the password
     * @param confirmPassword the password confirmation
     * @return the saved user
     * @throws UserRuleException for blank input, mismatched passwords, or a duplicate username
     */
    @Transactional
    public User register(String username, String password, String confirmPassword) {
        String name = requireText(username, "Username is required");
        requireText(password, "Password is required");
        if (!password.equals(confirmPassword)) {
            throw new UserRuleException(Reason.PASSWORD_MISMATCH, "Passwords do not match");
        }
        if (userRepository.existsByUsername(name)) {
            throw new UserRuleException(Reason.DUPLICATE_USERNAME, "Username is already taken");
        }
        return userRepository.save(new User(name, password));
    }

    /**
     * Checks a username and password. This is a manual credential lookup, not
     * secure authentication. Unknown users, wrong passwords, and disabled
     * users all return empty so the caller can show one general error.
     *
     * @param username the submitted username
     * @param password the submitted password
     * @return the matching enabled user, or empty if the check fails
     */
    @Transactional(readOnly = true)
    public Optional<User> checkCredentials(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username.trim())
                .filter(User::isEnabled)
                .filter(user -> user.getPassword().equals(password));
    }

    /**
     * Replaces a user's password and clears the password-change-required flag.
     *
     * @param username the user changing the password
     * @param currentPassword the existing password
     * @param newPassword the new password
     * @param confirmPassword the new-password confirmation
     * @throws UserRuleException if the user is unknown, the current password is wrong,
     *         the new password is blank, or the confirmation does not match
     */
    @Transactional
    public void changePassword(String username, String currentPassword,
                               String newPassword, String confirmPassword) {
        User user = userRepository.findByUsername(requireText(username, "Username is required"))
                .orElseThrow(() -> new UserRuleException(Reason.USER_NOT_FOUND, "User not found"));
        if (currentPassword == null || !user.getPassword().equals(currentPassword)) {
            throw new UserRuleException(Reason.INVALID_CURRENT_PASSWORD, "Current password is incorrect");
        }
        requireText(newPassword, "New password is required");
        if (!newPassword.equals(confirmPassword)) {
            throw new UserRuleException(Reason.PASSWORD_MISMATCH, "Passwords do not match");
        }
        user.setPassword(newPassword);
        user.setPasswordChangeRequired(false);
        userRepository.save(user);
    }

    /**
     * Creates the initial administrator if the username is unused and no
     * enabled administrator exists. The new account must change its password.
     *
     * @param username the administrator username
     * @param temporaryPassword the temporary password, supplied from deployment configuration
     * @return true if the administrator was created, false if bootstrap was skipped
     */
    @Transactional
    public boolean bootstrapAdmin(String username, String temporaryPassword) {
        String name = requireText(username, "Username is required");
        requireText(temporaryPassword, "Temporary password is required");
        if (userRepository.existsByUsername(name)
                || userRepository.countByRoleAndEnabled(Role.ADMIN, true) > 0) {
            return false;
        }
        User admin = new User(name, temporaryPassword);
        admin.setRole(Role.ADMIN);
        admin.setPasswordChangeRequired(true);
        userRepository.save(admin);
        return true;
    }

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the user, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return username == null ? Optional.empty() : userRepository.findByUsername(username.trim());
    }

    /**
     * Lists every user for the administration page.
     *
     * @return all users
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Changes a user's role.
     *
     * @param userId the user identifier
     * @param roleName the new role name, which must be USER or ADMIN
     * @throws UserRuleException for an invalid role, unknown user, or demotion of the only enabled administrator
     */
    @Transactional
    public void updateRole(Long userId, String roleName) {
        Role newRole = parseRole(roleName);
        User user = getUser(userId);
        if (newRole != Role.ADMIN) {
            guardLastAdministrator(user);
        }
        user.setRole(newRole);
        userRepository.save(user);
    }

    /**
     * Enables or disables a user.
     *
     * @param userId the user identifier
     * @param enabled the new enabled status
     * @throws UserRuleException for an unknown user or disabling the only enabled administrator
     */
    @Transactional
    public void setEnabled(Long userId, boolean enabled) {
        User user = getUser(userId);
        if (!enabled) {
            guardLastAdministrator(user);
        }
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    /**
     * Sets whether a user must change the password at next login.
     *
     * @param userId the user identifier
     * @param required the new flag value
     * @throws UserRuleException if the user does not exist
     */
    @Transactional
    public void setPasswordChangeRequired(Long userId, boolean required) {
        User user = getUser(userId);
        user.setPasswordChangeRequired(required);
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserRuleException(Reason.USER_NOT_FOUND, "User not found"));
    }

    private Role parseRole(String roleName) {
        try {
            return Role.valueOf(roleName == null ? "" : roleName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserRuleException(Reason.INVALID_ROLE, "Role must be USER or ADMIN");
        }
    }

    private void guardLastAdministrator(User user) {
        if (user.getRole() == Role.ADMIN && user.isEnabled()
                && userRepository.countByRoleAndEnabled(Role.ADMIN, true) <= 1) {
            throw new UserRuleException(Reason.LAST_ADMINISTRATOR,
                    "The only enabled administrator cannot be disabled or demoted");
        }
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new UserRuleException(Reason.INVALID_INPUT, message);
        }
        return value.trim();
    }
}