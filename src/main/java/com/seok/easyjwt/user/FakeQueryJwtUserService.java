package com.seok.easyjwt.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A mock implementation of {@link QueryJwtUserService} for testing or fallback purposes.
 * <p>
 * This class provides a no-op implementation that always returns an empty {@link Optional}.
 * It can be used as a placeholder when no real implementation of {@link QueryJwtUserService} is available.
 * <p>
 * Usage:
 * - Replace this class with a real implementation for production environments.
 */
@Repository
public class FakeQueryJwtUserService implements QueryJwtUserService {

    /**
     * Executes a query to fetch a {@link JwtUser} by username.
     * <p>
     * This implementation always returns an empty {@link Optional}.
     *
     * @param username the username of the user to fetch
     * @return an empty {@link Optional}, as this is a mock implementation
     */
    @Override
    public Optional<JwtUser> execute(String username) {
        return Optional.empty();
    }
}
