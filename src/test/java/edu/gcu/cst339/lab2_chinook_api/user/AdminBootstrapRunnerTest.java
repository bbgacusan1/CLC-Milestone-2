package edu.gcu.cst339.lab2_chinook_api.user;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/** Unit tests for {@link AdminBootstrapRunner}. */
class AdminBootstrapRunnerTest {

    private final UserService userService = mock(UserService.class);

    @Test
    void createsAdminWhenPasswordIsConfigured() {
        new AdminBootstrapRunner(userService, "admin", "some-temp-pass").run(null);

        verify(userService).bootstrapAdmin("admin", "some-temp-pass");
    }

    @Test
    void skipsBootstrapWhenPasswordIsBlank() {
        new AdminBootstrapRunner(userService, "admin", "").run(null);

        verifyNoInteractions(userService);
    }
}