package edu.gcu.cst339.lab2_chinook_api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Creates the initial administrator when the application starts. The
 * temporary password comes from deployment configuration, never from source
 * code. If no password is configured, or an administrator already exists,
 * startup continues without creating anything.
 */
@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final UserService userService;
    private final String adminUsername;
    private final String adminPassword;

    /**
     * Creates the runner.
     *
     * @param userService the user-management service
     * @param adminUsername the administrator username, defaulting to "admin"
     * @param adminPassword the temporary password from the environment, empty if unset
     */
    public AdminBootstrapRunner(UserService userService,
            @Value("${app.bootstrap.admin-username:admin}") String adminUsername,
            @Value("${app.bootstrap.admin-password:}") String adminPassword) {
        this.userService = userService;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    /**
     * Runs once after the application context starts. Skips quietly when no
     * password is configured. The password is never logged.
     *
     * @param args the application arguments (unused)
     */
    @Override
    public void run(ApplicationArguments args) {
        if (adminPassword.isBlank()) {
            log.warn("No bootstrap admin password configured; skipping bootstrap");
            return;
        }
        boolean created = userService.bootstrapAdmin(adminUsername, adminPassword);
        log.info(created ? "Bootstrap administrator created"
                         : "Bootstrap skipped: an administrator already exists");
    }
}