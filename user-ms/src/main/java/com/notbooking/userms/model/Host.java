package com.notbooking.userms.model;

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
}
