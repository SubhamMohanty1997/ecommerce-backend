package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import com.ecommerce.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("Creating inventory for productId={} quantity={}",productId,quantity);
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(quantity);
        inventory.setInStock(quantity>0);
        log.debug("Inventory object created: {}", inventory);
        return inventoryRepository.save(inventory);
    }

    @Cacheable(value = "inventory", key="#productId")
    @Override
    public Inventory getInventoryByProductId(Long productId) {
        log.info("Fetching inventory from DB for productId={}", productId);
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new InventoryNotFoundException("Inventory not found with productId: "+productId));
    }

    @Override
    public Boolean checkStock(Long productId, Integer quantity){
         Inventory inventory = inventoryRepository.findByProductId(productId)
             .orElseThrow(()-> new InventoryNotFoundException("Inventory not found with productId: "+productId));
        if(inventory.getQuantity() >= quantity){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @CacheEvict(value = "inventory", key="#productId")
    @Transactional
    @Override
    public InventoryResponse reduceStock(Long productId, Integer quantity) {
          Inventory inventory = getInventoryByProductId(productId);
          if(inventory.getQuantity()< quantity){
              throw new RuntimeException("Insufficient stock");
          }
          InventoryResponse response = new InventoryResponse();
          response.setProductId(inventory.getProductId());
          response.setQuantity(inventory.getQuantity()-quantity);
          response.setInStock(inventory.getQuantity()>0);
        return response;
    }
}
