package com.meli.app.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemComplete {
    private String id;
    private String title;
    private String description;
    private Integer price;
    private Integer stock;
    private Integer views;
    private Integer rating;
    private Integer sold;
    private String imagen;
    private Seller seller;
    private String category;
    private String spects;

    public ItemComplete(String id, String title, String description, Integer price, Integer stock, Integer views, Integer rating, Integer sold, String imagen, Seller seller, String category, String spects) {
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
}
