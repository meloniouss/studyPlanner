package UserJv;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findOrCreateUser(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        return userRepository.findByEmail(email)
                .orElseGet(() -> createUser(oauthUser));
    }

    private User createUser(OAuth2User oauthUser) {
        System.out.println("Creating user");
        User user = new User();
        user.setEmail(oauthUser.getAttribute("email"));
        user.setName(oauthUser.getAttribute("name"));
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        return userRepository.save(user);
    }
}
