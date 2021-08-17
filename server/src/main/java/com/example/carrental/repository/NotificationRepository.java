package com.example.carrental.repository;

import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Notification Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  /**
   * @param email of user.
   * @param status of notification.
   * @return amount of user notifications by status.
   */
  int countAllByUser_EmailAndStatus(String email, NotificationStatus status);

  /**
   * @param email of user.
   * @param status of notification
   * @param pageable data.
   * @return page of all user notifications.
   */
  Page<Notification> findAllByUser_EmailAndStatus(String email, NotificationStatus status,
      Pageable pageable);
}
