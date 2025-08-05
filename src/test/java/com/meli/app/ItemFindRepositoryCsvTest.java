package com.meli.app;

import com.meli.app.adapter.out.persitence.repository.item.ItemFindRepositoryCsv;
import com.meli.app.domain.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemFindRepositoryCsvTest {

    private ItemFindRepositoryCsv repository;
    private static final String CSV_FILE_PATH = "data/items.csv";
    private static final String CSV_BACKUP_PATH = "data/items.csv.bak";

    private final String initialContent = "id,title,description,price,rutaImagen,stock,views,rating,sold,seller,category,spects\n" +
            "1,Existing Item,Old Desc,100,path,10,0,0,0,SellerA,CatA,Specs\n" +
            "2,Another Item,Desc,200,path,20,0,0,0,SellerB,CatB,Specs\n" +
            "3,Item for search,Find me,500,path,5,0,0,0,SellerA,CatC,Specs\n";

    @BeforeEach
    void setUp() throws IOException {
        Path originalPath = Path.of(CSV_FILE_PATH);

        if (Files.exists(originalPath)) {
            Files.copy(originalPath, Path.of(CSV_BACKUP_PATH), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createDirectories(originalPath.getParent());
        }

        Files.write(originalPath, initialContent.getBytes());
        repository = new ItemFindRepositoryCsv();
    }

    @AfterEach
    void tearDown() throws IOException {
        Path originalPath = Path.of(CSV_FILE_PATH);
        Path backupPath = Path.of(CSV_BACKUP_PATH);
        if (Files.exists(backupPath)) {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath);
        } else {
            Files.delete(originalPath);
        }
    }


    @Test
    void testFindItems_WhenItemsExist_ReturnsAllItems() {
        // Act
        List<Item> items = repository.findItems();

        // Assert
        assertNotNull(items);
        assertEquals(3, items.size());
        assertEquals("Existing Item", items.get(0).getTitle());
        assertEquals("Another Item", items.get(1).getTitle());
    }

    @Test
    void testFindItems_WhenFileIsEmpty_ReturnsEmptyList() throws IOException {
        // Arrange
        Files.write(Path.of(CSV_FILE_PATH), "id,title,description,price,rutaImagen,stock,views,rating,sold,seller,category,spects\n".getBytes());

        // Act
        List<Item> items = repository.findItems();

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void testFindItems_ThrowsRuntimeExceptionOnIOException() throws IOException {
        // Arrange
        Files.delete(Path.of(CSV_FILE_PATH)); // Eliminamos el archivo para forzar un IOException

        // Act & Assert
        assertThrows(RuntimeException.class, () -> repository.findItems());
    }


    @Test
    void testFindById_WhenItemExists_ReturnsCorrectItem() {
        // Act
        Optional<Item> itemOptional = repository.findById("2");

        // Assert
        assertTrue(itemOptional.isPresent());
        assertEquals("2", itemOptional.get().getId());
        assertEquals("Another Item", itemOptional.get().getTitle());
    }

    @Test
    void testFindById_WhenItemDoesNotExist_ReturnsEmptyOptional() {
        // Act
        Optional<Item> itemOptional = repository.findById("999");

        // Assert
        assertFalse(itemOptional.isPresent());
    }

    @Test
    void testFindById_ThrowsRuntimeExceptionOnIOException() throws IOException {
        // Arrange
        Files.delete(Path.of(CSV_FILE_PATH));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> repository.findById("1"));
    }
    @Test
    void testFindByNameAndSeller_WhenItemExists_ReturnsCorrectItem() {
        // Act
        Optional<Item> itemOptional = repository.findByNameAndSeller("Item for search", "SellerA");

        // Assert
        assertTrue(itemOptional.isPresent());
        assertEquals("3", itemOptional.get().getId());
        assertEquals("Item for search", itemOptional.get().getTitle());
    }

    @Test
    void testFindByNameAndSeller_WhenItemDoesNotExist_ReturnsEmptyOptional() {
        // Act
        Optional<Item> itemOptional = repository.findByNameAndSeller("Nonexistent", "SellerA");

        // Assert
        assertFalse(itemOptional.isPresent());
    }

    @Test
    void testFindByNameAndSeller_ReturnsEmptyOptionalOnIOException() throws IOException {
        // Arrange
        Files.delete(Path.of(CSV_FILE_PATH));

        // Act
        Optional<Item> itemOptional = repository.findByNameAndSeller("Item for search", "SellerA");

        // Assert
        assertFalse(itemOptional.isPresent());
    }
}
