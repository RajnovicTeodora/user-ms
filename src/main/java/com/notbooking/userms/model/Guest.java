package com.notbooking.userms.model;

import com.notbooking.userms.dto.NewUserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "guest")
@Getter
@Setter
public class Guest extends User {

    public Guest() {
        super();
    }

    public Guest(NewUserDTO newUserDTO){
        super(newUserDTO);
    }
}