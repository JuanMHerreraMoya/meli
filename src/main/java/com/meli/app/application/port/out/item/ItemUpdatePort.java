package com.meli.app.application.port.out.item;

import com.meli.app.domain.model.Item;

public interface ItemUpdatePort {
    boolean updateItem(Item item);
}
