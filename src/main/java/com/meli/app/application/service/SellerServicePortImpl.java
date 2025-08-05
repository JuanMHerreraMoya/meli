package com.meli.app.application.service;

import com.meli.app.adapter.in.error.exception.SellerCreationException;
import com.meli.app.application.port.in.SellerServicePort;
import com.meli.app.application.port.out.seller.SellerCreatePort;
import com.meli.app.application.port.out.seller.SellerFindPort;
import com.meli.app.domain.model.Seller;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServicePortImpl implements SellerServicePort {

    private final SellerCreatePort sellerCreatePort;
    private final SellerFindPort sellerFindPort;
    public SellerServicePortImpl(SellerCreatePort sellerCreatePort, SellerFindPort sellerFindPort) {
        this.sellerCreatePort = sellerCreatePort;
        this.sellerFindPort = sellerFindPort;
    }

    @Override
    public boolean createItemOrThrow(Seller seller) {
        boolean created =createSeller(seller);
        if (!created) {
            throw new SellerCreationException("No se pudo crear. Ya existe o los datos son inválidos.");
        }
        return true;
    }

    public boolean createSeller(Seller seller) {
        Optional<Seller> oldSeller = sellerFindPort.getByName(seller.getName());
        if(oldSeller.isPresent()){
            return true;
        }
        return sellerCreatePort.createSeller(seller);
    }


}
