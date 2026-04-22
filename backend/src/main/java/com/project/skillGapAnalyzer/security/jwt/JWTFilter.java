package com.project.skillGapAnalyzer.security.jwt;

import com.project.skillGapAnalyzer.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtil jwtUtil;

    public JWTFilter(UserDetailsServiceImpl userDetailsService, JWTUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String userName = null;

        try {
            userName = jwtUtil.extractUserName(token);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT expired for request: {} {}", request.getMethod(), request.getRequestURI());
        } catch (MalformedJwtException | SignatureException e) {
            logger.warn("Invalid JWT token for request: {} {}", request.getMethod(), request.getRequestURI());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token is empty or invalid");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                if (jwtUtil.validateToken(token, userDetails.getUsername())) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.debug("User authenticated: {}", userName);

                } else {
                    logger.warn("JWT validation failed for user: {}", userName);
                }

            } catch (UsernameNotFoundException e) {
                logger.warn("User not found for token: {}", userName);
            }
        }

        filterChain.doFilter(request, response);
    }
}