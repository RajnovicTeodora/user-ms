package com.notbooking.userms.dto;

import com.notbooking.userms.model.User;
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

    public NewUserDTO(User user){
        this.username = user.getUsername();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.country = user.getAddress().getCountry();
        this.city = user.getAddress().getCity();
        this.street = user.getAddress().getStreet();
        this.streetNum = user.getAddress().getStreetNum();
    }
}
