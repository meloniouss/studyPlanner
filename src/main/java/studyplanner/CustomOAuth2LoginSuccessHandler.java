package studyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import studyplanner.user.User;
import studyplanner.user.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class CustomOAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${app.secret-key}")
    String secretKey;
    @Autowired
    private UserService userService;

    public CustomOAuth2LoginSuccessHandler() {
        System.out.println("CustomOAuth2LoginSuccessHandler bean created!");
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User currentUser = userService.findOrCreateUser(oAuth2User);
        System.out.println("Authentication successful! Redirecting to /otherpage.");

        String token = generateToken(currentUser.getName(), currentUser.getEmail());
        Cookie cookie = new Cookie("sessionToken", token);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(true); // Use true if using HTTPS

        // Create user ID cookie
        Cookie userIdCookie = new Cookie("userId", String.valueOf(currentUser.getId()));
        userIdCookie.setPath("/");
        userIdCookie.setHttpOnly(true);
        userIdCookie.setSecure(true);
        userIdCookie.setMaxAge(3600);


        response.addCookie(cookie);
        response.addCookie(userIdCookie);

        response.sendRedirect("http://localhost:3000/");

    }

    public String generateToken(String userId, String userEmail) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("email", userEmail)
                .withIssuer("Taskly")
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // Expires in 1 day
                .sign(Algorithm.HMAC256(secretKey)); // Sign with the secret key
    }

}
