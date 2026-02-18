package com.ecommerce.notification.service;

public interface NotificationService {

    void sendOrderNotification(String email, String message);
}
