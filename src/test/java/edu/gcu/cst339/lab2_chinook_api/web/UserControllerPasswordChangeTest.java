package edu.gcu.cst339.lab2_chinook_api.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException;
import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException.Reason;
import edu.gcu.cst339.lab2_chinook_api.user.UserService;

/** Unit tests for the change-password handlers in {@link UserController}. */
class UserControllerPasswordChangeTest {

    private final UserService userService = mock(UserService.class);
    private final UserController controller = new UserController(userService);

    private PasswordChangeViewModel form() {
        PasswordChangeViewModel form = new PasswordChangeViewModel();
        form.setCurrentPassword("old");
        form.setNewPassword("new");
        form.setConfirmPassword("new");
        return form;
    }

    private MockHttpSession loggedInSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(UserController.SESSION_USERNAME, "admin");
        return session;
    }

    @Test
    void getRedirectsToLoginWhenNoUsernameInSession() {
        String view = controller.showChangePasswordForm(new MockHttpSession(), new ExtendedModelMap());

        assertEquals("redirect:/login", view);
    }

    @Test
    void getShowsFormWhenUsernameInSession() {
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.showChangePasswordForm(loggedInSession(), model);

        assertEquals("change-password", view);
        assertNotNull(model.getAttribute("passwordChange"));
    }

    @Test
    void successfulChangeRedirectsToLoginAndClearsSession() {
        MockHttpSession session = loggedInSession();
        PasswordChangeViewModel form = form();
        BindingResult result = new BeanPropertyBindingResult(form, "passwordChange");

        String view = controller.changePassword(form, result, session, new RedirectAttributesModelMap());

        assertEquals("redirect:/login", view);
        verify(userService).changePassword("admin", "old", "new", "new");
        assertNull(session.getAttribute(UserController.SESSION_USERNAME));
    }

    @Test
    void wrongCurrentPasswordShowsErrorOnThatField() {
        doThrow(new UserRuleException(Reason.INVALID_CURRENT_PASSWORD, "Current password is incorrect"))
                .when(userService).changePassword(any(), any(), any(), any());
        PasswordChangeViewModel form = form();
        BindingResult result = new BeanPropertyBindingResult(form, "passwordChange");

        String view = controller.changePassword(form, result, loggedInSession(), new RedirectAttributesModelMap());

        assertEquals("change-password", view);
        assertTrue(result.hasFieldErrors("currentPassword"));
    }

    @Test
    void mismatchedConfirmationShowsErrorOnConfirmField() {
        doThrow(new UserRuleException(Reason.PASSWORD_MISMATCH, "Passwords do not match"))
                .when(userService).changePassword(any(), any(), any(), any());
        PasswordChangeViewModel form = form();
        BindingResult result = new BeanPropertyBindingResult(form, "passwordChange");

        String view = controller.changePassword(form, result, loggedInSession(), new RedirectAttributesModelMap());

        assertEquals("change-password", view);
        assertTrue(result.hasFieldErrors("confirmPassword"));
    }
}