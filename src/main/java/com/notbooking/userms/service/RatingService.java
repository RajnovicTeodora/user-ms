package com.notbooking.userms.service;

import com.notbooking.userms.dto.RatingDTO;
import com.notbooking.userms.dto.TableCellDTO;
import com.notbooking.userms.exception.BadRequestException;
import com.notbooking.userms.exception.NotFoundException;
import com.notbooking.userms.model.*;
import com.notbooking.userms.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserService userService;

    public List<Rating> findAllAccommodationRatings(String accommodation) {
        return ratingRepository.findByAccommodation(accommodation);
    }

    public List<Rating> findAllHostRatings(String username) {
        User host = userService.findUserByUsername(username);
        return ratingRepository.findByHost(host.getId());
    }

    public double getAvgAccommodationScore(String accommodation){
        List<Rating> rl = findAllAccommodationRatings(accommodation);
        if(rl.isEmpty()){
            return 0;
        }else{
            double sum = rl.stream().map(Rating::getScore).reduce(0, Integer::sum);
            return sum / rl.size();
        }
    }

    public double getAvgHostScore(String username){
        List<Rating> rl = findAllHostRatings(username);
        if(rl.isEmpty()){
            return 0;
        }else{
            double sum = rl.stream().map(Rating::getScore).reduce(0, Integer::sum);
            return sum / rl.size();
        }
    }

    public List<TableCellDTO> getAllAccommodationScores(String accommodation){
        List<Rating> rl = findAllAccommodationRatings(accommodation);
        if(rl.isEmpty()){
            throw new NotFoundException("Accommodation has no ratings");
        }else{
            List<TableCellDTO> table = new ArrayList<>();
            for (Rating r : rl) {
                table.add(new TableCellDTO(r.getScore(), r.getDate().toString(), r.getGuest().getUsername()));
            }
            return table;
        }
    }

    public List<TableCellDTO> getAllHostScores(String hostUsername){
        List<Rating> rl = findAllHostRatings(hostUsername);
        if(rl.isEmpty()){
            throw new NotFoundException("Host has no ratings");
        }else{
            List<TableCellDTO> table = new ArrayList<>();
            for (Rating r : rl) {
                table.add(new TableCellDTO(r.getScore(), r.getDate().toString(), r.getGuest().getUsername()));
            }
            return table;
        }
    }

    public int getExistingAccommodationScore(String username, String accommodation){
        Optional<Rating> r = ratingRepository.findByGuestAndAccommodation(
                userService.findUserByUsername(username).getId(), accommodation);
        if(r.isPresent()){
            return r.get().getScore();
        }
        return 0;
    }

    public int getExistingHostScore(String guestUsername, String hostUsername){
        Optional<Rating> r = ratingRepository.findByGuestAndHost(
                userService.findUserByUsername(guestUsername).getId(),
                userService.findUserByUsername(hostUsername).getId());
        if(r.isPresent()){
            return r.get().getScore();
        }
        return 0;
    }

    public int saveRating(RatingDTO ratingDTO){
        User guest = userService.findUserByUsername(ratingDTO.getGuestUsername());
        //EDIT EXISTING RATING
        if(ratingDTO.isEditExistingRating()){
            Optional<Rating> r;
            if(!ratingDTO.isHostRating()) {
                r = ratingRepository.findByGuestAndAccommodation(
                        guest.getId(), ratingDTO.getAccommodation());

            }else{
                User host = userService.findUserByUsername(ratingDTO.getHostUsername());
                r = ratingRepository.findByGuestAndHost(
                        guest.getId(), host.getId());
            }
            if (!r.isPresent()) {
                throw new BadRequestException("Existing rating not found");
            }
            r.get().setScore(ratingDTO.getScore());
            r.get().setDate(LocalDate.now());
            ratingRepository.save(r.get());
        }
        //CREATE NEW RATING
        else{
            Optional<Rating> r;
            if(!ratingDTO.isHostRating()){
                r = ratingRepository.findByGuestAndAccommodationDeleted(
                        guest.getId(), ratingDTO.getAccommodation());
                if(r.isPresent()){
                    r.get().setDeleted(false);
                    r.get().setDate(LocalDate.now());
                    r.get().setScore(ratingDTO.getScore());
                    ratingRepository.save(r.get());
                }else{
                    Rating nr = new Rating(
                            LocalDate.now(), ratingDTO.getScore(), (Guest) guest, ratingDTO.getAccommodation());
                    ratingRepository.save(nr);
                }
            }else{
                User host = userService.findUserByUsername(ratingDTO.getHostUsername());
                r = ratingRepository.findByGuestAndHostDeleted(
                        guest.getId(), host.getId());
                if(r.isPresent()){
                    r.get().setDeleted(false);
                    r.get().setDate(LocalDate.now());
                    r.get().setScore(ratingDTO.getScore());
                    ratingRepository.save(r.get());
                }else {
                    Rating nr = new Rating(
                            LocalDate.now(), ratingDTO.getScore(), (Guest) guest, (Host) host);
                    ratingRepository.save(nr);
                }
            }
        }
        return ratingDTO.getScore();
    }

    public int deleteRating(RatingDTO ratingDTO){
        Optional<Rating> r;
        User guest = userService.findUserByUsername(ratingDTO.getGuestUsername());
        if(ratingDTO.isHostRating()) {
            r = ratingRepository.findByGuestAndHost(
                    guest.getId(),
                    userService.findUserByUsername(ratingDTO.getHostUsername()).getId());
        }else{
            r = ratingRepository.findByGuestAndAccommodation(
                    guest.getId(),
                    ratingDTO.getAccommodation());
        }
        if(!r.isPresent()){
            throw new BadRequestException("Existing rating not found");
        }
        r.get().setDeleted(true);
        ratingRepository.save(r.get());
        return 0;
    }
}
