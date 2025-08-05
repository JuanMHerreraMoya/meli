package com.meli.app.application.port.out.seller;

import com.meli.app.domain.model.Seller;

import java.util.Optional;

public interface SellerFindPort {
    Optional<Seller> getByName(String name);
}
