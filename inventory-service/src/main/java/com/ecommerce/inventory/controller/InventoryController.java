package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<Inventory> create(@RequestParam Long productId,
                                            @RequestParam Integer quantity){
        Inventory inventory = inventoryService.createInventory(productId, quantity);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam Long productId, @RequestParam Integer quantity){
        Boolean isAvailable = inventoryService.checkStock(productId,quantity);
        return new ResponseEntity<>(isAvailable, HttpStatus.OK);
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<Inventory> getByProductId(@PathVariable Long productId){
      Inventory inventory = inventoryService.getInventoryByProductId(productId);
      return new ResponseEntity<>(inventory,HttpStatus.OK);
    }

    @PostMapping("/reduce")
    public ResponseEntity<InventoryResponse> reduceStock(@RequestBody InventoryRequest request){
        InventoryResponse response = inventoryService.reduceStock(request.getProductId(), request.getQuantity());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
