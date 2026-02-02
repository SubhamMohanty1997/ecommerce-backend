package com.ecommerce.inventory.service;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    @Override
    public Inventory createInventory(Long productId, Integer quantity) {
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(quantity);
        inventory.setInStock(quantity>0);
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new RuntimeException("Inventory not found!!"));
    }

    @Transactional
    @Override
    public void reduceStock(Long productId, Integer quantity) {
          Inventory inventory = getInventoryByProductId(productId);
          if(inventory.getQuantity()< quantity){
              throw new RuntimeException("Insufficient stock");
          }
          inventory.setQuantity(inventory.getQuantity()-quantity);
          inventory.setInStock(inventory.getQuantity()>0);
    }
}
