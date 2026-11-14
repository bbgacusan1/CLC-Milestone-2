package edu.gcu.cst339.lab2_chinook_api.web;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.gcu.cst339.lab2_chinook_api.user.User;
import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException;
import edu.gcu.cst339.lab2_chinook_api.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Spring MVC controller for browser registration and the Milestone 4 manual
 * credential check. All user rules live in {@link UserService}; this class
 * only handles form flow. The login here is not secure authentication:
 * nothing stops a visitor from typing a protected URL directly.
 */
@Controller
public class UserController {

    /** Session attribute holding the username after a successful credential check. */
    public static final String SESSION_USERNAME = "username";

    /** The one message shown for every failed login, so it never reveals which value was wrong. */
    static final String LOGIN_ERROR = "Invalid username or password";

    private final UserService userService;

    /**
     * Creates the controller.
     *
     * @param userService the user-management service
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Shows an empty registration form.
     *
     * @param model the Spring MVC model
     * @return the registration template
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("register", new RegistrationViewModel());
        return "register";
    }

    /**
     * Registers a new USER account, then redirects to the login page
     * (POST/Redirect/GET). Validation and rule errors redisplay the form.
     *
     * @param form the submitted registration data
     * @param bindingResult validation results for the form
     * @param redirectAttributes flash attributes for the redirect
     * @return the registration template on error, otherwise a redirect to login
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("register") RegistrationViewModel form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.register(form.getUsername(), form.getPassword(), form.getConfirmPassword());
        } catch (UserRuleException e) {
            switch (e.getReason()) {
                case DUPLICATE_USERNAME -> bindingResult.rejectValue("username", "duplicate", e.getMessage());
                case PASSWORD_MISMATCH -> bindingResult.rejectValue("confirmPassword", "mismatch", e.getMessage());
                default -> bindingResult.reject("registration.invalid", e.getMessage());
            }
            return "register";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please log in.");
        return "redirect:/login";
    }

    /**
     * Shows an empty login form.
     *
     * @param model the Spring MVC model
     * @return the login template
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("login", new LoginViewModel());
        return "login";
    }

    /**
     * Checks the submitted credentials through the user service. Unknown
     * users, wrong passwords, and disabled users all get the same error.
     * Users who must change their password are sent to the password-change page.
     *
     * @param form the submitted login data
     * @param bindingResult validation results for the form
     * @param session the HTTP session, used to remember the username for the password-change page
     * @param redirectAttributes flash attributes for the redirect
     * @return the login template on failure, otherwise a redirect
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") LoginViewModel form,
                        BindingResult bindingResult,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        Optional<User> user = userService.checkCredentials(form.getUsername(), form.getPassword());
        if (user.isEmpty()) {
            bindingResult.reject("login.invalid", LOGIN_ERROR);
            return "login";
        }
        session.setAttribute(SESSION_USERNAME, user.get().getUsername());
        if (user.get().isPasswordChangeRequired()) {
            return "redirect:/change-password";
        }
        redirectAttributes.addFlashAttribute("username", user.get().getUsername());
        return "redirect:/login/success";
    }

    /**
     * Shows the result of a successful credential check.
     *
     * @return the login-success template
     */
    @GetMapping("/login/success")
    public String loginSuccess() {
        return "login-success";
    }
}
