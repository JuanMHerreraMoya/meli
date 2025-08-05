package com.meli.app.adapter.out.persitence.repository.item;

import com.meli.app.adapter.out.persitence.entity.ItemCsvEntity;
import com.meli.app.adapter.out.persitence.mapper.ItemMapper;
import com.meli.app.application.port.out.item.ItemFindPort;
import com.meli.app.domain.model.Item;
import com.meli.app.domain.model.Seller;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemFindRepositoryCsv implements ItemFindPort  {
    private static final String CSV_ITEMS = "data/items.csv";

    @Override
    public Optional<Item> findByNameAndSeller(String title, String seller) {
        try (Reader reader = new FileReader(CSV_ITEMS)) {
            CsvToBean<ItemCsvEntity> csvToBean = new CsvToBeanBuilder<ItemCsvEntity>(reader)
                    .withType(ItemCsvEntity.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();
            List<ItemCsvEntity> items = csvToBean.parse();

            return items.stream()
                    .filter(i -> i.getTitle() != null && i.getSeller() != null &&
                            i.getTitle().equalsIgnoreCase(title) &&
                            i.getSeller().equalsIgnoreCase(seller))
                    .findFirst()
                    .map(ItemMapper::entityToItem);

        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findItems() {
        try (Reader reader = new FileReader(CSV_ITEMS)) {
            CsvToBean<ItemCsvEntity> csvToBean = new CsvToBeanBuilder<ItemCsvEntity>(reader)
                    .withType(ItemCsvEntity.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();
            List<ItemCsvEntity> items = csvToBean.parse();
            return items.stream().map(ItemMapper::entityToItem).toList();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Item> findById(String id) {
        try (Reader reader = new FileReader(CSV_ITEMS)) {
            CsvToBean<ItemCsvEntity> csvToBean = new CsvToBeanBuilder<ItemCsvEntity>(reader)
                    .withType(ItemCsvEntity.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();
            List<ItemCsvEntity> items = csvToBean.parse();
            return items.stream()
                    .filter(i->i.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .map(ItemMapper::entityToItem);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
