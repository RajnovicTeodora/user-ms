package com.notbooking.userms.model;

import com.notbooking.userms.dto.NewUserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "host")
@Getter
@Setter
public class Host extends User {

    @OneToMany(mappedBy = "host")
    private List<Rating> ratings;

    public Host() {
        super();
    }

    public Host(NewUserDTO newUserDTO){
        super(newUserDTO);
    }
}
