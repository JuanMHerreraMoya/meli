package com.meli.app.application.port.in;

import com.meli.app.domain.model.Seller;

public interface SellerServicePort {
    boolean createItemOrThrow(Seller from);
}
