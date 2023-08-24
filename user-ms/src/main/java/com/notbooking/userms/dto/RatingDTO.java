package com.notbooking.userms.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private String guestEmail;
    private boolean hostRating;
    private String hostEmail;
    private int accommodation;
    private int score;
    private boolean editExistingRating;

    public boolean isEditExistingRating() {
        return editExistingRating;
    }

    public void setEditExistingRating(boolean editExistingRating) {
        this.editExistingRating = editExistingRating;
    }

    public boolean isHostRating() {
        return hostRating;
    }

    public void setHostRating(boolean hostRating) {
        this.hostRating = hostRating;
    }
}
