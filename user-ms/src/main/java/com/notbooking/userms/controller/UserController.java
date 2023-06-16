package com.notbooking.userms.controller;

import com.notbooking.userms.dto.LoginDTO;
import com.notbooking.userms.dto.NewUserDTO;
import com.notbooking.userms.dto.TokenDTO;
import com.notbooking.userms.exception.BadRequestException;
import com.notbooking.userms.exception.EmailExistsException;
import com.notbooking.userms.exception.NotFoundException;
import com.notbooking.userms.exception.UsernameExistsException;
import com.notbooking.userms.security.TokenUtils;
import com.notbooking.userms.service.UserDetailsService;
import com.notbooking.userms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            TokenUtils tokenUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity loginUser(@RequestBody LoginDTO login, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            TokenDTO token = new TokenDTO();
            UserDetails userDetails = this.userDetailsService.loadUserByEmail(login.getEmail());
            String tokenValue = this.tokenUtils.generateToken(userDetails);
            token.setAccessToken(tokenValue);
            token.setExpiresIn(this.tokenUtils.getExpiration());
            token.setUsername(userDetails.getUsername());
            token.setUserType(this.userDetailsService.findRoleByUsername(userDetails.getUsername()));
            token.setEmail(login.getEmail());
            token.setFullName(this.userDetailsService.findFullNameByUsername(userDetails.getUsername()));

            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), login.getPassword()));
            } catch (BadCredentialsException e) {
                return new ResponseEntity<>("Your credentials are bad. Please, try again", HttpStatus.BAD_REQUEST);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Wrong password!", HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("Unauthorized.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity logoutUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            SecurityContextHolder.clearContext();
            return new ResponseEntity<>("You successfully logged out!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User is not authenticated!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> addUser(@RequestBody NewUserDTO newUserDTO){
        try {
            userService.addUser(newUserDTO);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        }catch (EmailExistsException e){
            return new ResponseEntity<>("Email already exists!", HttpStatus.FORBIDDEN);
        }catch (UsernameExistsException e){
            return new ResponseEntity<>("Username already exists!", HttpStatus.FORBIDDEN);
        }
    }
}
