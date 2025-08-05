package com.meli.app.application.port.in;

import com.meli.app.domain.model.Item;
import com.meli.app.domain.model.ItemComplete;

import java.util.List;
import java.util.Optional;

public interface ItemServicePort {
    List<Item> getItems();
    Optional<ItemComplete> findById(String id);

    boolean createItemOrThrow(Item from);
}
