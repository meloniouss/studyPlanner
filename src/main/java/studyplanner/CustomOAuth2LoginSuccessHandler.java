package studyplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import studyplanner.user.User;
import studyplanner.user.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

        String token = generateToken(currentUser.getName(), currentUser.getEmail(), currentUser.getId());
        Cookie cookie = new Cookie("sessionToken", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);  // 1 day
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
        System.out.println(response);
        response.sendRedirect(System.getenv("FRONT-END-URL"));
    }

    public String generateToken(String userId, String userEmail, Long userIdNumber) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("email", userEmail)
                .withClaim("userId", userIdNumber)
                .withIssuer("Taskly")
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

}
