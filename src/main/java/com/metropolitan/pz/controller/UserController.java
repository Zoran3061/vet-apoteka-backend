package com.metropolitan.pz.controller;

import com.metropolitan.pz.entities.User;
import com.metropolitan.pz.repository.UserRepository;
import com.metropolitan.pz.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.metropolitan.pz.entities.enums.Role;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    public UserController(UserRepository userRepository,
                          JwtUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    // ADMIN vidi sve korisnike
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        // admin kreira samo magacionera
        user.setRole(Role.MAGACIONER);
        return userDetailsService.save(user); // ovde se radi BCrypt encode password
    }

    // ADMIN tu dodaje MAGACIONER
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        if (id == 1L) { // ili ako je current admin id
            throw new RuntimeException("Cannot delete default admin.");
        }
        userRepository.deleteById(id);
    }

}


