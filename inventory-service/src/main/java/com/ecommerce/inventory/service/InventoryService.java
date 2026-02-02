package com.ecommerce.inventory.service;

import com.ecommerce.inventory.entity.Inventory;

public interface InventoryService {
    Inventory createInventory(Long productId,Integer quantity);
    Inventory getInventoryByProductId(Long productId);
    void reduceStock(Long productId, Integer quantity);
}
