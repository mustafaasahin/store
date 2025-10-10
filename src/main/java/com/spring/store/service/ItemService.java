package com.spring.store.service;

import com.spring.store.dao.ItemDao;
import com.spring.store.dto.item.ItemRequest;
import com.spring.store.dto.item.ItemResponse;
import com.spring.store.entity.Item;
import com.spring.store.exception.HttpBadRequestException;
import com.spring.store.exception.HttpMethodNotAllowedException;
import com.spring.store.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemDao itemDao;

    @Transactional(readOnly = true)
    public List<ItemResponse> getItems() {
        return itemDao.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long id) {
        return toResponse(findItemById(id));
    }

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        validateRequest(request);
        itemDao.findByName(request.getName()).ifPresent(item -> {
            throw new HttpMethodNotAllowedException("Item with name '" + request.getName() + "' already exists.");
        });

        Item item = Item.builder()
                .name(request.getName().trim())
                .description(StringUtils.hasText(request.getDescription()) ? request.getDescription().trim() : null)
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();

        return toResponse(itemDao.save(item));
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest request) {
        validateRequest(request);
        Item existing = findItemById(id);
        itemDao.findByName(request.getName())
                .filter(item -> !item.getId().equals(id))
                .ifPresent(item -> {
                    throw new HttpMethodNotAllowedException("Item with name '" + request.getName() + "' already exists.");
                });

        existing.setName(request.getName().trim());
        existing.setDescription(StringUtils.hasText(request.getDescription()) ? request.getDescription().trim() : null);
        existing.setPrice(request.getPrice());
        existing.setStockQuantity(request.getStockQuantity());

        return toResponse(itemDao.save(existing));
    }

    @Transactional
    public void deleteItem(Long id) {
        Item existing = findItemById(id);
        itemDao.delete(existing);
    }

    private Item findItemById(Long id) {
        return itemDao.findById(id)
                .orElseThrow(() -> new HttpNotFoundException("Item not found for id '" + id + "'."));
    }

    private void validateRequest(ItemRequest request) {
        if (request.getName() == null || !StringUtils.hasText(request.getName())) {
            throw new HttpBadRequestException("Item name is required.");
        }

        if (request.getPrice() == null || isNegative(request.getPrice())) {
            throw new HttpMethodNotAllowedException("Item price must be greater than or equal to zero.");
        }

        if (request.getStockQuantity() == null || request.getStockQuantity() < 0) {
            throw new HttpMethodNotAllowedException("Item stock quantity must be zero or greater.");
        }
    }

    private boolean isNegative(BigDecimal price) {
        return price.signum() < 0;
    }

    private ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .build();
    }
}
