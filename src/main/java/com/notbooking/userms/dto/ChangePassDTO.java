package com.notbooking.userms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePassDTO {
    private String email;
    private String oldPass;
    private String newPass;
}
