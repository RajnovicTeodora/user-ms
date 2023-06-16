package com.notbooking.userms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDTO {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String userRole;
    private String country;
    private String city;
    private String street;
    private int streetNum;

}
