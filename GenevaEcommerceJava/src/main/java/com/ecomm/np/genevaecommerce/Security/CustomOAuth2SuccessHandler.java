package com.ecomm.np.genevaecommerce.security;

import com.ecomm.np.genevaecommerce.enumeration.Role;
import com.ecomm.np.genevaecommerce.extra.PasswordGenerator;
import com.ecomm.np.genevaecommerce.model.RoleTable;
import com.ecomm.np.genevaecommerce.model.UserModel;
import com.ecomm.np.genevaecommerce.repository.RoleTableRepository;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    private final JwtUtils jwtService;
    private final CustomUserService customUserService;
    private final UserRepository userRepository;
    private final RoleTableRepository roleTableRepository;

    @Autowired
    public CustomOAuth2SuccessHandler(
            JwtUtils jwtService,
            CustomUserService customUserService,
            UserRepository userRepository,
            RoleTableRepository roleTableRepository
    ) {
        this.jwtService = jwtService;
        this.customUserService = customUserService;
        this.userRepository = userRepository;
        this.roleTableRepository = roleTableRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = extractProvider(request.getRequestURI());
        logger.info("OAuth2 login via: {}", provider);

        String username = (String) attributes.getOrDefault("login", attributes.get("id"));
        String name = (String) attributes.getOrDefault("name", username);
        String email = (String) attributes.get("email");
        String jwt;

        if (username == null || username.isEmpty()) {
            username = "user" + PasswordGenerator.GenerateNums();
        }
        if (name == null || name.isEmpty()) {
            name = username;
        }
        if (email == null || email.isEmpty()) {
            email = username + "@" + provider + "-oauth.local";
        }
        try {
            CustomUser user = (CustomUser) customUserService.loadUserByUsername(email);
            jwt = jwtService.generateJwtTokens(user);
            setJwtCookie(response, jwt);
            logger.info("Existing OAuth2 user logged in: {}", email);
        } catch (UsernameNotFoundException ex) {
            RoleTable roleTable = roleTableRepository.findByRole(Role.USER);
            UserModel userAccount = new UserModel();
            userAccount.setEmail(email);
            userAccount.setUserName(username);
            userAccount.setPassword(PasswordGenerator.generate(14)); // Random password
            userAccount.setRoleTable(roleTable);
            userAccount = userRepository.save(userAccount);

            CustomUser newUser = CustomUser.build(userAccount);
            jwt = jwtService.generateJwtTokens(newUser);
            setJwtCookie(response, jwt);
            logger.info("New OAuth2 user created: {}", email);
        } catch (Exception ex) {
            logger.error("OAuth2 login error: {}", ex.getMessage());
            response.sendRedirect("http://localhost:5500/index.html");
            return;
        }

        response.sendRedirect("http://localhost:5500/login.html?jwt="+jwt);
    }

    private String extractProvider(String uri) {
        if (uri == null) return "unknown";
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }

    private void setJwtCookie(HttpServletResponse response, String jwt) {
        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}