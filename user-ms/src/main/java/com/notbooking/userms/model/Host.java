package com.notbooking.userms.model;

import com.notbooking.userms.dto.NewUserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "host")
@Getter
@Setter
public class Host extends User {

    public Host() {
        super();
    }

    public Host(NewUserDTO newUserDTO){
        super(newUserDTO);
    }
}
