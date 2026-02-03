package com.ecommerce.order.service;

import com.ecommerce.order.constant.OrderStatus;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.exception.ProductNotFoundException;
import com.ecommerce.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate=restTemplate;
    }

    @Value("${services.product.url}")
    private String productServiceUrl;

    @Value("${services.inventory.url}")
    private String inventoryServiceUrl;

    @Transactional
    @Override
    public Order placeOrder(Long productId, Integer quantity) {

        String productUrl = productServiceUrl+"/api/v1/products/"+productId;
        ProductResponse product = restTemplate.getForObject(productUrl, ProductResponse.class);

        if(product == null){
            throw new ProductNotFoundException("Product not found with ID: "+productId);
        }

        String checkStockUrl = inventoryServiceUrl
                +"/api/v1/inventory/check"
                +"?productId="+productId
                +"&quantity="+quantity;

        Boolean inStock = restTemplate.getForObject(checkStockUrl,Boolean.class);

        if (Boolean.FALSE.equals(inStock)) {
            throw new RuntimeException("Product out of stock");
        }

        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setPrice(product.getPrice()*quantity);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        String reduceStockUrl = inventoryServiceUrl
                + "/api/v1/inventory/reduce"
                + "/" + productId
                + "?quantity=" + quantity;

        restTemplate.getForObject(reduceStockUrl,null,Void.class);

        order.setStatus(OrderStatus.CONFIRMED);


        return orderRepository.save(order);
    }
}
