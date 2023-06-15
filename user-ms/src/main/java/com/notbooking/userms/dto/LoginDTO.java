package com.notbooking.userms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {

    private String email;
    private String password;

    public LoginDTO(String email, String password) {
        this.setPassword(password);
        this.setEmail(email);
    }

}
