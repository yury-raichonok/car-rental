package com.example.carrental.controller;

import com.example.carrental.controller.dto.message.CreateMessageRequest;
import com.example.carrental.controller.dto.message.MessageResponse;
import com.example.carrental.service.MessageService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
@Validated
public class MessageController {

  private final MessageService messageService;

  @GetMapping(path = "/all")
  public ResponseEntity<Page<MessageResponse>> findAll(Pageable pageable) {
    var messages = messageService.findAll(pageable);
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  @GetMapping(path = "/new")
  public ResponseEntity<Page<MessageResponse>> findNewMessages(Pageable pageable) {
    var messages = messageService.findNewMessages(pageable);
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestBody CreateMessageRequest createMessageRequest) {
    try {
      var response = messageService.create(createMessageRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> updateMessageAsRead(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = messageService.updateMessageAsRead(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
