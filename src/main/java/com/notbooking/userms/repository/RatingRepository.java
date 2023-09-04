package com.notbooking.userms.repository;

import com.notbooking.userms.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    //ACCOMMODATION RATINGS

    @Query("select r from Rating r where r.isDeleted = false and " +
            "r.isHostRating = false and r.accommodation = :accommodation and r.guest.id = :guest ")
    Optional<Rating> findByGuestAndAccommodation(@Param("guest") Long guest,@Param("accommodation") String accommodation);

    @Query("select r from Rating r where r.isDeleted = true and " +
            "r.isHostRating = false and r.accommodation = :accommodation and r.guest.id = :guest ")
    Optional<Rating> findByGuestAndAccommodationDeleted(@Param("guest") Long guest,@Param("accommodation") String accommodation);

    @Query("select r from Rating r where r.isDeleted = false and " +
            "r.isHostRating = false and r.accommodation = :accommodation ")
    List<Rating> findByAccommodation( @Param("accommodation") String accommodation);

    //HOST RATINGS

    @Query("select r from Rating r where r.isDeleted = false and " +
            "r.isHostRating = true and r.host.id = :host and r.guest.id = :guest ")
    Optional<Rating> findByGuestAndHost(@Param("guest") Long guest,@Param("host") Long host);

    @Query("select r from Rating r where r.isDeleted = true and " +
            "r.isHostRating = true and r.host.id = :host and r.guest.id = :guest ")
    Optional<Rating> findByGuestAndHostDeleted(@Param("guest") Long guest,@Param("host") Long host);

    @Query("select r from Rating r where " +
            "r.isDeleted = false and r.isHostRating = true and r.host.id = :host ")
    List<Rating> findByHost( @Param("host") Long host);
}
