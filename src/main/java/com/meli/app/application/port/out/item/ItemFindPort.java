package com.meli.app.application.port.out.item;

import com.meli.app.domain.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemFindPort {
    Optional<Item> findByNameAndSeller(String title, String seller);

    List<Item> findItems();

    Optional<Item> findById(String id);
}
