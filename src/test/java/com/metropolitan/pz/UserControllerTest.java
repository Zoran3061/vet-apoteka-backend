package com.metropolitan.pz;

import com.metropolitan.pz.controller.UserController;
import com.metropolitan.pz.entities.User;
import com.metropolitan.pz.entities.enums.Role;
import com.metropolitan.pz.repository.UserRepository;
import com.metropolitan.pz.security.JwtUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(createUser(1L, "John", "Doe", "johndoe", "password", Role.USER));
        users.add(createUser(2L, "Jane", "Smith", "janesmith", "password", Role.ADMIN));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        Long id = 1L;
        User user = createUser(id, "John", "Doe", "johndoe", "password", Role.USER);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userController.getUserById(id);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateUser() {
        User user = createUser(1L, "John", "Doe", "johndoe", "password", Role.USER);

        when(userDetailsService.save(user)).thenReturn(user);

        User result = userController.createUser(user);

        assertEquals(user, result);
        verify(userDetailsService, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {
        Long id = 1L;
        User existingUser = createUser(id, "John", "Doe", "johndoe", "password", Role.USER);
        User updatedUser = createUser(id, "Updated John", "Updated Doe", "updatedjohndoe", "updatedpassword", Role.ADMIN);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User result = userController.updateUser(id, updatedUser);

        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;

        userController.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    private User createUser(Long id, String firstName, String lastName, String username, String password, Role role) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}

