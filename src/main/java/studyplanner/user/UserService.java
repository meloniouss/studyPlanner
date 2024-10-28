package studyplanner.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public User findOrCreateUser(OAuth2User oauthUser) {
        try {
            String email = oauthUser.getAttribute("email");
            System.out.println("finding or creating user");
            System.out.println("User found: " + userRepository.findByEmail(email)); // Log presence and value

            return userRepository.findByEmail(email)
                    .orElseGet(() -> createUser(oauthUser));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User createUser(OAuth2User oauthUser) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(oauthUser.getAttribute("email"));
            if(userOptional.isPresent()){
                throw new IllegalStateException("E-mail taken");
            }
            System.out.println("Creating user");
            User user = new User(oauthUser.getAttribute("email"), oauthUser.getAttribute("name"));
            user.setEmail(oauthUser.getAttribute("email"));
            user.setName(oauthUser.getAttribute("name"));
            System.out.println(user);
            return userRepository.save(user); // we are saving our new user here.
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }
    protected void deleteUser(Long id){
        boolean exists = userRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("Student with id" + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }
}
