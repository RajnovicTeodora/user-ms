package com.notbooking.userms.service;

import com.notbooking.userms.exception.NotFoundException;
import com.notbooking.userms.model.User;
import com.notbooking.userms.model.UserRole;
import com.notbooking.userms.repository.UserRepository;
import com.notbooking.userms.repository.UserRoleRepository;
import com.notbooking.userms.security.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = this.userRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username '%s' is not found!", username)));

        return UserFactory.create(user);
    }

    public UserDetails loadUserByEmail(String email) {
        User user = this.userRepository.findByEmailAndNotDeleted(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email '%s' is not found!", email)));

        return UserFactory.create(user);
    }

    public String findRoleByUsername(String username){
        Optional<UserRole> ret = userRoleRepository.findByUsername(username);
        if(ret.isPresent()){
            return ret.get().getName();
        }
        return "";
    }

    public String findFullNameByUsername(String username){
        User user = this.userRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username '%s' is not found!", username)));
        return user.getName()+" "+user.getSurname();
    }

    public int findIdByUsername(String username){
        User user = this.userRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username '%s' is not found!", username)));
        return user.getId().intValue();
    }
}
