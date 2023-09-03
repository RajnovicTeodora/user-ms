package com.notbooking.userms.controller;

import com.notbooking.userms.dto.ChangePassDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
            token.setId(this.userDetailsService.findIdByUsername(userDetails.getUsername()));

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

    @PreAuthorize("hasAnyRole('HOST', 'GUEST')")
    @GetMapping(value = "/changeNotification/{email}/{type}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> changeNotification(@PathVariable String email, @PathVariable int type ) {
        try {
            return new ResponseEntity<>(userService.changeNotification(email, type), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with email "+ email + " not found!", HttpStatus.NOT_FOUND);
        }catch (BadRequestException e) {
            return new ResponseEntity<>("Entered notification type does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('HOST', 'GUEST')")
    @GetMapping(value = "/deleteAccount/{email}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable String email) {
        try {
            return new ResponseEntity<>(userService.deleteAccount(email), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with email "+ email + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('HOST', 'GUEST')")
    @GetMapping(value = "/getUserInfo/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfo(@PathVariable String email) {
        try {
            return new ResponseEntity<>(userService.getUserInfo(email), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with email "+ email + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('HOST', 'GUEST')")
    @PostMapping(path = "/editUser/{email}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editUser(@RequestBody NewUserDTO newUserDTO, @PathVariable String email){
        try {
            return new ResponseEntity<>(userService.editUser(newUserDTO, email), HttpStatus.OK);
        }catch (EmailExistsException e){
            return new ResponseEntity<>("Email already exists!", HttpStatus.FORBIDDEN);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }catch (UsernameExistsException e){
            return new ResponseEntity<>("Username already exists!", HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAnyRole('HOST', 'GUEST')")
    @PostMapping(path = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody ChangePassDTO changePassDTO){
        try {
            return new ResponseEntity<>(userService.changePassword(changePassDTO), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with email "+ changePassDTO.getEmail() + " not found!", HttpStatus.NOT_FOUND);
        }catch (BadRequestException e) {
            return new ResponseEntity<>("Incorrect password.", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('HOST', 'GUEST')")
    @GetMapping(value = "/checkNotification/{username}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> checkNotificationActive(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.checkNotification(username), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
    }

}
