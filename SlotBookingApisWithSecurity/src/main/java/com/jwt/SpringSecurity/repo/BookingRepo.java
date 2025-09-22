package com.jwt.SpringSecurity.repo;


import com.jwt.SpringSecurity.model.Booking;
import com.jwt.SpringSecurity.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking,Long> {
    @Query("SELECT b FROM Booking b WHERE b.slot.slotid = :slotid")
    Optional<Booking> findBySlotId(@Param("slotid") Long slotid);

    Optional<Booking>findBySlot(Slot slot);
}
