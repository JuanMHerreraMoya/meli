package com.meli.app;

import com.meli.app.adapter.in.error.exception.SellerCreationException;
import com.meli.app.application.port.out.seller.SellerCreatePort;
import com.meli.app.application.port.out.seller.SellerFindPort;
import com.meli.app.application.service.SellerServicePortImpl;
import com.meli.app.domain.model.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServicePortImplTest {

    @Mock
    private SellerCreatePort sellerCreatePort;
    @Mock
    private SellerFindPort sellerFindPort;

    @InjectMocks
    private SellerServicePortImpl sellerServicePort;

    private Seller newSeller;
    private Seller existingSeller;

    @BeforeEach
    void setUp() {
        newSeller = Seller.builder().name("New Seller").build();
        existingSeller = Seller.builder().name("Existing Seller").build();
    }

    @Test
    void testCreateSeller_WhenSellerDoesNotExistAndCreationSucceeds_ReturnsTrue() {
        // Arrange
        when(sellerFindPort.getByName("New Seller")).thenReturn(Optional.empty());
        when(sellerCreatePort.createSeller(newSeller)).thenReturn(true);

        // Act
        boolean result = sellerServicePort.createSeller(newSeller);

        // Assert
        assertTrue(result);
        verify(sellerFindPort, times(1)).getByName("New Seller");
        verify(sellerCreatePort, times(1)).createSeller(newSeller);
    }

    @Test
    void testCreateSeller_WhenSellerAlreadyExists_ReturnsTrueAndDoesNotCreate() {
        // Arrange
        when(sellerFindPort.getByName("Existing Seller")).thenReturn(Optional.of(existingSeller));

        // Act
        boolean result = sellerServicePort.createSeller(existingSeller);

        // Assert
        assertTrue(result);
        verify(sellerFindPort, times(1)).getByName("Existing Seller");
        verifyNoInteractions(sellerCreatePort);
    }

    @Test
    void testCreateItemOrThrow_WhenCreationSucceeds_ReturnsTrue() {
        // Arrange
        when(sellerFindPort.getByName("New Seller")).thenReturn(Optional.empty());
        when(sellerCreatePort.createSeller(newSeller)).thenReturn(true);

        // Act
        boolean result = sellerServicePort.createItemOrThrow(newSeller);

        // Assert
        assertTrue(result);
        verify(sellerFindPort, times(1)).getByName("New Seller");
        verify(sellerCreatePort, times(1)).createSeller(newSeller);
    }

    @Test
    void testCreateItemOrThrow_WhenCreationFails_ThrowsSellerCreationException() {
        // Arrange
        when(sellerFindPort.getByName("New Seller")).thenReturn(Optional.empty());
        // Simular que la creación falla
        when(sellerCreatePort.createSeller(newSeller)).thenReturn(false);

        // Act & Assert
        assertThrows(SellerCreationException.class, () -> sellerServicePort.createItemOrThrow(newSeller));
        verify(sellerFindPort, times(1)).getByName("New Seller");
        verify(sellerCreatePort, times(1)).createSeller(newSeller);
    }

    @Test
    void testCreateItemOrThrow_WhenSellerAlreadyExists_ReturnsTrue() {
        // Arrange
        when(sellerFindPort.getByName("Existing Seller")).thenReturn(Optional.of(existingSeller));

        // Act
        boolean result = sellerServicePort.createItemOrThrow(existingSeller);

        // Assert
        assertTrue(result);
        verify(sellerFindPort, times(1)).getByName("Existing Seller");
        verifyNoInteractions(sellerCreatePort);
    }
}
