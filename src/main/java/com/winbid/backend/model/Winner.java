package com.winbid.backend.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
public class Winner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Associated product that the winner bid on

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who won the bid

    @Column(nullable = false)
    private LocalDateTime winDate; // Date and time when the user won

    @Column(nullable = false)
    private Double winningBidAmount; // The winning bid amount

}
