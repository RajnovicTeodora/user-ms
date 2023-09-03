package com.notbooking.userms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private boolean type1;
    private boolean type2;
    private boolean type3;
    private boolean type4;
    private boolean type5;
    private Long userId;
}
