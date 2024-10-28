package studyplanner;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import studyplanner.CustomOAuth2LoginSuccessHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    public SecurityConfig(CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler) {
        this.customOAuth2LoginSuccessHandler = customOAuth2LoginSuccessHandler;
    }

    @PostConstruct
    public void init() {
        System.out.println("Security configuration initialized.");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/otherpage", "/logout", "/login", "/oauth2/**").permitAll() // Allow public access to login and OAuth2 endpoints MAYBE DOUBLE CHECK IF THIS IS NECESSARY?
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(customOAuth2LoginSuccessHandler); // Your success handler
                })
                .logout(logout -> logout
                        .logoutSuccessHandler(new LogoutSuccessHandler() {
                            @Override
                            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.sendRedirect("http://localhost:3000/");
                                System.out.println(response);
                                System.out.println("logout done");
                            }
                        }
                ))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

