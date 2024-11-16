package com.seok.easyjwt.auth;

import com.seok.easyjwt.user.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * Implementation of {@link UserDetails} that wraps a {@link JwtUser}.
 * <p>
 * This class is used by Spring Security to represent an authenticated user. It integrates
 * JWT-specific user details with Spring Security's user model.
 * <p>
 * Key Features:
 * - Maps {@link JwtUser} details (e.g., username and authorities) to Spring Security's {@link UserDetails}.
 * - Provides default values for account status methods (e.g., non-expired, non-locked).
 */
public class JwtUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;
    private final JwtUser jwtUser;

    /**
     * Constructs a new {@code JwtUserDetails} object with the specified {@link JwtUser}.
     *
     * @param jwtUser the JWT user to wrap
     */
    public JwtUserDetails(JwtUser jwtUser) {
        this.jwtUser = jwtUser;
    }

    /**
     * Returns the authorities granted to the user.
     * <p>
     * These correspond to roles or permissions associated with the {@link JwtUser}.
     *
     * @return the authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return jwtUser.getAuthorities();
    }

    /**
     * Returns an empty string, as the password is not stored in the JWT.
     *
     * @return an empty string
     */
    @Override
    public String getPassword() {
        return ""; // Return empty string to avoid NullPointerException
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return jwtUser.getUsername();
    }

    /**
     * Indicates whether the user's account has expired.
     * <p>
     * This implementation always returns {@code true}, but can be customized as needed.
     *
     * @return {@code true} if the account is non-expired; {@code false} otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize as needed
    }

    /**
     * Indicates whether the user's account is locked.
     * <p>
     * This implementation always returns {@code true}, but can be customized as needed.
     *
     * @return {@code true} if the account is non-locked; {@code false} otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize as needed
    }

    /**
     * Indicates whether the user's credentials (e.g., password) have expired.
     * <p>
     * This implementation always returns {@code true}, but can be customized as needed.
     *
     * @return {@code true} if the credentials are non-expired; {@code false} otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize as needed
    }

    /**
     * Indicates whether the user's account is enabled.
     * <p>
     * This implementation always returns {@code true}, but can be customized as needed.
     *
     * @return {@code true} if the account is enabled; {@code false} otherwise
     */
    @Override
    public boolean isEnabled() {
        return true; // Customize as needed
    }

    /**
     * Returns the underlying {@link JwtUser}.
     *
     * @return the {@link JwtUser}
     */
    public JwtUser getJwtUser() {
        return jwtUser;
    }
}