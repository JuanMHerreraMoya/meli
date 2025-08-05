package com.meli.app.adapter.out.persitence.entity;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemCsvEntity {
    @CsvBindByPosition(position = 0)
    private String id;

    @CsvBindByPosition(position = 1)
    private String title;

    @CsvBindByPosition(position = 2)
    private String description;

    @CsvBindByPosition(position = 3)
    private Integer price;

    @CsvBindByPosition(position = 4)
    private String rutaImagen;

    @CsvBindByPosition(position = 5)
    private Integer stock;

    @CsvBindByPosition(position = 6)
    private Integer views;

    @CsvBindByPosition(position = 7)
    private Integer rating;

    @CsvBindByPosition(position = 8)
    private Integer sold;

    @CsvBindByPosition(position = 9)
    private String seller;

    @CsvBindByPosition(position = 10)
    private String category;

    @CsvBindByPosition(position = 11)
    private String spects;
    public ItemCsvEntity(){}
    public ItemCsvEntity(String id, String title, String description, Integer price, String rutaImagen, Integer stock, Integer views,Integer rating, Integer sold, String seller, String category, String spects) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.rutaImagen = rutaImagen;
        this.stock = stock;
        this.views = views;
        this.rating = rating;
        this.sold = sold;
        this.seller = seller;
        this.category = category;
        this.spects = spects;
    }
}
