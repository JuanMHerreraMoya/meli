package com.meli.app;

import com.meli.app.adapter.out.persitence.repository.item.ItemCreateRepositoryCsv;
import com.meli.app.domain.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemCreateRepositoryCsvTest {

    private ItemCreateRepositoryCsv repository;
    private static final String CSV_FILE_PATH = "data/items.csv";
    private static final String CSV_BACKUP_PATH = "data/items.csv.bak";

    // Contenido inicial para nuestros tests
    private final String initialContent = "id,title,description,price,rutaImagen,stock,views,rating,sold,seller,category,spects\n" +
            "1,Existing Item,Old Desc,100,path,10,0,0,0,SellerA,CatA,Specs\n" +
            "2,Another Item,Desc,200,path,20,0,0,0,SellerB,CatB,Specs\n";

    @BeforeEach
    void setUp() throws IOException {

        Path originalPath = Path.of(CSV_FILE_PATH);
        if (Files.exists(originalPath)) {
            Files.copy(originalPath, Path.of(CSV_BACKUP_PATH), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createDirectories(originalPath.getParent());
        }
        Files.write(originalPath, initialContent.getBytes());
        repository = new ItemCreateRepositoryCsv();
        repository.init();
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
    void testCreateItem_Success() throws IOException {
        // Arrange
        Item newItem = new Item("3", "New Item", "Desc", 300, 30, 0, 0, 0, "path", "SellerC", "CatC", "Specs");
        // Act
        boolean result = repository.createItem(newItem);
        // Assert
        assertTrue(result);
        String content = Files.readString(Path.of(CSV_FILE_PATH));
        assertTrue(content.contains("3,New Item,Desc,300,path,30,0,0,0,SellerC,CatC,Specs"));
    }

    @Test
    void testCreateItem_WithNullFields_SuccessAndSanitized() throws IOException {
        // Arrange
        Item newItemWithNulls = new Item("4", "Test", null, 400, null, null, null, null, null, null, null, null);
        // Act
        boolean result = repository.createItem(newItemWithNulls);
        // Assert
        assertTrue(result);
        String content = Files.readString(Path.of(CSV_FILE_PATH));
        assertTrue(content.contains("4,Test,,400,,0,0,0,0,,,\n"));
    }

    @Test
    void testUpdateItem_Success() throws IOException {
        // Arrange
        Item updatedItemDomain = new Item("1", "Existing Item", "New Desc", 150, 15, 5, 0, 0, "path", "SellerA", "CatA", "Specs");
        // Act
        boolean result = repository.updateItem(updatedItemDomain);
        // Assert
        assertTrue(result);
        String content = Files.readString(Path.of(CSV_FILE_PATH));
        assertTrue(content.contains("1,Existing Item,New Desc,150,path,15,5,0,0,SellerA,CatA,Specs"));
        assertTrue(content.contains("2,Another Item,Desc,200,path,20,0,0,0,SellerB,CatB,Specs"));
    }

    @Test
    void testUpdateItem_WhenItemNotFound_ReturnsFalse() throws IOException {
        // Arrange
        Item nonExistentItem = new Item("3", "Non Existent Item", "Desc", 50, 5, 0, 0, 0, "path", "SellerC", "CatC", "Specs");
        // Act
        boolean result = repository.updateItem(nonExistentItem);
        // Assert
        assertFalse(result);
        String content = Files.readString(Path.of(CSV_FILE_PATH));
        assertTrue(content.contains("1,Existing Item,Old Desc,100,path,10,0,0,0,SellerA,CatA,Specs"));
        assertTrue(content.contains("2,Another Item,Desc,200,path,20,0,0,0,SellerB,CatB,Specs"));
        assertFalse(content.contains("Non Existent Item"));
    }
}