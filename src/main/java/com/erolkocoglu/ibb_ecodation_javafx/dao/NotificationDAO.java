package com.erolkocoglu.ibb_ecodation_javafx.dao;

import com.erolkocoglu.ibb_ecodation_javafx.dto.NotificationDTO;
import com.erolkocoglu.ibb_ecodation_javafx.utils.NotificationType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private List<NotificationDTO> notifications = new ArrayList<>();

    public void addNotification(String message, NotificationType notificationType) {
        NotificationDTO notification = new NotificationDTO();
        notification.setMessage(message);
        notification.setNotificationType(notificationType);
        notification.setTimestamp(LocalDateTime.now());
        notification.saveToFile(message, notificationType);
        notifications.add(notification);

    }

    public List<NotificationDTO> getAllNotifications() {
        return notifications;
    }
}
