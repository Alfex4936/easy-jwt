package com.seok.easyjwt.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Easy-JWT library.
 * <p>
 * These properties control the behavior of JWT token generation, validation, and related functionality.
 * Users can define these properties in their application configuration (e.g., `application.yml` or `application.properties`).
 */
@ConfigurationProperties("easy-jwt")
public class JwtProperties {

    /**
     * Indicates whether the JWT functionality is enabled. Defaults to {@code true}.
     */
    private boolean enabled = true;

    /**
     * The secret key used for signing and verifying JWT tokens.
     * <p>
     * This key must be at least 256 bits (32 characters) when using the HS256 algorithm.
     */
    private String secret;

    /**
     * The expiration time (in seconds) for access tokens. Defaults to 10 minutes (600 seconds).
     */
    private Long accessTokenExpiration = 600L; // Default 10 minutes

    /**
     * The expiration time (in seconds) for refresh tokens. Defaults to 30 days (2592000 seconds).
     */
    private Long refreshTokenExpiration = 2592000L; // Default 30 days

    /**
     * The prefix for JWT tokens in the HTTP Authorization header. Defaults to "Bearer ".
     * <p>
     * Example: "Bearer <token>"
     */
    private String tokenPrefix = "Bearer ";

    /**
     * The name of the HTTP header where the token is expected. Defaults to "Authorization".
     */
    private String headerString = "Authorization";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(Long accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(Long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHeaderString() {
        return headerString;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }
}
