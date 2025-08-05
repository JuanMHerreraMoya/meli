package com.meli.app.application.port.out.seller;

import com.meli.app.domain.model.Seller;

public interface SellerCreatePort {
    boolean createSeller(Seller seller);
}
