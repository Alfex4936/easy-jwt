package com.seok.easyjwt.jwt;

/**
 * Enumeration representing the type of JWT tokens.
 * <p>
 * This is used to distinguish between different types of tokens, such as:
 * - {@link #ACCESS}: Tokens used for granting access to protected resources.
 * - {@link #REFRESH}: Tokens used to obtain new access tokens after the current one expires.
 */
public enum TokenType {
    /**
     * Represents an access token.
     * <p>
     * Access tokens are used to authenticate and authorize users for protected API endpoints.
     */
    ACCESS,

    /**
     * Represents a refresh token.
     * <p>
     * Refresh tokens are used to obtain new access tokens without requiring re-authentication.
     */
    REFRESH
}