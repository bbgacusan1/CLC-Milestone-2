package edu.gcu.cst339.lab2_chinook_api.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link User} records.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by exact username.
     * 
     * @param username the username to look up
     * @return the matching user, or empty if none exists
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a username is already taken
     * 
     * @param username the username to check
     * @return true if a user with taht username exists
     */
    boolean existsByUsername(String username);

    /**
     * Counts users holding a given role with a given enabled sttatus.
     * The serbice uses this to protect the last enabled administrator.
     * 
     * @param role the role to match
     * @param enabled the enabled status to match
     * @return the number of matching users
     */
    long countByRoleAndEnabled(Role role, boolean enabled);
}
