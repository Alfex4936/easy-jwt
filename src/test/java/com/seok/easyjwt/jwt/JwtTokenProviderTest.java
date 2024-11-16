package com.seok.easyjwt.jwt;


import com.seok.easyjwt.auth.JwtUserDetails;
import com.seok.easyjwt.exception.ExpiredTokenException;
import com.seok.easyjwt.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtProperties jwtProperties;
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("VerySecretKey12345678901234567890"); // At least 256 bits
        jwtProperties.setAccessTokenExpiration(3L); // Short expiration for testing
        jwtProperties.setRefreshTokenExpiration(4L);

        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService, jwtProperties);
    }

    @Test
    public void testGenerateAccessToken() {
        String token = jwtTokenProvider.generateAccessToken("testUser", null);
        assertNotNull(token);
    }

    @Test
    public void testGenerateRefreshToken() {
        String token = jwtTokenProvider.generateRefreshToken("testUser", null);
        assertNotNull(token);
    }

    @Test
    public void testGetAuthentication_ValidAccessToken() {
        String token = jwtTokenProvider.generateAccessToken("testUser", null);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        assertNotNull(jwtTokenProvider.getAuthentication(token));
        verify(userDetailsService).loadUserByUsername("testUser");
    }

    @Test
    public void testGetAuthentication_InvalidTokenType() {
        String token = jwtTokenProvider.generateRefreshToken("testUser", null);

        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> {
            jwtTokenProvider.getAuthentication(token);
        });
        assertEquals("Invalid token type", exception.getMessage());
    }

    @Test
    public void testGetAuthentication_ExpiredToken() throws InterruptedException {
        jwtProperties.setAccessTokenExpiration(1L); // 1 second expiration
        String token = jwtTokenProvider.generateAccessToken("testUser", null);
        Thread.sleep(1500); // Sleep to expire the token

        assertThrows(ExpiredTokenException.class, () -> {
            jwtTokenProvider.getAuthentication(token);
        });
    }

    @Test
    public void testGetAuthentication_InvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(InvalidTokenException.class, () -> {
            jwtTokenProvider.getAuthentication(invalidToken);
        });
    }
}