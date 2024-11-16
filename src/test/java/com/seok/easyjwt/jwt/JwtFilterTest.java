package com.seok.easyjwt.jwt;


import com.seok.easyjwt.exception.EasyJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    private JwtFilter jwtFilter;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        jwtFilter = new JwtFilter(jwtTokenProvider);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "valid.token.here";
        when(jwtTokenProvider.resolveToken(request)).thenReturn(token);
        Authentication authentication = mock(Authentication.class);
        when(jwtTokenProvider.getAuthentication(token)).thenReturn(authentication);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "invalid.token.here";
        when(jwtTokenProvider.resolveToken(request)).thenReturn(token);
        when(jwtTokenProvider.getAuthentication(token)).thenThrow(new EasyJwtException("Invalid Token"));

        // Expect an EasyJwtException to be thrown
        assertThrows(EasyJwtException.class, () -> {
            jwtFilter.doFilterInternal(request, response, filterChain);
        });

        // Verify that the authentication is null
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify that filterChain.doFilter was NOT called
        verifyNoInteractions(filterChain);
    }

    @Test
    public void testDoFilterInternal_NoToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}