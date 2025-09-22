package com.jwt.SpringSecurity.service;

import com.jwt.SpringSecurity.dto.*;
import com.jwt.SpringSecurity.model.Booking;
import com.jwt.SpringSecurity.model.Rating;
import com.jwt.SpringSecurity.model.Slot;
import com.jwt.SpringSecurity.model.UserData;
import com.jwt.SpringSecurity.repo.BookingRepo;
import com.jwt.SpringSecurity.repo.RatingRepo;
import com.jwt.SpringSecurity.repo.SlotRepo;
import com.jwt.SpringSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlotBookingService {

    @Autowired
    BookingRepo bookingRepo;
    @Autowired
    RatingRepo ratingRepo;
    @Autowired
    SlotRepo slotRepo;
    @Autowired
    UserRepo userRepo;

        @Autowired
        private AuthenticationManager authMannager;

        @Autowired
        private UserRepo repo;

        @Autowired
        private JWTService jwtService;

        private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

        public UserData register(UserData userData){
            userData.setPassword(encoder.encode(userData.getPassword()));
            repo.save(userData);
            return userData;
        }

        public String verify(UserData userData) {
            Authentication authentication=
                    authMannager.authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(),userData.getPassword()));
            if(authentication.isAuthenticated()) {
                return jwtService.generateToken(userData.getUsername());
            }else {
                return "fail";
            }
        }


    private void validateSlotTimes(LocalTime startTime, LocalTime endTime) {


        if (endTime.isBefore(startTime) || startTime.equals(endTime)) {
            throw new RuntimeException("End time must be after start time");
        }
    }

    public List<Slot> createSlotsForMultipleDays(SlotByDataRange slotByDataRange) {
        UserData user = userRepo.findById(Math.toIntExact(slotByDataRange.getUserid()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate startDate = slotByDataRange.getStartdate();
        LocalDate endDate = slotByDataRange.getEnddate();
        LocalTime startTime = slotByDataRange.getStarttime();
        LocalTime endTime = slotByDataRange.getEndtime();

        // Validate the time range
        validateSlotTimes(startTime, endTime);

        List<Slot> createdSlots = new ArrayList<>();

        // Iterate over each day in the date range
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {

            LocalTime currentTime = startTime;

            // Create slots for each hour within the time range for the current day
            while (currentTime.isBefore(endTime)) {
                LocalTime nextTime = currentTime.plusHours(1);

                // Validate if this slot already exists for the user on the current day
                if (!slotRepo.findByUserAndDateAndStartTimeAndEndTime(user, currentDate, currentTime, nextTime).isEmpty()) {
                    throw new RuntimeException("A slot from " + currentTime + " to " + nextTime + " already exists for this user on " + currentDate);
                }

                // Create a new slot for the current hour on the current day
                Slot slot = new Slot();
                slot.setUser(user);
                slot.setDate(currentDate);
                slot.setStartdate(startDate);
                slot.setEnddate(endDate);
                slot.setDay(currentDate.getDayOfWeek());
                slot.setStartTime(currentTime);
                slot.setEndTime(nextTime);
                slot.setStatus("AVAILABLE");

                createdSlots.add(slotRepo.save(slot));

                // Move to the next hour
                currentTime = nextTime;
            }

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return createdSlots;
    }

    public List<UserData>findUserWithExpertise(String expertise){
        return userRepo.findByExpertise(expertise);
    }

    public List<SlotByUserExpertise> findUsersWithSlotsByExpertise(String expertise) {
        return slotRepo.findUsersAndSlotsByExpertise(expertise);
    }
    public Booking bookSlot(BookingRequest bookingRequest) {
        // Find the user by ID
        UserData user = userRepo.findById(Math.toIntExact(bookingRequest.getUserid()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the slot by ID
        Slot slot = slotRepo.findById(bookingRequest.getSlotid())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // Update the slot status to "BOOKED"
        slot.setStatus("BOOKED");
        slotRepo.save(slot); // Save the updated slot

        // Create a new Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSlot(slot);

        return bookingRepo.save(booking); // Save and return the booking
    }

    public Rating addRating(RatingRequest ratingRequest) {
        // Create a new Rating object
        Rating rating = new Rating();
        rating.setComment(ratingRequest.getComment());

        // Find the booking by ID
        Optional<Booking> bookingOptional = bookingRepo.findById(ratingRequest.getBookingid());
        if (!bookingOptional.isPresent()) {
            throw new RuntimeException("Booking not found with ID: " + ratingRequest.getBookingid());
        }
        rating.setBooking(bookingOptional.get());

        // Find the user by ID and associate it if necessary
        if (ratingRequest.getUserid() != null) {
            Optional<UserData> userOptional = userRepo.findById(Math.toIntExact(ratingRequest.getUserid()));
            userOptional.ifPresent(user -> {});
        }

        // Save and return the rating
        return ratingRepo.save(rating);
    }

    public SlotBySlotRequest getSlotById(Long slotid) {
        Slot slot = slotRepo.findById(slotid)
                .orElseThrow(() -> new RuntimeException("Slot not found with id " + slotid));

        SlotBySlotRequest slotDTO = new SlotBySlotRequest();
        slotDTO.setSlotid(slot.getSlotid());
        slotDTO.setDate(slot.getDate().toString());
        slotDTO.setDay(slot.getDate().getDayOfWeek().toString());
        slotDTO.setStartTime(slot.getStartTime().toString());
        slotDTO.setEndTime(slot.getEndTime().toString());

        Optional<Booking> booking = bookingRepo.findBySlotId(slotid);
        if (booking.isPresent()) {
            slotDTO.setUsername(booking.get().getUser().getUsername());
            slotDTO.setStatus(slot.getStatus().toString());
        } else {
            slotDTO.setUsername(slot.getUser().getUsername());
            slotDTO.setStatus(slot.getStatus().toString());
        }

        return slotDTO;
    }


    public void deleteSlot(Long slotid) {
        // Check if the slot exists
        if (!slotRepo.existsById(slotid)) {
            throw new RuntimeException("Slot not found with ID: " + slotid);
        }

        // Delete the slot
        slotRepo.deleteById(slotid);
    }

    public void cancelBookingBySlot(Long slotid) {
        // Find the slot by ID
        Slot slot = slotRepo.findById(slotid)
                .orElseThrow(() -> new RuntimeException("Slot not found with ID: " + slotid));

        // Find the booking associated with the slot
        Optional<Booking> bookingOptional = bookingRepo.findBySlot(slot);
        if (!bookingOptional.isPresent()) {
            throw new RuntimeException("No booking found for slot ID: " + slotid);
        }

        Booking booking = bookingOptional.get();
        // Check if the slot is already available
        if (slot.getStatus().equals("AVAILABLE")) {
            throw new RuntimeException("This booking is already canceled or the slot is available.");
        }

        // Set the slot status back to available
        slot.setStatus("AVAILABLE");

        // Save the updated slot
        slotRepo.save(slot);

        // Optionally, delete the booking or update its status
        bookingRepo.delete(booking);
    }

    public SlotBySlotRequest getBookedSlotById(Long slotid) {
        Slot slot = slotRepo.findById(slotid)
                .orElseThrow(() -> new RuntimeException("Slot not found with id " + slotid));

        SlotBySlotRequest slotDTO = new SlotBySlotRequest();
        slotDTO.setSlotid(slot.getSlotid());
        slotDTO.setDate(slot.getDate().toString());
        slotDTO.setDay(slot.getDate().getDayOfWeek().toString());
        slotDTO.setStartTime(slot.getStartTime().toString());
        slotDTO.setEndTime(slot.getEndTime().toString());


        Optional<Booking> booking = bookingRepo.findBySlotId(slotid);
        if (booking.isPresent())
        {
            slotDTO.setUsername(booking.get().getUser().getUsername());
            slotDTO.setStatus(slot.getStatus().toString());
        } else {
            slotDTO.setUsername(booking.get().getUser().getUsername()); //username in the booking
            slotDTO.setStatus(slot.getStatus().toString());
        }
        return slotDTO;
    }

}
