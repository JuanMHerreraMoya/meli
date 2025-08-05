package com.meli.app;

import com.meli.app.adapter.in.error.exception.ItemCreationException;
import com.meli.app.application.port.out.item.ItemCreatePort;
import com.meli.app.application.port.out.item.ItemFindPort;
import com.meli.app.application.port.out.item.ItemUpdatePort;
import com.meli.app.application.port.out.seller.SellerFindPort;
import com.meli.app.application.service.ItemServicePortImpl;
import com.meli.app.domain.model.Item;
import com.meli.app.domain.model.ItemComplete;
import com.meli.app.domain.model.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServicePortImplTest {

    @Mock
    private ItemCreatePort itemCreatePort;
    @Mock
    private SellerFindPort sellerFindPort;
    @Mock
    private ItemFindPort itemFindPort;
    @Mock
    private ItemUpdatePort itemUpdatePort;

    @InjectMocks
    private ItemServicePortImpl itemServicePort;

    private Item newItem;
    private Seller existingSeller;

    @BeforeEach
    void setUp() {
        newItem = new Item();
        newItem.setTitle("Test Item");
        newItem.setSeller("Test Seller");

        existingSeller = new Seller();
        existingSeller.setName("Test Seller");
    }


    @Test
    void testCreateItemOrThrow_WhenItemIsCreatedSuccessfully_ReturnsTrue() {
        // Arrange
        when(sellerFindPort.getByName("Test Seller")).thenReturn(Optional.of(existingSeller));
        when(itemFindPort.findByNameAndSeller("Test Item", "Test Seller")).thenReturn(Optional.empty());
        when(itemCreatePort.createItem(any(Item.class))).thenReturn(true);

        // Act
        boolean result = itemServicePort.createItemOrThrow(newItem);

        // Assert
        assertTrue(result);
        verify(sellerFindPort, times(1)).getByName("Test Seller");
        verify(itemFindPort, times(1)).findByNameAndSeller("Test Item", "Test Seller");
        verify(itemCreatePort, times(1)).createItem(newItem);
        verifyNoInteractions(itemUpdatePort);
    }

    @Test
    void testGetItems_ReturnsListOfItems() {
        // Arrange
        List<Item> mockItems = Arrays.asList(newItem);
        when(itemFindPort.findItems()).thenReturn(mockItems);

        // Act
        List<Item> result = itemServicePort.getItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getTitle());
        verify(itemFindPort, times(1)).findItems();
    }

    @Test
    void testGetItems_ReturnsEmptyList() {
        // Arrange
        when(itemFindPort.findItems()).thenReturn(Collections.emptyList());

        // Act
        List<Item> result = itemServicePort.getItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemFindPort, times(1)).findItems();
    }

    @Test
    void testFindById_WhenItemIsFound_ReturnsItemCompleteAndUpdatesViews() {
        // Arrange
        String itemId = "123";
        Item foundItem = new Item();
        foundItem.setId(itemId);
        foundItem.setViews(10);
        foundItem.setSeller("Test Seller");

        when(itemFindPort.findById(itemId)).thenReturn(Optional.of(foundItem));
        when(itemUpdatePort.updateItem(any(Item.class))).thenReturn(true);
        when(sellerFindPort.getByName("Test Seller")).thenReturn(Optional.of(existingSeller));

        // Act
        Optional<ItemComplete> result = itemServicePort.findById(itemId);

        // Assert
        assertTrue(result.isPresent());
        ItemComplete itemComplete = result.get();
        assertEquals(itemId, itemComplete.getId());
        assertEquals(11, itemComplete.getViews());

        verify(itemFindPort, times(1)).findById(itemId);
        verify(itemUpdatePort, times(1)).updateItem(argThat(item -> item.getViews() == 11));
        verify(sellerFindPort, times(1)).getByName("Test Seller");
    }

    @Test
    void testFindById_WhenItemIsNotFound_ReturnsEmptyOptional() {
        // Arrange
        String itemId = "not_found";
        when(itemFindPort.findById(itemId)).thenReturn(Optional.empty());

        // Act
        Optional<ItemComplete> result = itemServicePort.findById(itemId);

        // Assert
        assertFalse(result.isPresent());

        verify(itemFindPort, times(1)).findById(itemId);
        verifyNoInteractions(itemUpdatePort);
        verifyNoInteractions(sellerFindPort);
    }

    @Test
    void testCreateItemOrThrow_WhenSellerDoesNotExist_ThrowsException() {
        // Arrange
        when(sellerFindPort.getByName("Test Seller")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemCreationException.class, () -> itemServicePort.createItemOrThrow(newItem));

        verify(sellerFindPort, times(1)).getByName("Test Seller");
        verify(itemFindPort, times(1)).findByNameAndSeller(newItem.getTitle(), newItem.getSeller());
        verifyNoInteractions(itemCreatePort);
        verifyNoInteractions(itemUpdatePort);
    }

    @Test
    void testCreateItemOrThrow_WhenCreationFails_ThrowsException() {
        // Arrange
        when(sellerFindPort.getByName("Test Seller")).thenReturn(Optional.of(existingSeller));
        when(itemFindPort.findByNameAndSeller("Test Item", "Test Seller")).thenReturn(Optional.empty());
        when(itemCreatePort.createItem(any(Item.class))).thenReturn(false);

        // Act & Assert
        assertThrows(ItemCreationException.class, () -> itemServicePort.createItemOrThrow(newItem));

        // Verify interactions
        verify(sellerFindPort, times(1)).getByName("Test Seller");
        verify(itemFindPort, times(1)).findByNameAndSeller("Test Item", "Test Seller");
        verify(itemCreatePort, times(1)).createItem(newItem);
        verifyNoInteractions(itemUpdatePort);
    }

    @Test
    void testCreateItemOrThrow_WhenUpdateFails_ThrowsException() {
        // Arrange
        Item existingItem = new Item();
        existingItem.setTitle("Test Item");
        existingItem.setSeller("Test Seller");

        when(sellerFindPort.getByName("Test Seller")).thenReturn(Optional.of(existingSeller));
        when(itemFindPort.findByNameAndSeller("Test Item", "Test Seller")).thenReturn(Optional.of(existingItem));
        when(itemUpdatePort.updateItem(any(Item.class))).thenReturn(false);

        // Act & Assert
        assertThrows(ItemCreationException.class, () -> itemServicePort.createItemOrThrow(newItem));

        // Verify interactions
        verify(sellerFindPort, times(1)).getByName("Test Seller");
        verify(itemFindPort, times(1)).findByNameAndSeller("Test Item", "Test Seller");
        verify(itemUpdatePort, times(1)).updateItem(newItem);
        verifyNoInteractions(itemCreatePort);
    }
}
