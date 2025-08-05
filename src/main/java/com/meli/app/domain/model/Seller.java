package com.meli.app.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Seller {
    private String id;
    @NotBlank
    private String name;
    private String certified;
    private Date initYear;
    public Seller(){}

    public Seller(String id, String name, String certified, Date initYear) {
        this.id = id;
        this.name = name;
        this.certified = certified;
        this.initYear = initYear;
    }

    public static Seller from(Seller incoming) {
        Seller seller = new Seller();
        seller.id = incoming.id != null ? incoming.id : UUID.randomUUID().toString();
        seller.name = incoming.getName();
        seller.certified = incoming.certified != null ? incoming.certified : "No";
        seller.initYear = incoming.initYear != null ? incoming.initYear : new Date();
        return seller;
    }
}

