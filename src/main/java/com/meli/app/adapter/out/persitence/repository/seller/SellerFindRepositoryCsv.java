package com.meli.app.adapter.out.persitence.repository.seller;

import com.meli.app.adapter.out.persitence.entity.SellerCsvEntity;
import com.meli.app.adapter.out.persitence.mapper.SellerMapper;
import com.meli.app.application.port.out.seller.SellerFindPort;
import com.meli.app.domain.model.Seller;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

@Service
public class SellerFindRepositoryCsv implements SellerFindPort {
    private static final String CSV_SELLERS = "data/sellers.csv";


    @Override
    public Optional<Seller> getByName(String name) {
        try (Reader reader = new FileReader(CSV_SELLERS)) {
            CsvToBean<SellerCsvEntity> csvToBean = new CsvToBeanBuilder<SellerCsvEntity>(reader)
                    .withType(SellerCsvEntity.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<SellerCsvEntity> sellers = csvToBean.parse();

            Optional<SellerCsvEntity> sellerCsvEntity = sellers.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(name)).findFirst();
            return sellerCsvEntity.map(SellerMapper::sellerEntityToDto);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer sellers.csv", e);
        }
    }
}
