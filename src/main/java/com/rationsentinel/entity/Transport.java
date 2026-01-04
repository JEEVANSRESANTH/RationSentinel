package com.rationsentinel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transports")
@Getter
@Setter
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private StockAllocation stockAllocation;

    @ManyToOne
    private User transporter;

    private String vehicleNumber;

    private LocalDateTime dispatchTime;
    private LocalDateTime expectedArrivalTime;
    private LocalDateTime actualArrivalTime;
}
