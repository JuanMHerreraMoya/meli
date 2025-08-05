package com.meli.app.adapter.out.persitence.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class SellerCsvEntity {
    @CsvBindByName(column = "id")
    private String id;
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "certified")
    private String certified;
    @CsvBindByName(column = "initYear")
    private String initYear;


    public SellerCsvEntity() {}

    public SellerCsvEntity(String id, String name, String certified, String initYear) {
        this.id = id;
        this.name = name;
        this.certified = certified;
        this.initYear = initYear;
    }
}
