package com.seok.easyjwt.user;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a user in the JWT-based authentication system.
 * <p>
 * This interface defines the core properties of a user that can be authenticated using JWT.
 * It extends {@link Serializable} to allow user details to be serialized when necessary (e.g., in distributed systems).
 * <p>
 * Usage:
 * - Implement this interface in your user class to integrate with the Easy-JWT library.
 */
public interface JwtUser extends Serializable {

    /**
     * Retrieves the username of the user.
     * <p>
     * The username is typically used as the subject in the JWT token.
     *
     * @return the username of the user
     */
    String getUsername();

    /**
     * Retrieves the authorities granted to the user.
     * <p>
     * These authorities are typically used to enforce role-based access control (RBAC) in the application.
     *
     * @return a collection of {@link GrantedAuthority} objects representing the user's permissions
     */
    Collection<? extends GrantedAuthority> getAuthorities();
}