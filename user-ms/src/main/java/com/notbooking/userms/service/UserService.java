package com.notbooking.userms.service;

import com.notbooking.userms.dto.NewUserDTO;
import com.notbooking.userms.exception.EmailExistsException;
import com.notbooking.userms.exception.UsernameExistsException;
import com.notbooking.userms.model.Guest;
import com.notbooking.userms.model.Host;
import com.notbooking.userms.model.UserRole;
import com.notbooking.userms.repository.AddressRepository;
import com.notbooking.userms.repository.UserRepository;
import com.notbooking.userms.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public String findEmailByUsername(String username) {
        return userRepository.findEmailByUsernameAndNotDeleted(username);
    }

    public Optional<UserRole> findRoleByUsername(String username){
        return userRoleRepository.findByUsername(username);
    }

    public void addUser(NewUserDTO newUserDTO) {
        if (userRepository.findByEmailAndNotDeleted(newUserDTO.getEmail()).isPresent()) {
            throw new EmailExistsException("Email already exists!");
        } else if (userRepository.findByUsernameAndNotDeleted(newUserDTO.getUsername()).isPresent()) {
            throw new UsernameExistsException("Username already exists!");
        }

        if (newUserDTO.getUserRole().equals("HOST")) {
            Host newHost = new Host(newUserDTO);
            newHost.setRole(userRoleRepository.findByName("HOST").get());
            newHost.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
            addressRepository.saveAndFlush(newHost.getAddress());
            userRepository.saveAndFlush(newHost);
        }else{
            Guest newGuest = new Guest(newUserDTO);
            newGuest.setRole(userRoleRepository.findByName("GUEST").get());
            newGuest.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
            addressRepository.saveAndFlush(newGuest.getAddress());
            userRepository.saveAndFlush(newGuest);
        }
    }
}
