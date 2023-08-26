package com.notbooking.userms.service;

import com.notbooking.userms.dto.ChangePassDTO;
import com.notbooking.userms.dto.NewUserDTO;
import com.notbooking.userms.exception.BadRequestException;
import com.notbooking.userms.exception.EmailExistsException;
import com.notbooking.userms.exception.NotFoundException;
import com.notbooking.userms.exception.UsernameExistsException;
import com.notbooking.userms.model.*;
import com.notbooking.userms.repository.AddressRepository;
import com.notbooking.userms.repository.UserRepository;
import com.notbooking.userms.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmailAndNotDeleted(email);
        if (!user.isPresent()) {
            throw new NotFoundException("User with email " + email + " not found!");
        }
        return user.get();
    }

    public Optional<UserRole> findRoleByUsername(String username){
        return userRoleRepository.findByUsername(username);
    }

    public String addUser(NewUserDTO newUserDTO) {
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
        return newUserDTO.getUsername();
    }


    public String changeNotification(String email) {
        User user = findUserByEmail(email);
        user.setNotificationsActive(!user.isNotificationsActive());
        userRepository.saveAndFlush(user);
        return "Successfully changed notification settings!";
    }

    public String deleteAccount(String email) {
        User user = findUserByEmail(email);

        //TODO if user has active reservations delete is forbidden/
        // if host is deleted all his accommodations must be deleted
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
        return "Successfully deleted account!";
    }

    public NewUserDTO getUserInfo(String email){
        User user = findUserByEmail(email);
        return new NewUserDTO(user);
    }

    public NewUserDTO editUser(NewUserDTO userInfo, String email) {
        User user = findUserByEmail(email);
        if(!userInfo.getEmail().equalsIgnoreCase(email) &&
                userRepository.findByEmailAndNotDeleted(userInfo.getEmail()).isPresent()) {
            throw new EmailExistsException("Email already exists!");
        }
        if(!userInfo.getUsername().equalsIgnoreCase(user.getUsername()) &&
                userRepository.findByUsernameAndNotDeleted(userInfo.getUsername()).isPresent()) {
            throw new EmailExistsException("Username already exists!");
        }
        user.setName(userInfo.getName());
        user.setSurname(userInfo.getSurname());
        user.setUsername(userInfo.getUsername());
        user.setEmail(userInfo.getEmail());
        Optional<Address> address = addressRepository.findById(user.getAddress().getId());
        if (!address.isPresent()) {
            throw new NotFoundException("Address not found" );
        }
        address.get().setCity(userInfo.getCity());
        address.get().setCountry(userInfo.getCountry());
        address.get().setStreet(userInfo.getStreet());
        address.get().setStreetNum(userInfo.getStreetNum());
        user.setAddress(address.get());

        addressRepository.saveAndFlush(address.get());
        userRepository.saveAndFlush(user);
        return userInfo;
    }

    public String changePassword(ChangePassDTO changePassDTO) {
        User user = findUserByEmail(changePassDTO.getEmail());
        SecurityContextHolder.clearContext();
        Authentication authentication;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), changePassDTO.getOldPass()));
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Incorrect old password.");
        }
        SecurityContextHolder.clearContext();
        try {
            user.setPassword(passwordEncoder.encode(changePassDTO.getNewPass()));
            userRepository.save(user);
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), changePassDTO.getNewPass()));
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Credentials are incorrect. Please, try again");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "Successfully changed password!";
    }

}
