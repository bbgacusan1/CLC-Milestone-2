package edu.gcu.cst339.lab2_chinook_api.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.gcu.cst339.lab2_chinook_api.user.UserRuleException;
import edu.gcu.cst339.lab2_chinook_api.user.UserService;

/**
 * Spring MVC controller for the Milestone 4 user-administration page. All
 * rules live in {@link UserService}. This page is NOT securely protected:
 * anyone who types the URL can reach it until Spring Security arrives in
 * Milestone 5.
 */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private static final String REDIRECT = "redirect:/admin/users";

    private final UserService userService;

    /**
     * Creates the controller.
     *
     * @param userService the user-management service
     */
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lists all users. Entities are converted to password-free rows before
     * they reach the template.
     *
     * @param model the Spring MVC model
     * @return the administration template
     */
    @GetMapping
    public String listUsers(Model model) {
        List<UserRowViewModel> rows = userService.findAll().stream()
                .map(UserRowViewModel::from)
                .toList();
        model.addAttribute("users", rows);
        return "admin-users";
    }

    /**
     * Changes a user's role.
     *
     * @param id the user identifier
     * @param form the submitted role
     * @param redirectAttributes flash attributes for the redirect
     * @return a redirect back to the list
     */
    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable("id") Long id,
                             @ModelAttribute UserAdminViewModel form,
                             RedirectAttributes redirectAttributes) {
        return apply(redirectAttributes, "Role updated",
                () -> userService.updateRole(id, form.getRole()));
    }

    /**
     * Enables or disables a user.
     *
     * @param id the user identifier
     * @param form the submitted enabled status
     * @param redirectAttributes flash attributes for the redirect
     * @return a redirect back to the list
     */
    @PostMapping("/{id}/enabled")
    public String changeEnabled(@PathVariable("id") Long id,
                                @ModelAttribute UserAdminViewModel form,
                                RedirectAttributes redirectAttributes) {
        if (form.getEnabled() == null) {
            return missing(redirectAttributes);
        }
        boolean enabled = form.getEnabled();
        return apply(redirectAttributes, "Account status updated",
                () -> userService.setEnabled(id, enabled));
    }

    /**
     * Requires or clears a password change for a user.
     *
     * @param id the user identifier
     * @param form the submitted flag
     * @param redirectAttributes flash attributes for the redirect
     * @return a redirect back to the list
     */
    @PostMapping("/{id}/password-change")
    public String changePasswordRequirement(@PathVariable("id") Long id,
                                            @ModelAttribute UserAdminViewModel form,
                                            RedirectAttributes redirectAttributes) {
        if (form.getPasswordChangeRequired() == null) {
            return missing(redirectAttributes);
        }
        boolean required = form.getPasswordChangeRequired();
        return apply(redirectAttributes, "Password-change requirement updated",
                () -> userService.setPasswordChangeRequired(id, required));
    }

    /** Runs a service action, flashing a success or rule-violation message. */
    private String apply(RedirectAttributes redirectAttributes, String successMessage, Runnable action) {
        try {
            action.run();
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (UserRuleException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return REDIRECT;
    }

    private String missing(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Missing form value");
        return REDIRECT;
    }
}