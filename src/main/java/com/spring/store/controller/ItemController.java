package com.spring.store.controller;

import com.spring.store.dto.item.ItemRequest;
import com.spring.store.dto.item.ItemResponse;
import com.spring.store.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemResponse> getItems() {
        return itemService.getItems();
    }

    @GetMapping("/{id}")
    public ItemResponse getItem(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest request) {
        ItemResponse response = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ItemResponse updateItem(@PathVariable Long id, @RequestBody ItemRequest request) {
        return itemService.updateItem(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
