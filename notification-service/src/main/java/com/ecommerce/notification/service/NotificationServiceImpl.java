package com.ecommerce.notification.service;

import com.ecommerce.notification.entity.Notification;
import com.ecommerce.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    public void sendOrderNotification(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("ORDER CONFIRMATION!");
        mail.setText(message);

        mailSender.send(mail);

        Notification notification = Notification.builder()
                .recipientEmail(email)
                .message(message)
                .type("ORDER")
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

    }
}
