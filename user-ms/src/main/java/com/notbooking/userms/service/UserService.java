package com.notbooking.userms.service;

import com.notbooking.userms.exception.NotFoundException;
import com.notbooking.userms.model.User;
import com.notbooking.userms.model.UserRole;
import com.notbooking.userms.repository.UserRepository;
import com.notbooking.userms.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    public String resetPassword(String email) {
        Optional<User> user = userRepository.findByEmailAndNotDeleted(email);
        if (!user.isPresent()) {
            throw new NotFoundException("User with email " + email + " not found!");
        }
        String password = generateRandomPassword(20);
        user.get().setPassword(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user.get());
        return "Successfully reset password.";
    }

    public String findEmailByUsername(String username) {
        return userRepository.findEmailByUsernameAndNotDeleted(username);
    }

    public String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                + "jklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public Optional<UserRole> findRoleByUsername(String username){
        return userRoleRepository.findByUsername(username);
    }
}
