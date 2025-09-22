package com.jwt.SpringSecurity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Entity
@Table(name = "slot")
public class Slot {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long slotid;

        @ManyToOne
        @JoinColumn(name = "OfferedBy_id", referencedColumnName = "userid")
        @JsonBackReference
        private UserData user;
        private LocalDate date;
        private LocalDate startdate;
        private LocalDate enddate;
        @Enumerated(EnumType.STRING)
        private DayOfWeek day;

        private LocalTime startTime;
        private LocalTime endTime;
        private String status;

//    @OneToOne(mappedBy = "slot")
//    private Booking booking;

}
