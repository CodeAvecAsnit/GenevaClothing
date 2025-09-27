package com.ecomm.np.genevaecommerce.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);


    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("The Jwt filter has been triggered "+request.getRequestURI());

        try {
            String jwt = parseJWT(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromToken(jwt);
                String role = jwtUtils.getUserRoleFromToken(jwt);
                int id = jwtUtils.getUserIdFromToken(jwt);
                logger.info(id+" . Authenticated user: " + username + " with role: " + role);
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                CustomUser customUser = new CustomUser(id,username,"",authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(customUser, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJWT(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null) {
            logger.debug("JWT extracted from Authorization header: {}", jwt);
            return jwt;
        }
        Cookie[] cookies = request.getCookies();
        if ( cookies!= null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    logger.debug("JWT extracted from Cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        logger.debug("JWT not found in header or cookie");
        return null;
    }
}
