package edu.gcu.cst339.lab2_chinook_api.web;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import edu.gcu.cst339.lab2_chinook_api.user.User;
import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException;
import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException.Reason;
import edu.gcu.cst339.lab2_chinook_api.user.UserService;

/** Unit tests for {@link AdminUserController}. */
class AdminUserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final AdminUserController controller = new AdminUserController(userService);

    @Test
    void listShowsPasswordFreeRows() {
        when(userService.findAll()).thenReturn(List.of(new User("alice", "secret")));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.listUsers(model);

        assertEquals("admin-users", view);
        @SuppressWarnings("unchecked")
        List<UserRowViewModel> rows = (List<UserRowViewModel>) model.getAttribute("users");
        assertEquals(1, rows.size());
        assertEquals("alice", rows.get(0).getUsername());
        assertThrows(NoSuchMethodException.class,
                () -> UserRowViewModel.class.getMethod("getPassword"));
    }

    @Test
    void roleChangeCallsServiceAndFlashesSuccess() {
        UserAdminViewModel form = new UserAdminViewModel();
        form.setRole("ADMIN");
        RedirectAttributesModelMap flash = new RedirectAttributesModelMap();

        String view = controller.changeRole(5L, form, flash);

        assertEquals("redirect:/admin/users", view);
        verify(userService).updateRole(5L, "ADMIN");
        assertTrue(flash.getFlashAttributes().containsKey("successMessage"));
    }

    @Test
    void invalidRoleIsReportedNotSaved() {
        doThrow(new UserRuleException(Reason.INVALID_ROLE, "Role must be USER or ADMIN"))
                .when(userService).updateRole(any(), any());
        UserAdminViewModel form = new UserAdminViewModel();
        form.setRole("SUPERUSER");
        RedirectAttributesModelMap flash = new RedirectAttributesModelMap();

        controller.changeRole(5L, form, flash);

        assertEquals("Role must be USER or ADMIN", flash.getFlashAttributes().get("errorMessage"));
        assertFalse(flash.getFlashAttributes().containsKey("successMessage"));
    }

    @Test
    void lastAdministratorCannotBeDisabled() {
        doThrow(new UserRuleException(Reason.LAST_ADMINISTRATOR,
                "The only enabled administrator cannot be disabled or demoted"))
                .when(userService).setEnabled(any(), org.mockito.ArgumentMatchers.anyBoolean());
        UserAdminViewModel form = new UserAdminViewModel();
        form.setEnabled(false);
        RedirectAttributesModelMap flash = new RedirectAttributesModelMap();

        controller.changeEnabled(1L, form, flash);

        assertTrue(flash.getFlashAttributes().containsKey("errorMessage"));
    }

    @Test
    void missingEnabledValueIsRejectedWithoutCallingService() {
        RedirectAttributesModelMap flash = new RedirectAttributesModelMap();

        controller.changeEnabled(1L, new UserAdminViewModel(), flash);

        verifyNoInteractions(userService);
        assertTrue(flash.getFlashAttributes().containsKey("errorMessage"));
    }

    @Test
    void passwordChangeRequirementCallsService() {
        UserAdminViewModel form = new UserAdminViewModel();
        form.setPasswordChangeRequired(true);
    

        controller.changePasswordRequirement(3L, form, new RedirectAttributesModelMap());

        verify(userService).setPasswordChangeRequired(3L, true);
    }
}
