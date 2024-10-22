package UserJv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardClass {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, OAuth2AuthenticationToken authentication) {
        OAuth2User oauthUser = authentication.getPrincipal();

        // Assuming userService.findOrCreateUser returns a User object
        User user = userService.findOrCreateUser(oauthUser); //careful, this is a custom user, not an oauth user

        if (user != null) {
            model.addAttribute("name", user.getName()); // Assuming getName() returns the user's name
            model.addAttribute("email", user.getEmail());
        } else {
            // Handle user not found scenario (optional)
            model.addAttribute("error", "User not found.");
        }

        return "dashboard"; // Ensure you have a dashboard.html in the templates directory
    }
}
