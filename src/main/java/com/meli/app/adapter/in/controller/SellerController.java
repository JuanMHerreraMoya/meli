package com.meli.app.adapter.in.controller;

import com.meli.app.application.port.in.SellerServicePort;
import com.meli.app.domain.model.Seller;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = "*")
public class SellerController {
    private final SellerServicePort sellerServicePort;

    public SellerController(SellerServicePort sellerServicePort) {
        this.sellerServicePort = sellerServicePort;
    }

    @PostMapping()
    public ResponseEntity<String> createSeller(@Valid @RequestBody Seller seller) {
        sellerServicePort.createItemOrThrow(Seller.from(seller));
        return ResponseEntity.ok("Seller creado/actualizado correctamente.");
    }
}
