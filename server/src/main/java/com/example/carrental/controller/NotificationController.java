package com.example.carrental.controller;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.service.NotificationService;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for Notifications REST endpoints.
 * <p>
 * This class handles the CRUD operations for Notifications, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * Handle the /notifications endpoint.
   * @param pageable page of notifications.
   * @return page of all new notifications.
   */
  @GetMapping
  public ResponseEntity<Page<NotificationResponse>> findAllNew(Pageable pageable) {
    var response = notificationService.findAllNew(pageable);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /notifications/history endpoint.
   * @param pageable page of notifications.
   * @return page of all notifications history.
   */
  @GetMapping(path = "/history")
  public ResponseEntity<Page<NotificationResponse>> findAllNotificationsHistory(Pageable pageable) {
    var response = notificationService.findAllNotificationsHistory(pageable);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /notifications/amount endpoint.
   * @return new user notifications amount.
   */
  @GetMapping(path = "/amount")
  public ResponseEntity<Integer> findNewUserNotificationsAmount() {
    var response = notificationService.findNewUserNotificationsAmount();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handle the /notifications/{id} endpoint.
   * @param id of the notification which updated.
   * @return status 200 if notification is updated.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> updateNotificationAsRead(@NotNull @Positive @PathVariable Long id) {
    notificationService.updateNotificationAsRead(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /notifications/{id} endpoint.
   * @param id of the notification which deleted.
   * @return status 200 if notification is deleted.
   */
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> delete(@NotNull @Positive @PathVariable Long id) {
    notificationService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
