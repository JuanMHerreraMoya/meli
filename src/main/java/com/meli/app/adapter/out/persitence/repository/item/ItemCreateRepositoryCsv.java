package com.meli.app.adapter.out.persitence.repository.item;

import com.meli.app.adapter.out.persitence.AbstractCsvRepository;
import com.meli.app.adapter.out.persitence.entity.ItemCsvEntity;
import com.meli.app.adapter.out.persitence.mapper.ItemMapper;
import com.meli.app.application.port.out.item.ItemCreatePort;
import com.meli.app.application.port.out.item.ItemUpdatePort;
import com.meli.app.domain.model.Item;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ItemCreateRepositoryCsv extends AbstractCsvRepository implements ItemCreatePort , ItemUpdatePort {
    private static final String CSV_ITEMS = "data/items.csv";
    @PostConstruct
    public void init() {
        initializerFile(CSV_ITEMS, new String[] { "id", "title", "description", "price",
                "rutaImagen", "stock", "views","rating", "sold", "seller","category", "spects"});
    }
    @Override
    public boolean createItem(Item item) {
        ItemCsvEntity itemCsvEntity = ItemMapper.itemToEntity(item);
        File file = new File(CSV_ITEMS);
        try (Writer writer = new FileWriter(file, true)) {
            ColumnPositionMappingStrategy<ItemCsvEntity> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(ItemCsvEntity.class);
            strategy.setColumnMapping("id", "title", "description", "price", "rutaImagen", "stock",
                    "views", "rating", "sold", "seller", "category", "spects");

            StatefulBeanToCsvBuilder<ItemCsvEntity> builder = new StatefulBeanToCsvBuilder<ItemCsvEntity>(writer)
                    .withMappingStrategy(strategy)
                    .withApplyQuotesToAll(false)
                    .withOrderedResults(true);

            StatefulBeanToCsv<ItemCsvEntity> beanToCsv = builder.build();
            sanitize(itemCsvEntity);
            beanToCsv.write(itemCsvEntity);
            return true;
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sanitize(ItemCsvEntity item) {
        if (item.getPrice() == null) item.setPrice(0);
        if (item.getStock() == null) item.setStock(0);
        if (item.getViews() == null) item.setViews(0);
        if (item.getRating() == null) item.setRating(0);
        if (item.getSold() == null) item.setSold(0);
        if (item.getId() == null) item.setId("");
        if (item.getTitle() == null) item.setTitle("");
        if (item.getDescription() == null) item.setDescription("");
        if (item.getRutaImagen() == null) item.setRutaImagen("");
        if (item.getSeller() == null) item.setSeller("");
        if (item.getCategory() == null) item.setCategory("");
        if (item.getSpects() == null) item.setSpects("");
    }

    @Override
    public boolean updateItem(Item updatedItem) {
        ItemCsvEntity updatedEntity = ItemMapper.itemToEntity(updatedItem);
        try (Reader reader = new FileReader(CSV_ITEMS)) {
            List<ItemCsvEntity> items = new CsvToBeanBuilder<ItemCsvEntity>(reader)
                    .withType(ItemCsvEntity.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build()
                    .parse();
            boolean updated = false;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getTitle().equalsIgnoreCase(updatedEntity.getTitle()) &&
                        items.get(i).getSeller().equalsIgnoreCase(updatedEntity.getSeller())) {
                    items.set(i, updatedEntity);
                    updated = true;
                    break;
                }
            }
            if (!updated) return false;
            try (Writer writer = new FileWriter(CSV_ITEMS, false)) {
                writer.write("id,title,description,price,rutaImagen,stock,views,rating,sold,seller,category,spects\n");

                // Usamos SOLO @CsvBindByPosition en ItemCsvEntity
                ColumnPositionMappingStrategy<ItemCsvEntity> strategy = new ColumnPositionMappingStrategy<>();
                strategy.setType(ItemCsvEntity.class);

                StatefulBeanToCsv<ItemCsvEntity> beanToCsv = new StatefulBeanToCsvBuilder<ItemCsvEntity>(writer)
                        .withMappingStrategy(strategy)
                        .withApplyQuotesToAll(false)
                        .withOrderedResults(true)
                        .build();

                for (ItemCsvEntity itemCsvEntity : items) {
                    sanitize(itemCsvEntity);
                }

                beanToCsv.write(items);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
