package com.seok.easyjwt.configuration;

import com.seok.easyjwt.auth.JwtUserDetailsService;
import com.seok.easyjwt.jwt.JwtFilter;
import com.seok.easyjwt.jwt.JwtProperties;
import com.seok.easyjwt.jwt.JwtTokenProvider;
import com.seok.easyjwt.user.CurrentUserService;
import com.seok.easyjwt.user.JwtUser;
import com.seok.easyjwt.user.QueryJwtUserService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Auto-configuration for the Easy-JWT library.
 * <p>
 * This class provides the necessary beans and configuration for JWT functionality.
 * It integrates seamlessly with Spring Boot by leveraging conditional annotations to
 * enable or disable functionality based on application properties.
 * <p>
 * By default, the configuration is enabled if the property {@code easy-jwt.enabled} is not set
 * or explicitly set to {@code true}.
 * <p>
 * Key Features:
 * - Configures {@link JwtFilter} for validating JWT tokens.
 * - Provides default implementations for required beans, such as {@link QueryJwtUserService},
 *   unless overridden by the user.
 */
@AutoConfiguration
@ConditionalOnProperty(name = "easy-jwt.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@EnableConfigurationProperties(JwtProperties.class)
public class EasyJwtAutoConfiguration {

    /**
     * Provides a {@link QueryJwtUserService} bean.
     * <p>
     * This service is responsible for retrieving {@link JwtUser} details based on the username.
     * By default, an exception is thrown if no implementation is provided.
     *
     * @return the {@link QueryJwtUserService} bean
     * @throws IllegalStateException if no {@link QueryJwtUserService} bean is found
     */
    @Bean
    @ConditionalOnMissingBean(QueryJwtUserService.class)
    public QueryJwtUserService queryJwtUserService() {
        throw new IllegalStateException("No QueryJwtUserService bean found. Please provide an implementation.");
    }

    /**
     * Provides a {@link JwtUserDetailsService} bean.
     * <p>
     * This service integrates with Spring Security to load user details for authentication.
     *
     * @param queryJwtUserService the service used to fetch user details
     * @return the {@link JwtUserDetailsService} bean
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public JwtUserDetailsService jwtUserDetailsService(QueryJwtUserService queryJwtUserService) {
        return new JwtUserDetailsService(queryJwtUserService);
    }

    /**
     * Provides a {@link JwtFilter} bean.
     * <p>
     * This filter intercepts HTTP requests to validate JWT tokens and set authentication
     * in the {@link org.springframework.security.core.context.SecurityContextHolder}.
     *
     * @param jwtTokenProvider the provider responsible for token validation
     * @return the {@link JwtFilter} bean
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtFilter jwtFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtFilter(jwtTokenProvider);
    }

    /**
     * Provides a {@link JwtTokenProvider} bean.
     * <p>
     * This provider is responsible for generating, validating, and parsing JWT tokens.
     *
     * @param userDetailsService the service used to load user details
     * @param jwtProperties      the properties for JWT configuration
     * @return the {@link JwtTokenProvider} bean
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(UserDetailsService userDetailsService, JwtProperties jwtProperties) {
        return new JwtTokenProvider(userDetailsService, jwtProperties);
    }

    /**
     * Provides a {@link CurrentUserService} bean.
     * <p>
     * This service allows easy retrieval of the current authenticated user.
     *
     * @return the {@link CurrentUserService} bean
     */
    @Bean
    @ConditionalOnMissingBean
    public CurrentUserService<?> currentUserService() {
        return new CurrentUserService<>();
    }
}