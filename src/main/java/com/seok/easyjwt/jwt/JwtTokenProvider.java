package com.seok.easyjwt.jwt;

import com.seok.easyjwt.exception.ExpiredTokenException;
import com.seok.easyjwt.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Provides functionality for generating, validating, and parsing JWT tokens.
 * <p>
 * This class serves as the core of the JWT functionality, offering methods to:
 * - Generate access and refresh tokens.
 * - Validate and parse tokens to extract authentication details.
 * - Resolve tokens from HTTP requests.
 * <p>
 * The {@code JwtTokenProvider} relies on the {@link JwtProperties} for configuration,
 * including the secret key, token expiration times, and HTTP header settings.
 */
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    /**
     * Constructs a new {@code JwtTokenProvider} with the specified dependencies.
     *
     * @param userDetailsService the service used to load user details by username
     * @param jwtProperties      the configuration properties for JWT tokens
     * @throws IllegalArgumentException if the secret key is null or empty
     */
    public JwtTokenProvider(@Qualifier("userDetailsService") UserDetailsService userDetailsService, JwtProperties jwtProperties) {
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;

        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().isEmpty()) {
            throw new IllegalArgumentException("JWT secret cannot be null or empty");
        }

        // Generate a SecretKey using the HS256 algorithm
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a new access token.
     * <p>
     * The token includes the specified claims and expires after the duration
     * defined in {@link JwtProperties#getAccessTokenExpiration()}.
     *
     * @param subject the subject (typically the username) for the token
     * @param claims  additional claims to include in the token
     * @return the generated access token
     */
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, jwtProperties.getAccessTokenExpiration(), TokenType.ACCESS, claims);
    }

    /**
     * Generates a new refresh token.
     * <p>
     * The token expires after the duration defined in {@link JwtProperties#getRefreshTokenExpiration()}.
     *
     * @param subject the subject (typically the username) for the token
     * @param claims  additional claims to include in the token
     * @return the generated refresh token
     */
    public String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, jwtProperties.getRefreshTokenExpiration(), TokenType.REFRESH, claims);
    }

    /**
     * Generates a JWT token with the specified parameters.
     *
     * @param subject            the subject (typically the username) for the token
     * @param expirationSeconds  the expiration time (in seconds) for the token
     * @param type               the type of token (e.g., ACCESS, REFRESH)
     * @param claims             additional claims to include in the token
     * @return the generated JWT token
     */
    private String generateToken(String subject, Long expirationSeconds, TokenType type, Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + expirationSeconds * 1000L);

        JwtBuilder builder = Jwts.builder().subject(subject).issuedAt(now).expiration(exp).claim("typ", type.name()).signWith(secretKey);

        if (claims != null && !claims.isEmpty()) {
            builder.claims(claims);
        }

        return builder.compact();
    }

    /**
     * Extracts authentication details from a token.
     * <p>
     * This method validates the token and retrieves the subject to load
     * the corresponding {@link UserDetails}.
     *
     * @param token the JWT token to parse
     * @return an {@link Authentication} object for the user
     * @throws InvalidTokenException if the token is invalid
     * @throws ExpiredTokenException if the token has expired
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        if (!TokenType.ACCESS.name().equals(claims.get("typ"))) {
            throw new InvalidTokenException("Invalid token type");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    /**
     * Resolves a JWT token from the HTTP request.
     * <p>
     * The token is extracted from the header specified in {@link JwtProperties#getHeaderString()}.
     * If the header is missing or does not contain a valid token, this method returns {@code null}.
     *
     * @param request the HTTP request to resolve the token from
     * @return the resolved JWT token, or {@code null} if no valid token is found
     */
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getHeaderString());
        if (token != null && token.startsWith(jwtProperties.getTokenPrefix())) {
            return token.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }

    /**
     * Parses and validates a JWT token, returning its claims.
     *
     * @param token the JWT token to parse
     * @return the claims contained in the token
     * @throws ExpiredTokenException if the token has expired
     * @throws InvalidTokenException if the token is invalid
     */
    private Claims getClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("Token has expired", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }

}