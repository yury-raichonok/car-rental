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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for Messages REST endpoints.
 * <p>
 * This class handles the CRUD operations for feedback Messages, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
@Validated
public class MessageController {

  private final MessageService messageService;

  /**
   * Handle the /messages/all endpoint.
   * @param pageable page of messages.
   * @return list of all messages.
   */
  @GetMapping(path = "/all")
  public ResponseEntity<Page<MessageResponse>> findAll(Pageable pageable) {
    var messages = messageService.findAll(pageable);
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  /**
   * Handle the /messages/new endpoint.
   * @param pageable page of messages.
   * @return list of new messages.
   */
  @GetMapping(path = "/new")
  public ResponseEntity<Page<MessageResponse>> findAllNewMessages(Pageable pageable) {
    var messages = messageService.findAllNewMessages(pageable);
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  /**
   * Handle the /messages/new/amount endpoint.
   * @return new messages amount.
   */
  @GetMapping(path = "/new/amount")
  public ResponseEntity<Integer> findNewMessagesAmount() {
    var messages = messageService.findNewMessagesAmount();
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  /**
   * Handle the /messages/new/amount/day endpoint.
   * @return new messages amount per day.
   */
  @GetMapping(path = "/new/amount/day")
  public ResponseEntity<Integer> findNewMessagesAmountPerDay() {
    var messages = messageService.findNewMessagesAmountPerDay();
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  /**
   * Handle the /messages endpoint.
   * @param createMessageRequest request with parameters.
   * @return status 200 if new message created.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateMessageRequest createMessageRequest) {
    messageService.create(createMessageRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /messages/{id} endpoint.
   * @param id of the message which updated.
   * @return status 200 if message status updated.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> updateMessageAsRead(@NotNull @Positive @PathVariable Long id) {
    messageService.updateMessageAsRead(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
