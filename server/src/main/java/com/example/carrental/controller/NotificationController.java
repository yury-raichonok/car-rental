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

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<Page<NotificationResponse>> findAllNew(Pageable pageable) {
    var response = notificationService.findAllNew(pageable);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(path = "/history")
  public ResponseEntity<Page<NotificationResponse>> findAllNotificationsHistory(Pageable pageable) {
    var response = notificationService.findAllNotificationsHistory(pageable);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> updateNotificationAsRead(@NotNull @Positive @PathVariable Long id) {
    notificationService.updateNotificationAsRead(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> delete(@NotNull @Positive @PathVariable Long id) {
    notificationService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
