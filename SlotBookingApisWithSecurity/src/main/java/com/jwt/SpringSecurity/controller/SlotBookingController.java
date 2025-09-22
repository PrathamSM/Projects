package com.jwt.SpringSecurity.controller;


import com.jwt.SpringSecurity.dto.*;
import com.jwt.SpringSecurity.model.Booking;
import com.jwt.SpringSecurity.model.Rating;
import com.jwt.SpringSecurity.model.Slot;
import com.jwt.SpringSecurity.model.UserData;
import com.jwt.SpringSecurity.repo.SlotRepo;
import com.jwt.SpringSecurity.service.SlotBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SlotBookingController {

    @Autowired
    private SlotBookingService slotBookingService;
    @Autowired
    private SlotRepo slotRepo;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserData userData){
         slotBookingService.register(userData);
        return ResponseEntity.ok(userData.getUsername()+" Registered Successfully! with userid "+userData.getUserid());
    }

    @PostMapping("/login")
    public String login(@RequestBody UserData user) {

        return slotBookingService.verify(user);
    }


    @PostMapping("/create/slotsForMultipleDays")
    public ResponseEntity<List<Slot>> createSlotsForMultipleDays(@RequestBody SlotByDataRange slotByDataRange) {
        List<Slot> createdSlotsByDate = slotBookingService.createSlotsForMultipleDays(slotByDataRange);
        return ResponseEntity.ok(createdSlotsByDate);
    }

    // New endpoint to search users by expertise
    @GetMapping("/users/expertise")
    public ResponseEntity<List<UserData>> findUserWithExpertise(@RequestParam String expertise) {
        List<UserData> UserByExpertise = slotBookingService.findUserWithExpertise(expertise);
        return ResponseEntity.ok(UserByExpertise);
    }

    @GetMapping("/slot/expertise")
    public ResponseEntity<List<SlotByUserExpertise>> searchUsersByExpertise(@RequestParam String expertise) {
        List<SlotByUserExpertise> users = slotBookingService.findUsersWithSlotsByExpertise(expertise);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/bookSlot")
    public ResponseEntity<Booking> bookSlot(@RequestBody BookingRequest bookingRequest) {
        Booking newBooking = slotBookingService.bookSlot(bookingRequest);
        return ResponseEntity.ok(newBooking);
    }

    @PostMapping("/ratings")
    public ResponseEntity<Rating> addRating(@RequestBody RatingRequest ratingRequest) {
        Rating newRating = slotBookingService.addRating(ratingRequest);
        return ResponseEntity.ok(newRating);
    }
    @GetMapping("/slot/{slotid}")
    public ResponseEntity<SlotBySlotRequest> getSlotById(@PathVariable Long slotid) {
        SlotBySlotRequest slotBySlotRequest = slotBookingService.getSlotById(slotid);
        return ResponseEntity.ok(slotBySlotRequest);
    }


    @DeleteMapping("/delete/{slotid}")
    public ResponseEntity<String> deleteSlot(@PathVariable Long slotid) {
        try {
            slotBookingService.deleteSlot(slotid);
            return ResponseEntity.ok("Slot with ID " + slotid + " has been deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/cancelBookingBySlot/{slotid}")
    public ResponseEntity<String> cancelBookingBySlot(@PathVariable Long slotid) {
        try {
            slotBookingService.cancelBookingBySlot(slotid);
            return ResponseEntity.ok("Booking for slot " + slotid + " canceled successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/bookedSlot/{slotid}")
    public ResponseEntity<?> getBookedSlotById(@PathVariable Long slotid) {
        try {
            SlotBySlotRequest bookedSlotBySlotRequest = slotBookingService.getBookedSlotById(slotid);
            return ResponseEntity.ok(bookedSlotBySlotRequest);
        } catch (RuntimeException e) {
            // Handle the case where the slot is not booked
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This slot is not booked!"); // Return 404 with message
        }
    }



}
