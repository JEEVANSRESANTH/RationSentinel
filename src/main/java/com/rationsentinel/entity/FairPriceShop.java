package com.rationsentinel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fair_price_shops")
@Getter
@Setter
public class FairPriceShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shopCode;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String district;
}
