package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;

public interface InventoryService {
    Inventory createInventory(Long productId,Integer quantity);
    Inventory getInventoryByProductId(Long productId);
    InventoryResponse reduceStock(Long productId, Integer quantity);
    Boolean checkStock(Long productId, Integer quantity);
}
