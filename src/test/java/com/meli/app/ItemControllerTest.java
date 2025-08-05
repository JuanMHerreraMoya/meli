package com.meli.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.app.adapter.in.controller.ItemController;
import com.meli.app.adapter.in.error.exception.ItemNotFoundException;
import com.meli.app.application.port.in.ItemServicePort;
import com.meli.app.domain.model.Item;
import com.meli.app.domain.model.ItemComplete;
import com.meli.app.domain.model.Seller;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemServicePort itemServicePort;
    @Mock
    private Validator validator;

    @InjectMocks
    private ItemController itemController;

    private Item item1;
    private Item item2;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        item1 = new Item();
        item1.setId("1");
        item1.setTitle("Item 1");

        item2 = new Item();
        item2.setId("2");
        item2.setTitle("Item 2");

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetItems_ReturnsListOfItems() {
        List<Item> mockItems = Arrays.asList(item1, item2);
        when(itemServicePort.getItems()).thenReturn(mockItems);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(itemServicePort, times(1)).getItems();
    }

    @Test
    void testFindById_ReturnsItemComplete() {
        String itemId = "123";
        Seller seller = Seller.builder().id("1").name("test").certified("No").initYear(new Date()).build();
        ItemComplete mockItem = new ItemComplete("123","test","1,test",1, 1,5,0,0,"test.jpg",seller,"test","test" );
        when(itemServicePort.findById(itemId)).thenReturn(Optional.of(mockItem));
        ItemComplete result = itemController.findById(itemId);
        assertNotNull(result);
        verify(itemServicePort, times(1)).findById(itemId);
    }

    @Test
    void testFindById_ThrowsItemNotFoundException() {
        String itemId = "not_exist";
        when(itemServicePort.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> itemController.findById(itemId));
        verify(itemServicePort, times(1)).findById(itemId);
    }

    @Test
    void testCreateItem_Success() throws Exception {
        // Arrange
        Item validItem = new Item("3", "New Item", "Description", 100, 10, null, 0, 0, "test.jpg", "Seller", "Category", "Specs");
        String itemJson = objectMapper.writeValueAsString(validItem);

        MockMultipartFile imagePart = new MockMultipartFile("imagen", "image.png", "image/png", "imageData".getBytes());

        when(validator.validate(any(Item.class))).thenReturn(Collections.emptySet());
        when(itemServicePort.createItemOrThrow(any(Item.class))).thenReturn(true);

        // Act
        ResponseEntity<String> response = itemController.createItem(itemJson, imagePart);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item creado/ Actualizado.", response.getBody());
        verify(itemServicePort, times(1)).createItemOrThrow(any(Item.class));
    }

    @Test
    void testCreateItem_ValidationErrors_ReturnsBadRequest() throws Exception {
        // Arrange
        Item invalidItem = new Item();
        String itemJson = objectMapper.writeValueAsString(invalidItem);
        MockMultipartFile imagePart = new MockMultipartFile("imagen", "image.png", "image/png", "imageData".getBytes());

        Set<ConstraintViolation<Item>> violations = new HashSet<>();
        violations.add(mock(ConstraintViolation.class)); // Simula una violación
        when(validator.validate(any(Item.class))).thenReturn(violations);

        // Act
        ResponseEntity<String> response = itemController.createItem(itemJson, imagePart);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Errores de validación:"));
        verify(itemServicePort, never()).createItemOrThrow(any());
    }

    @Test
    void testCreateItem_ServiceFails_ReturnsBadRequest() throws Exception {
        // Arrange
        Item validItem = new Item("3", "New Item", "Description", 100, 10, null, 0, 0, "test.jpg", "Seller", "Category", "Specs");
        String itemJson = objectMapper.writeValueAsString(validItem);
        MockMultipartFile imagePart = new MockMultipartFile("imagen", "image.png", "image/png", "imageData".getBytes());

        when(validator.validate(any(Item.class))).thenReturn(Collections.emptySet());
        when(itemServicePort.createItemOrThrow(any(Item.class))).thenReturn(false);

        // Act
        ResponseEntity<String> response = itemController.createItem(itemJson, imagePart);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error al crear el item.", response.getBody());
        verify(itemServicePort, times(1)).createItemOrThrow(any(Item.class));
    }
}