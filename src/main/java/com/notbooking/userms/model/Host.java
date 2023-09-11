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

    @Column(name = "notificationType1Active", nullable = false)
    private boolean notificationType1Active; //RESERVATION_REQUEST

    @Column(name = "notificationType2Active", nullable = false)
    private boolean notificationType2Active; //RESERVATION_CANCELLATION

    @Column(name = "notificationType3Active", nullable = false)
    private boolean notificationType3Active; //HOST_RATING

    @Column(name = "notificationType4Active", nullable = false)
    private boolean notificationType4Active; //ACCOMMODATION_RATING

    @OneToMany(mappedBy = "host")
    private List<Rating> ratings;

    public Host() {
        super();
    }

    public Host(NewUserDTO newUserDTO){
        super(newUserDTO);
    }
}
