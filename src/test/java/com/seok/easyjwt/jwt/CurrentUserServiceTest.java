package com.seok.easyjwt.jwt;


import com.seok.easyjwt.auth.JwtUserDetails;
import com.seok.easyjwt.user.CurrentUserService;
import com.seok.easyjwt.user.JwtUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrentUserServiceTest {

    private CurrentUserService<JwtUser> currentUserService;

    @BeforeEach
    public void setUp() {
        currentUserService = new CurrentUserService<>();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetCurrentUser_Authenticated() {
        JwtUser jwtUser = mock(JwtUser.class);
        JwtUserDetails jwtUserDetails = new JwtUserDetails(jwtUser);
        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(jwtUserDetails)
        );

        JwtUser currentUser = currentUserService.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals(jwtUser, currentUser);
    }

    @Test
    public void testGetCurrentUser_NotAuthenticated() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            currentUserService.getCurrentUser();
        });
        assertEquals("Current user is not authenticated", exception.getMessage());
    }
}

class JwtAuthenticationToken extends org.springframework.security.authentication.UsernamePasswordAuthenticationToken {
    public JwtAuthenticationToken(JwtUserDetails principal) {
        super(principal, null, principal.getAuthorities());
    }
}