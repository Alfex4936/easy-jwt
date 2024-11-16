package com.seok.easyjwt.jwt;


import com.seok.easyjwt.auth.JwtUserDetails;
import com.seok.easyjwt.auth.JwtUserDetailsService;
import com.seok.easyjwt.user.JwtUser;
import com.seok.easyjwt.user.QueryJwtUserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUserDetailsServiceTest {

    @Test
    public void testLoadUserByUsername_UserExists() {
        QueryJwtUserService queryJwtUserService = mock(QueryJwtUserService.class);
        JwtUser jwtUser = mock(JwtUser.class);
        when(queryJwtUserService.execute("testUser")).thenReturn(Optional.of(jwtUser));

        JwtUserDetailsService service = new JwtUserDetailsService(queryJwtUserService);
        JwtUserDetails userDetails = (JwtUserDetails) service.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals(jwtUser, userDetails.getJwtUser());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        QueryJwtUserService queryJwtUserService = mock(QueryJwtUserService.class);
        when(queryJwtUserService.execute("testUser")).thenReturn(Optional.empty());

        JwtUserDetailsService service = new JwtUserDetailsService(queryJwtUserService);

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("testUser");
        });
    }
}