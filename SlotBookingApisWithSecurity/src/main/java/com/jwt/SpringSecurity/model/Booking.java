package com.jwt.SpringSecurity.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "booking")
public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long bookingid;

        // Booking belongs to a user
        @ManyToOne
        @JoinColumn(name = "BookedByUser_id", referencedColumnName = "userid")
        private UserData user;

        // Booking belongs to one slot (One-to-One)
        @OneToOne
        @JoinColumn(name = "BookedSlot_id", referencedColumnName = "slotid")
        private Slot slot;

        @OneToMany(mappedBy = "booking",cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Rating> ratings;


}
