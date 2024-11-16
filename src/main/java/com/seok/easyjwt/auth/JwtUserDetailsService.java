package com.seok.easyjwt.auth;

import com.seok.easyjwt.user.JwtUser;
import com.seok.easyjwt.user.QueryJwtUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service implementation of {@link UserDetailsService} for loading user details using JWT.
 * <p>
 * This service integrates with Spring Security to load user details from a {@link QueryJwtUserService}.
 * It is typically used during authentication to validate users and set up the security context.
 * <p>
 * The class is marked with {@link ConditionalOnMissingBean}, allowing users to override this service
 * with their own implementation if needed.
 */
@ConditionalOnMissingBean(UserDetailsService.class)
public class JwtUserDetailsService implements UserDetailsService {

    private final QueryJwtUserService queryJwtUserService;

    /**
     * Constructs a new {@code JwtUserDetailsService} with the specified {@link QueryJwtUserService}.
     *
     * @param queryJwtUserService the service used to retrieve user details by username
     */
    public JwtUserDetailsService(QueryJwtUserService queryJwtUserService) {
        this.queryJwtUserService = queryJwtUserService;
    }

    /**
     * Loads a user's details by username.
     * <p>
     * This method queries the {@link QueryJwtUserService} to fetch a {@link JwtUser}.
     * If the user is not found, it throws a {@link UsernameNotFoundException}.
     *
     * @param username the username of the user to load
     * @return a {@link JwtUserDetails} object containing the user's details
     * @throws UsernameNotFoundException if no user is found with the specified username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUser jwtUser = queryJwtUserService.execute(username)
                .orElseThrow(() -> new UsernameNotFoundException("JwtUser Not Found"));

        return new JwtUserDetails(jwtUser);
    }
}