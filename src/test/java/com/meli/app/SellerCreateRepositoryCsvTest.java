package com.meli.app;


import com.meli.app.adapter.out.persitence.repository.seller.SellerCreateRepositoryCsv;
import com.meli.app.domain.model.Seller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SellerCreateRepositoryCsvTest {

    private SellerCreateRepositoryCsv repository;
    private static final String CSV_FILE_PATH = "data/sellers.csv";
    private static final String CSV_BACKUP_PATH = "data/sellers.csv";

    private final String initialHeader = "id,name,certified,initYear\n";

    @BeforeEach
    void setUp() throws IOException {
        Path originalPath = Path.of(CSV_FILE_PATH);

        if (Files.exists(originalPath)) {
            originalPath.toFile().setWritable(true);
            Files.copy(originalPath, Path.of(CSV_BACKUP_PATH), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createDirectories(originalPath.getParent());
        }
        Files.write(originalPath, initialHeader.getBytes());
        repository = new SellerCreateRepositoryCsv();
        repository.init();
    }

    @AfterEach
    void tearDown() throws IOException {
        Path originalPath = Path.of(CSV_FILE_PATH);
        Path backupPath = Path.of(CSV_BACKUP_PATH);
        if (Files.exists(originalPath)) {
            originalPath.toFile().setWritable(true);
        }

        if (Files.exists(backupPath)) {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath);
        } else if (Files.exists(originalPath)) {
            Files.delete(originalPath);
        }
    }


    @Test
    void testCreateSeller_WhenSellerIsNew_ReturnsTrueAndAppendsToFile() throws IOException {
        // Arrange
        Seller newSeller = Seller.builder().id("1").name("New Seller").certified("Si").initYear(new Date()).build();

        // Act
        boolean result = repository.createSeller(newSeller);

        // Assert
        assertTrue(result);
        String content = Files.readString(Path.of(CSV_FILE_PATH));
        assertTrue(content.contains("1,New Seller,Si"));
        assertTrue(content.startsWith(initialHeader));
    }

    @Test
    void testCreateSeller_WhenExceptionOccurs_ThrowsRuntimeException() throws IOException {
        // Arrange
        Seller newSeller = Seller.builder().id("2").name("Problematic Seller").build();

        Path csvPath = Path.of(CSV_FILE_PATH);
        csvPath.toFile().setReadOnly();

        // Act & Assert
        try {
            assertThrows(RuntimeException.class, () -> repository.createSeller(newSeller));
        } finally {
            csvPath.toFile().setWritable(true);
        }
    }
}
