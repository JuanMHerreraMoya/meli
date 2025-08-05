package com.meli.app.adapter.out.persitence.mapper;

import com.meli.app.adapter.out.persitence.entity.SellerCsvEntity;
import com.meli.app.domain.model.Seller;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Component
public class SellerMapper {
    public static Seller sellerEntityToDto(SellerCsvEntity sellerCsvEntity){
        return Seller.builder().id(sellerCsvEntity.getId())
                .name(sellerCsvEntity.getName()).certified(sellerCsvEntity.getCertified())
                .initYear(Date.valueOf(sellerCsvEntity.getInitYear())).build();
    }

    public static SellerCsvEntity sellerToEntity(Seller seller){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(seller.getInitYear());

        return SellerCsvEntity.builder()
                .id(seller.getId())
                .name(seller.getName())
                .certified(seller.getCertified())
                .initYear(dateString)
                .build();
    }

}
