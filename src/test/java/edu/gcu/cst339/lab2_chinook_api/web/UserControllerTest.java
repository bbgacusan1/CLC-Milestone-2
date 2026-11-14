package edu.gcu.cst339.lab2_chinook_api.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException;
import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException.Reason;
import edu.gcu.cst339.lab2_chinook_api.user.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    // ---------- Story 3: registration ----------

    @Test
    void registrationPageShowsForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(content().string(containsString("confirmPassword")));
    }

    @Test
    void validRegistrationRedirectsToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "reg-test-user")
                        .param("password", "secret")
                        .param("confirmPassword", "secret"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).register("reg-test-user", "secret", "secret");
    }

    @Test
    void tamperedRoleParameterIsNotPassedToService() throws Exception {
        // The ViewModel has no role field, so an extra "role" parameter is ignored
        // and the service's three-argument register() always creates a USER.
        mockMvc.perform(post("/register")
                        .param("username", "reg-test-user")
                        .param("password", "secret")
                        .param("confirmPassword", "secret")
                        .param("role", "ADMIN"))
                .andExpect(redirectedUrl("/login"));

        verify(userService).register("reg-test-user", "secret", "secret");
    }

    @Test
    void mismatchedPasswordsShowMessageWithoutRedisplayingPasswords() throws Exception {
        when(userService.register("reg-test-user", "secret1", "different1"))
                .thenThrow(new UserRuleException(Reason.PASSWORD_MISMATCH, "Passwords do not match"));

        mockMvc.perform(post("/register")
                        .param("username", "reg-test-user")
                        .param("password", "secret1")
                        .param("confirmPassword", "different1"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(content().string(containsString("Passwords do not match")))
                .andExpect(content().string(not(containsString("secret1"))))
                .andExpect(content().string(not(containsString("different1"))));
    }

    @Test
    void duplicateUsernameShowsMessage() throws Exception {
        when(userService.register("taken", "secret", "secret"))
                .thenThrow(new UserRuleException(Reason.DUPLICATE_USERNAME, "Username is already taken"));

        mockMvc.perform(post("/register")
                        .param("username", "taken")
                        .param("password", "secret")
                        .param("confirmPassword", "secret"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Username is already taken")));
    }

    @Test
    void blankFieldsRedisplayFormWithoutCallingService() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("password", "")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(content().string(containsString("Username is required")));

        verify(userService, never()).register(anyString(), anyString(), anyString());
    }
}
