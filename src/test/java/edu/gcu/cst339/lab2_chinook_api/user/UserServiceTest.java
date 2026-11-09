package edu.gcu.cst339.lab2_chinook_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException.Reason;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    private User userWithRole(Role role, boolean enabled) {
        User user = new User("someone", "pw");
        user.setRole(role);
        user.setEnabled(enabled);
        return user;
    }

    private Reason reasonOf(Runnable action) {
        return assertThrows(UserRuleException.class, action::run).getReason();
    }

    // 1. Duplicate-username rejection
    @Test
    void registerRejectsDuplicateUsername() {
        when(userRepository.existsByUsername("sam")).thenReturn(true);

        assertEquals(Reason.DUPLICATE_USERNAME,
                reasonOf(() -> userService.register("sam", "pw1", "pw1")));
        verify(userRepository, never()).save(any());
    }

    // 2. Password-confirmation validation
    @Test
    void registerRejectsMismatchedPasswords() {
        assertEquals(Reason.PASSWORD_MISMATCH,
                reasonOf(() -> userService.register("sam", "pw1", "pw2")));
        verify(userRepository, never()).save(any());
    }

    // 3. Least-privileged registration
    @Test
    void registerAlwaysCreatesEnabledUserRole() {
        when(userRepository.existsByUsername("sam")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.register("sam", "pw1", "pw1");

        assertEquals(Role.USER, saved.getRole());
        assertTrue(saved.isEnabled());
        assertFalse(saved.isPasswordChangeRequired());
    }

    // 4. Valid and invalid credential checking
    @Test
    void checkCredentialsAcceptsValidCredentials() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(new User("sam", "pw1")));

        assertTrue(userService.checkCredentials("sam", "pw1").isPresent());
    }

    @Test
    void checkCredentialsRejectsWrongPasswordAndUnknownUser() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(new User("sam", "pw1")));
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertTrue(userService.checkCredentials("sam", "wrong").isEmpty());
        assertTrue(userService.checkCredentials("ghost", "pw1").isEmpty());
    }

    // 5. Disabled-user rejection
    @Test
    void checkCredentialsRejectsDisabledUser() {
        User disabled = new User("sam", "pw1");
        disabled.setEnabled(false);
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(disabled));

        assertTrue(userService.checkCredentials("sam", "pw1").isEmpty());
    }

    // 6. Administrator bootstrap behavior
    @Test
    void bootstrapAdminCreatesAdminRequiringPasswordChange() {
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(userRepository.countByRoleAndEnabled(Role.ADMIN, true)).thenReturn(0L);

        assertTrue(userService.bootstrapAdmin("admin", "temp"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(Role.ADMIN, captor.getValue().getRole());
        assertTrue(captor.getValue().isPasswordChangeRequired());
    }

    @Test
    void bootstrapAdminSkipsWhenEnabledAdminExists() {
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(userRepository.countByRoleAndEnabled(Role.ADMIN, true)).thenReturn(1L);

        assertFalse(userService.bootstrapAdmin("admin", "temp"));
        verify(userRepository, never()).save(any());
    }

    // 7. Password replacement
    @Test
    void changePasswordReplacesPasswordAndClearsFlag() {
        User user = new User("sam", "old");
        user.setPasswordChangeRequired(true);
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(user));

        userService.changePassword("sam", "old", "new", "new");

        assertEquals("new", user.getPassword());
        assertFalse(user.isPasswordChangeRequired());
        verify(userRepository).save(user);
    }

    @Test
    void changePasswordRejectsWrongCurrentPassword() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(new User("sam", "old")));

        assertEquals(Reason.INVALID_CURRENT_PASSWORD,
                reasonOf(() -> userService.changePassword("sam", "bad", "new", "new")));
    }

    @Test
    void changePasswordRejectsMismatchedConfirmation() {
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(new User("sam", "old")));

        assertEquals(Reason.PASSWORD_MISMATCH,
                reasonOf(() -> userService.changePassword("sam", "old", "new", "other")));
    }

    // 8. Allowed-role validation
    @Test
    void updateRoleRejectsInvalidRole() {
        assertEquals(Reason.INVALID_ROLE,
                reasonOf(() -> userService.updateRole(1L, "SUPERUSER")));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateRoleAcceptsAllowedRole() {
        User user = userWithRole(Role.USER, true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateRole(1L, "admin");

        assertEquals(Role.ADMIN, user.getRole());
    }

    // 9. Protection against disabling or demoting the only administrator
    @Test
    void cannotDisableOnlyEnabledAdministrator() {
        User admin = userWithRole(Role.ADMIN, true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.countByRoleAndEnabled(Role.ADMIN, true)).thenReturn(1L);

        assertEquals(Reason.LAST_ADMINISTRATOR,
                reasonOf(() -> userService.setEnabled(1L, false)));
        assertTrue(admin.isEnabled());
    }

    @Test
    void cannotDemoteOnlyEnabledAdministrator() {
        User admin = userWithRole(Role.ADMIN, true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.countByRoleAndEnabled(Role.ADMIN, true)).thenReturn(1L);

        assertEquals(Reason.LAST_ADMINISTRATOR,
                reasonOf(() -> userService.updateRole(1L, "USER")));
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    void canDisableAdministratorWhenAnotherEnabledAdminExists() {
        User admin = userWithRole(Role.ADMIN, true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.countByRoleAndEnabled(Role.ADMIN, true)).thenReturn(2L);

        userService.setEnabled(1L, false);

        assertFalse(admin.isEnabled());
    }
}