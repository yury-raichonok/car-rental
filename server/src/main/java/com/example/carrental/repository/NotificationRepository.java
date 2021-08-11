package com.example.carrental.repository;

import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Page<Notification> findAllByUser_EmailAndStatus(String email, NotificationStatus status,
      Pageable pageable);

  int countAllByUser_EmailAndStatus(String email, NotificationStatus status);
}
