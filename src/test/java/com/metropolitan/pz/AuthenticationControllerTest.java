package com.metropolitan.pz;

import com.metropolitan.pz.controller.AuthenticationController;
import com.metropolitan.pz.entities.User;
import com.metropolitan.pz.entities.enums.Role;
import com.metropolitan.pz.security.JwtToken;
import com.metropolitan.pz.security.JwtTokenUtil;
import com.metropolitan.pz.security.JwtUserDetails;
import com.metropolitan.pz.security.JwtUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

@SpringBootTest
public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Test
    public void testLogin_Success() throws Exception {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(Role.USER);

        // Mock the authenticationManager.authenticate() method
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        // Mock the userDetailsService.loadUserByUsername() method
        JwtUserDetails userDetails = new JwtUserDetails(user);
        Mockito.when(userDetailsService.loadUserByUsername(user.getUsername()))
                .thenReturn(userDetails);

        // Mock the jwtTokenUtil.generateToken() method
        String token = "dummy_token";
        Mockito.when(jwtTokenUtil.generateToken(userDetails))
                .thenReturn(token);

        ResponseEntity<JwtToken> response = authenticationController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().getToken());
    }

//    @Test(expected = Exception.class)
//    public void testLogin_InvalidCredentials() throws Exception {
//        User user = new User("testuser", "invalidpassword");
//
//        // Mock the authenticationManager.authenticate() method to throw BadCredentialsException
//        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(BadCredentialsException.class);
//
//        authenticationController.login(user);
//    }

//    @Test(expected = Exception.class)
//    public void testLogin_UserDisabled() throws Exception {
//        User user = new User("testuser", "password");
//
//        // Mock the authenticationManager.authenticate() method to throw DisabledException
//        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(DisabledException.class);
//
//        authenticationController.login(user);
//    }

    @Test
    public void testSaveUser_Success() throws Exception {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(Role.USER);

        // Mock the userDetailsService.save() method
        Mockito.when(userDetailsService.save(user))
                .thenReturn(user);

        ResponseEntity<?> response = authenticationController.saveUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}
