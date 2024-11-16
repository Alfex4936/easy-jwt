package com.seok.easyjwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter is responsible for intercepting incoming HTTP requests to validate JWT tokens.
 * <p>
 * If a token is valid:
 * - The filter extracts the authentication information from the token and sets it in the {@link SecurityContextHolder}.
 * <p>
 * If a token is invalid:
 * - It is expected that errors (e.g., invalid or expired tokens) will propagate to the client and
 *   be handled using a global exception handler (e.g., {@link org.springframework.web.bind.annotation.ControllerAdvice}).
 * <p>
 * Usage:
 * - This filter should be registered as part of the Spring Security filter chain.
 * - JWT tokens are resolved from the request header specified in {@link JwtProperties#getHeaderString()}.
 */
public class JwtFilter extends OncePerRequestFilter {
//    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructs a new {@code JwtFilter} with the specified {@code JwtTokenProvider}.
     *
     * @param jwtTokenProvider the token provider used for resolving and validating JWT tokens
     */
    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Processes each HTTP request to validate the JWT token, if present.
     * <p>
     * If a token is present and valid:
     * - The filter sets the corresponding {@link Authentication} in the {@link SecurityContextHolder}.
     * <p>
     * If a token is invalid:
     * - The exception should propagate, and the application can handle it using a global exception handler.
     *
     * @param request     the {@link HttpServletRequest} being processed
     * @param response    the {@link HttpServletResponse} for the request
     * @param filterChain the {@link FilterChain} to pass control to the next filter
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Users should handle errors with like @ControllerAdvice
//            try {
//
//            } catch (EasyJwtException e) {
//                // Token is invalid or expired
//                SecurityContextHolder.clearContext();
//                log.error("Invalid or expired token.", e);
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token.");
//                return;
//            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}