package com.meli.app.adapter.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.app.adapter.in.error.exception.ItemNotFoundException;
import com.meli.app.application.port.in.ItemServicePort;
import com.meli.app.domain.model.Item;
import com.meli.app.domain.model.ItemComplete;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemServicePort itemServicePort;
    private final Validator validator;


    public ItemController(ItemServicePort itemServicePort, Validator validator) {
        this.itemServicePort = itemServicePort;
        this.validator = validator;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createItem(
            @RequestPart("item") String itemJson,
            @RequestPart("imagen") MultipartFile imagen) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Item item = mapper.readValue(itemJson, Item.class);

            Set<ConstraintViolation<Item>> violations = validator.validate(item);
            if (!violations.isEmpty()) {
                String errores = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body("Errores de validación: " + errores);
            }

            byte[] bytes = imagen.getBytes();
            String imagenBase64 = Base64.getEncoder().encodeToString(bytes);
            item.setImagen(imagenBase64);

            boolean created = itemServicePort.createItemOrThrow(Item.from(item));
            return created
                    ? ResponseEntity.ok("Item creado/ Actualizado.")
                    : ResponseEntity.badRequest().body("Error al crear el item.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<Item>> getItems(){
        return ResponseEntity.ok(itemServicePort.getItems());
    }

    @GetMapping("/{id}")
    public ItemComplete findById(@PathVariable String id) {
        return itemServicePort.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }
}
