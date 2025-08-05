package com.meli.app;

import com.meli.app.adapter.in.controller.SellerController;
import com.meli.app.application.port.in.SellerServicePort;
import com.meli.app.domain.model.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerControllerTest {

    @Mock
    private SellerServicePort sellerServicePort;

    @InjectMocks
    private SellerController sellerController;

    private Seller validSeller;
    private Seller invalidSeller;

    @BeforeEach
    void setUp() {
        validSeller = new Seller();
        validSeller.setId("123");
        validSeller.setName("Test Seller");

        invalidSeller = new Seller();
    }

    @Test
    void testCreateSeller_Success() {
        // Arrange

        // Act
        ResponseEntity<String> response = sellerController.createSeller(validSeller);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Seller creado/actualizado correctamente.", response.getBody());

        verify(sellerServicePort, times(1)).createItemOrThrow(any(Seller.class));
    }

    @Test
    void testCreateSeller_ThrowsException_FromService() {
        // Arrange
        doThrow(new RuntimeException("Error en el servicio de creación")).when(sellerServicePort)
                .createItemOrThrow(any(Seller.class));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> sellerController.createSeller(validSeller));
        assertEquals("Error en el servicio de creación", exception.getMessage());
        verify(sellerServicePort, times(1)).createItemOrThrow(any(Seller.class));
    }
}
