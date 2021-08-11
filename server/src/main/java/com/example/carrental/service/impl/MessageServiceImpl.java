package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.message.CreateMessageRequest;
import com.example.carrental.controller.dto.message.MessageResponse;
import com.example.carrental.entity.message.Message;
import com.example.carrental.mapper.MessageMapper;
import com.example.carrental.repository.MessageRepository;
import com.example.carrental.service.MessageService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;

  @Override
  public Page<MessageResponse> findAll(Pageable pageable) {
    var messagesPage = messageRepository.findAll(pageable);
    List<MessageResponse> messageResponses = new ArrayList<>();
    messagesPage.forEach(m -> messageResponses.add(messageMapper.messageToMessageResponse(m)));
    return new PageImpl<>(messageResponses, messagesPage.getPageable(),
        messagesPage.getTotalElements());
  }

  @Override
  public Page<MessageResponse> findNewMessages(Pageable pageable) {
    var messagesPage = messageRepository.findAllByReadedFalse(pageable);
    List<MessageResponse> messageResponses = new ArrayList<>();
    messagesPage.forEach(m -> messageResponses.add(messageMapper.messageToMessageResponse(m)));
    return new PageImpl<>(messageResponses, messagesPage.getPageable(),
        messagesPage.getTotalElements());
  }

  @Override
  public Message findById(Long id) {
    var optionalMessage = messageRepository.findById(id);
    if (optionalMessage.isEmpty()) {
      log.error("Message with id {} does not exist", id);
      throw new IllegalStateException(String.format("Message with id %d does not exists", id));
    }
    return optionalMessage.get();
  }

  @Override
  public String create(CreateMessageRequest createMessageRequest) {
    messageRepository.save(Message
        .builder()
        .name(createMessageRequest.getName())
        .email(createMessageRequest.getEmail())
        .phone(createMessageRequest.getPhone())
        .message(createMessageRequest.getMessage())
        .sentDate(LocalDateTime.now())
        .readed(false)
        .build());
    return "Success";
  }

  @Override
  @Transactional
  public String updateMessageAsRead(Long id) {
    var message = findById(id);

    message.setReaded(true);
    messageRepository.save(message);

    return "Success";
  }

  @Override
  public int findNewMessagesAmount() {
    return messageRepository.countAllByReadedFalse();
  }

  @Override
  public int findNewMessagesAmountPerDay() {
    return messageRepository
        .countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
  }
}
