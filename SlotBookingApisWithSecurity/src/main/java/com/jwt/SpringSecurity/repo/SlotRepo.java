package com.jwt.SpringSecurity.repo;


import com.jwt.SpringSecurity.dto.SlotByUserExpertise;
import com.jwt.SpringSecurity.model.Slot;
import com.jwt.SpringSecurity.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;

@Repository
public interface SlotRepo extends JpaRepository<Slot,Long> {


    // Fetching both user and slot details by expertise
    @Query("SELECT new com.jwt.SpringSecurity.dto.SlotByUserExpertise( " +
            "u.userid, u.username, u.email, u.expertise, u.position, s.slotid, s.date, s.day, s.startTime, s.endTime, s.status) " +
            "FROM Slot s JOIN s.user u WHERE u.expertise = ?1")
    List<SlotByUserExpertise> findUsersAndSlotsByExpertise(String expertise);


    List<Slot> findByUserAndDateAndStartTimeAndEndTime(UserData user, LocalDate date, LocalTime startTime, LocalTime endTime);
}
