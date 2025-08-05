package com.meli.app.adapter.out.persitence;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public abstract class AbstractCsvRepository {
    protected void initializerFile(String path, String[] headers) {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                writer.writeNext(headers);
            } catch (IOException e) {
                throw new RuntimeException("Error al crear CSV", e);
            }
        }
    }
}
