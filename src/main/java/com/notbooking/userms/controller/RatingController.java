package com.notbooking.userms.controller;

import com.notbooking.userms.dto.RatingDTO;
import com.notbooking.userms.exception.BadRequestException;
import com.notbooking.userms.exception.NotFoundException;
import com.notbooking.userms.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping(value = "/getAvgAccommodationScore/{accommodation}")
    public ResponseEntity<?> getAvgAccommodationScore(@PathVariable String accommodation) {
        try {
            return new ResponseEntity<>(ratingService.getAvgAccommodationScore(accommodation), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("Accommodation not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAvgHostScore/{username}")
    public ResponseEntity<?> getAvgHostScore(@PathVariable String username) {
        try {
            return new ResponseEntity<>(ratingService.getAvgHostScore(username), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("Host not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAllAccommodationScores/{accommodation}")
    public ResponseEntity<?> getAllAccommodationScores(@PathVariable String accommodation) {
        try {
            return new ResponseEntity<>(ratingService.getAllAccommodationScores(accommodation), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("Accommodation has no ratings", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAllHostScores/{hostUsername}")
    public ResponseEntity<?> getAllHostScores(@PathVariable String hostUsername) {
        try {
            return new ResponseEntity<>(ratingService.getAllHostScores(hostUsername), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("Host has no ratings", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('GUEST')")
    @GetMapping(value = "/getExistingAccommodationScore/{username}/{accommodation}")
    public ResponseEntity<?> getExistingAccommodationScore(@PathVariable String username, @PathVariable String accommodation) {
        try {
            return new ResponseEntity<>(ratingService.getExistingAccommodationScore(username, accommodation), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with username "+ username + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('GUEST')")
    @GetMapping(value = "/getExistingHostScore/{guestUsername}/{hostUsername}")
    public ResponseEntity<?> getExistingHostScore(@PathVariable String guestUsername, @PathVariable String hostUsername) {
        try {
            return new ResponseEntity<>(ratingService.getExistingHostScore(guestUsername, hostUsername), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with username "+ guestUsername + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('GUEST')")
    @PostMapping(path = "/saveRating", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveRating(@RequestBody RatingDTO ratingDTO){
        try {
            return new ResponseEntity<>(ratingService.saveRating(ratingDTO), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }catch (BadRequestException e){
            return new ResponseEntity<>("Existing rating not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('GUEST')")
    @PostMapping(path = "/deleteRating", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRating(@RequestBody RatingDTO ratingDTO){
        try {
            return new ResponseEntity<>(ratingService.deleteRating(ratingDTO), HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }catch (BadRequestException e){
            return new ResponseEntity<>("Existing rating not found!", HttpStatus.NOT_FOUND);
        }
    }

}
