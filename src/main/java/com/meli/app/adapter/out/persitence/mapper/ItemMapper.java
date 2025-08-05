package com.meli.app.adapter.out.persitence.mapper;

import com.meli.app.adapter.out.persitence.entity.ItemCsvEntity;
import com.meli.app.domain.model.Item;

public class ItemMapper {
    public static ItemCsvEntity itemToEntity(Item item){
        return ItemCsvEntity.builder().id(item.getId())
        .title(item.getTitle()).description(item.getDescription())
                .price(item.getPrice()).stock(item.getStock()).views(item.getViews())
                .rating(item.getRating()).sold(item.getSold())
                .rutaImagen(item.getImagen()).seller(item.getSeller())
                .category(item.getCategory())
                .spects(item.getSpects())
                .build();
    }

    public static Item entityToItem(ItemCsvEntity itemCsvEntity){
        return Item.builder()
                .id(itemCsvEntity.getId())
                .title(itemCsvEntity.getTitle())
                .description(itemCsvEntity.getDescription())
                .price(itemCsvEntity.getPrice())
                .stock(itemCsvEntity.getStock())
                .views(itemCsvEntity.getViews())
                .rating(itemCsvEntity.getRating())
                .sold(itemCsvEntity.getSold())
                .imagen(itemCsvEntity.getRutaImagen())
                .seller(itemCsvEntity.getSeller())
                .category(itemCsvEntity.getCategory())
                .spects(itemCsvEntity.getSpects())
                .build();
    }
}
