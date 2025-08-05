package com.meli.app;

import com.meli.app.adapter.out.persitence.repository.seller.SellerFindRepositoryCsv;
import com.meli.app.domain.model.Seller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SellerFindRepositoryCsvTest {

    private SellerFindRepositoryCsv repository;
    private static final String CSV_FILE_PATH = "data/sellers.csv";
    private static final String CSV_BACKUP_PATH = "data/sellers.csv.bak";

    private final String initialContent = "id,name,certified,initYear\n" +
            "1,Existing Seller,Si,2016-01-01\n" +
            "2,Another Seller,No,2020-01-01\n" +
            "3,Find Me,Si,2021-01-01\n";

    @BeforeEach
    void setUp() throws IOException {
        Path originalPath = Path.of(CSV_FILE_PATH);
        Path backupPath = Path.of(CSV_BACKUP_PATH);

        if (Files.exists(backupPath)) {
            backupPath.toFile().setWritable(true);
        }

        if (Files.exists(originalPath)) {
            originalPath.toFile().setWritable(true);
            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createDirectories(originalPath.getParent());
        }

        Files.write(originalPath, initialContent.getBytes());

        repository = new SellerFindRepositoryCsv();
    }

    @AfterEach
    void tearDown() throws IOException {
        Path originalPath = Path.of(CSV_FILE_PATH);
        Path backupPath = Path.of(CSV_BACKUP_PATH);

        if (Files.exists(originalPath)) {
            originalPath.toFile().setWritable(true);
        }

        if (Files.exists(backupPath)) {
            backupPath.toFile().setWritable(true);
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath);
        } else if (Files.exists(originalPath)) {
            Files.delete(originalPath);
        }
    }

    @Test
    void testGetByName_WhenSellerExists_ReturnsCorrectSeller() {
        // Act
        Optional<Seller> sellerOptional = repository.getByName("Find Me");

        // Assert
        assertTrue(sellerOptional.isPresent());
        assertEquals("3", sellerOptional.get().getId());
        assertEquals("Find Me", sellerOptional.get().getName());
        assertEquals("Si", sellerOptional.get().getCertified());
    }

    @Test
    void testGetByName_WhenSellerDoesNotExist_ReturnsEmptyOptional() {
        // Act
        Optional<Seller> sellerOptional = repository.getByName("Nonexistent Seller");

        // Assert
        assertFalse(sellerOptional.isPresent());
    }

    @Test
    void testGetByName_WhenFileIsEmpty_ReturnsEmptyOptional() throws IOException {
        // Arrange
        Files.write(Path.of(CSV_FILE_PATH), "id,name,certified,initYear\n".getBytes());

        // Act
        Optional<Seller> sellerOptional = repository.getByName("Existing Seller");

        // Assert
        assertFalse(sellerOptional.isPresent());
    }

    @Test
    void testGetByName_WhenIOExceptionOccurs_ThrowsRuntimeException() throws IOException {
        // Arrange
        Files.delete(Path.of(CSV_FILE_PATH));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> repository.getByName("Existing Seller"));
    }
}
