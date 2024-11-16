package com.seok.easyjwt.user;

import com.seok.easyjwt.auth.JwtUserDetailsService;

import java.util.Optional;

/**
 * Defines a service for querying user details based on the username.
 * <p>
 * This interface abstracts the process of retrieving user information for JWT authentication.
 * It is typically used by the {@link JwtUserDetailsService} to fetch {@link JwtUser} details
 * during the authentication process.
 * <p>
 * Usage:
 * - Implement this interface to provide user retrieval logic, such as querying a database
 *   or an external service.
 */
public interface QueryJwtUserService {

    /**
     * Executes a query to fetch a {@link JwtUser} by username.
     * <p>
     * The implementation should return an {@link Optional} containing the {@link JwtUser} if found,
     * or an empty {@link Optional} if no user is found with the specified username.
     *
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing the {@link JwtUser}, or empty if no user is found
     */
    Optional<JwtUser> execute(String username);
}
