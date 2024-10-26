package studyplanner.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // endpoint to handle OAuth2 user login
    @PostMapping("/oauth2/callback") //likely unnecessary?
    public ResponseEntity<User> handleOAuth2Callback(@RequestBody OAuth2User oauthUser) {
        User user = userService.findOrCreateUser(oauthUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //must add UPDATE endpoint as well

    // endpoint to delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}
