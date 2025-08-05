package com.meli.app.domain.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Builder
@Data
public class Item {

    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @Min(0)
    private Integer price;
    private Integer stock;
    private Integer views;
    private Integer rating;
    private Integer sold;
    @NotBlank
    private String imagen;
    @NotBlank
    private String seller;
    @NotBlank
    private String category;
    @NotBlank
    private String spects;
    public Item(){}
    public Item(String id,String title, String description, Integer price, Integer stock, Integer views,Integer rating,
                Integer sold, String imagen, String seller,String category, String spects) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.views = views;
        this.rating = rating;
        this.sold = sold;
        this.imagen = imagen;
        this.seller = seller;
        this.category = category;
        this.spects = spects;
    }
    public static Item from(Item incoming) {
        Item item = new Item();
        item.id = incoming.id != null ? incoming.id : UUID.randomUUID().toString();
        item.title = incoming.title;
        item.description = incoming.description;
        item.price = incoming.price;
        item.stock = incoming.stock != null ? incoming.stock : 1;
        item.rating = incoming.rating != null ? incoming.rating : 5;
        item.views = incoming.views != null ? incoming.views : 0;
        item.sold = incoming.sold != null ? incoming.sold : 0;
        item.imagen = incoming.imagen;
        item.seller = incoming.seller;
        item.category = incoming.category;
        item.spects = incoming.spects;
        return item;
    }
}
