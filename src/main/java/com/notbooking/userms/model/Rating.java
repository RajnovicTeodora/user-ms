package com.notbooking.userms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rating")
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @Column(name = "is_host_rating", nullable = false)
    private boolean isHostRating;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = true)
    private Host host;

    @Column(name = "accommodation_id", nullable = false)
    private String accommodation;

    public Rating(LocalDate date, int score, Guest guest, String accommodation){
        this.date = date;
        this.score = score;
        this.guest = guest;
        this.accommodation = accommodation;
        this.isHostRating = false;
        this.isDeleted = false;
    }

    public Rating(LocalDate date, int score, Guest guest, Host host){
        this.date = date;
        this.score = score;
        this.guest = guest;
        this.host = host;
        this.isHostRating = true;
        this.isDeleted = false;
    }

    public boolean isHostRating() {
        return isHostRating;
    }

    public void setHostRating(boolean hostRating) {
        isHostRating = hostRating;
    }
}
