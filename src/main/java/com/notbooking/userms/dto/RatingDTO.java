package com.notbooking.userms.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private String guestUsername;
    private boolean hostRating;
    private String hostUsername;
    private String accommodation;
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
