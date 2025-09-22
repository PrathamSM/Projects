package com.jwt.SpringSecurity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "rating")
public class Rating {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long ratingid;

        // Rating belongs to a booking (Many-to-One)
        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "RatedByBooking_id", referencedColumnName = "Bookingid")
        private Booking booking;

        private String comment;

}
