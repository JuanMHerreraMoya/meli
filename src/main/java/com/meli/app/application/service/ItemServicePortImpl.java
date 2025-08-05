package com.meli.app.application.service;

import com.meli.app.adapter.in.error.exception.ItemCreationException;
import com.meli.app.application.port.in.ItemServicePort;
import com.meli.app.application.port.out.item.ItemCreatePort;
import com.meli.app.application.port.out.item.ItemFindPort;
import com.meli.app.application.port.out.item.ItemUpdatePort;
import com.meli.app.application.port.out.seller.SellerFindPort;
import com.meli.app.domain.model.Item;
import com.meli.app.domain.model.ItemComplete;
import com.meli.app.domain.model.Seller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServicePortImpl implements ItemServicePort {

    private final ItemCreatePort itemCreatePort;
    private final SellerFindPort sellerFindPort;
    private final ItemFindPort itemFindPort;
    private final ItemUpdatePort itemUpdatePort;

    public ItemServicePortImpl(ItemCreatePort itemCreatePort, SellerFindPort sellerFindPort, ItemFindPort itemFindPort, ItemUpdatePort itemUpdatePort) {
        this.itemCreatePort = itemCreatePort;
        this.sellerFindPort = sellerFindPort;
        this.itemFindPort = itemFindPort;
        this.itemUpdatePort = itemUpdatePort;
    }


    public boolean createItem(Item item) {
        Optional<Seller> seller = sellerFindPort.getByName(item.getSeller());
        Optional<Item> itemExisting = itemFindPort.findByNameAndSeller(item.getTitle(), item.getSeller());
        if(seller.isPresent()){
            if(itemExisting.isEmpty()){
                return itemCreatePort.createItem(item);
            }else{
                return itemUpdatePort.updateItem(item);
            }
        }else{
            return false;
        }
    }
    @Override
    public boolean createItemOrThrow(Item item) {
        boolean created =createItem(item);
        if (!created) {
            throw new ItemCreationException("No se pudo crear el item. Ya existe o los datos son inválidos.Verificar la infomracion del Item");
        }
        return true;
    }

    @Override
    public List<Item> getItems() {
        return itemFindPort.findItems();
    }

    @Override
    public Optional<ItemComplete> findById(String id) {
        Optional<Item> itemOptional = itemFindPort.findById(id);
        if(itemOptional.isPresent()){
            Item itemToUpdate = itemOptional.get();
            itemToUpdate.setViews(itemToUpdate.getViews()+1);
            itemUpdatePort.updateItem(itemToUpdate);
            return completeAnswer(itemOptional.get());
        }
        return Optional.empty();
    }

    private Optional<ItemComplete> completeAnswer(Item itemOptional) {
        ItemComplete itemComplete = ItemComplete.builder().id(itemOptional.getId()).title(itemOptional.getTitle())
                .description(itemOptional.getDescription())
                .price(itemOptional.getPrice())
                .stock(itemOptional.getStock())
                .rating(itemOptional.getRating())
                .views(itemOptional.getViews())
                .sold(itemOptional.getSold())
                .imagen(itemOptional.getImagen())
                .spects(itemOptional.getSpects())
                .category(itemOptional.getCategory()).build();
        Optional<Seller> seller = sellerFindPort.getByName(itemOptional.getSeller());
        seller.ifPresent(itemComplete::setSeller);
        return Optional.of(itemComplete);
    }
}
