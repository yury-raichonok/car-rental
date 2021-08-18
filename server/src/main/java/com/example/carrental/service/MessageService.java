package com.example.carrental.service;

import com.example.carrental.controller.dto.message.CreateMessageRequest;
import com.example.carrental.controller.dto.message.MessageResponse;
import com.example.carrental.entity.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The service for Messages.
 * <p>
 * This interface describes actions on Messages.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface MessageService {

  Page<MessageResponse> findAll(Pageable pageable);

  Page<MessageResponse> findAllNewMessages(Pageable pageable);

  Message findById(Long id);

  void create(CreateMessageRequest createMessageRequest);

  void updateMessageAsRead(Long id);

  int findNewMessagesAmount();

  int findNewMessagesAmountPerDay();
}
