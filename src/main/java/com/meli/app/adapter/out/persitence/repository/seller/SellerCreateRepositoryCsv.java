package com.meli.app.adapter.out.persitence.repository.seller;

import com.meli.app.adapter.out.persitence.AbstractCsvRepository;
import com.meli.app.adapter.out.persitence.entity.SellerCsvEntity;
import com.meli.app.adapter.out.persitence.mapper.SellerMapper;
import com.meli.app.application.port.out.seller.SellerCreatePort;
import com.meli.app.domain.model.Seller;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

@Service
public class SellerCreateRepositoryCsv extends AbstractCsvRepository implements SellerCreatePort {
    private static final String CSV_SELLERS = "data/sellers.csv";
    @PostConstruct
    public void init() {
        initializerFile(CSV_SELLERS, new String[] {"id", "name", "certified", "initYear"});
    }

    @Override
    public boolean createSeller(Seller seller) {
        SellerCsvEntity sellerCsvEntity = SellerMapper.sellerToEntity(seller);
        File file = new File(CSV_SELLERS);
        try (Writer writer = new FileWriter(file, true)) {
            ColumnPositionMappingStrategy<SellerCsvEntity> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(SellerCsvEntity.class);
            strategy.setColumnMapping("id", "name", "certified", "initYear");

            StatefulBeanToCsv<SellerCsvEntity> beanToCsv =
                    new StatefulBeanToCsvBuilder<SellerCsvEntity>(writer)
                            .withMappingStrategy(strategy)
                            .withApplyQuotesToAll(false)
                            .withOrderedResults(false)
                            .build();

            beanToCsv.write(sellerCsvEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al escribir el item en el CSV", e);
        }
    }
}
