package com.seok.easyjwt.user;

import com.seok.easyjwt.auth.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service for retrieving the current authenticated user from the security context.
 * <p>
 * This class provides a type-safe way to access the current user who is authenticated via JWT.
 * It assumes that the principal stored in the security context is an instance of {@link JwtUserDetails}.
 * <p>
 * Usage:
 * - Call {@link #getCurrentUser()} to retrieve the current authenticated user.
 *
 * @param <T> the type of {@link JwtUser} being returned
 */
public class CurrentUserService<T extends JwtUser> {

    /**
     * Retrieves the current authenticated user from the {@link SecurityContextHolder}.
     * <p>
     * This method checks if the security context contains an authentication object
     * and if the principal is an instance of {@link JwtUserDetails}. If these conditions are met,
     * it returns the {@link JwtUser} associated with the current authentication.
     *
     * @return the current authenticated {@link JwtUser}
     * @throws IllegalStateException if the user is not authenticated or the principal is not a {@link JwtUserDetails}
     */
    @SuppressWarnings("unchecked")
    public T getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            return (T) ((JwtUserDetails) authentication.getPrincipal()).getJwtUser();
        } else {
            throw new IllegalStateException("Current user is not authenticated");
        }
    }
}