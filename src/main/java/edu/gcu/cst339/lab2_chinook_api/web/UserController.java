package edu.gcu.cst339.lab2_chinook_api.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException;
import edu.gcu.cst339.lab2_chinook_api.user.UserService;
import jakarta.validation.Valid;

/**
 * Spring MVC controller for browser registration. All user rules live in
 * {@link UserService}; this class only handles form flow.
 */
@Controller
public class UserController {

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
}
