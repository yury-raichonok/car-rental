package com.example.carrental.controller;

import com.example.carrental.service.NotificationService;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<?> findAllNew(Pageable pageable) {
    try {
      var response = notificationService.findAllNew(pageable);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/history")
  public ResponseEntity<?> findAllNotificationsHistory(Pageable pageable) {
    try {
      var response = notificationService.findAllNotificationsHistory(pageable);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<?> updateNotificationAsRead(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = notificationService.updateNotificationAsRead(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> delete(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = notificationService.delete(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
