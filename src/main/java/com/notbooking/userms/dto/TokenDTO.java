package com.notbooking.userms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDTO {

    private String accessToken;
    private String username;
    private Long expiresIn;
    private String userType;
    private String email;
    private String fullName;
    private int id;

    public TokenDTO(String token, String username, Long expiresIn, String userType) {
        this.setAccessToken(token);
        this.setUsername(username);
        this.setExpiresIn(expiresIn);
        this.setUserType(userType);
    }

}
