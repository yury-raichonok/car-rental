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

  private static final int START_HOUR_OF_STATISTIC = 0;
  private static final int START_MINUTES_OF_STATISTIC = 0;

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;

  @Override
  public Page<MessageResponse> findAll(Pageable pageable) {
    var messagesPage = messageRepository.findAll(pageable);
    List<MessageResponse> messagesResponse = new ArrayList<>();
    messagesPage
        .forEach(message -> messagesResponse.add(messageMapper.messageToMessageResponse(message)));
    return new PageImpl<>(messagesResponse, messagesPage.getPageable(),
        messagesPage.getTotalElements());
  }

  @Override
  public Page<MessageResponse> findAllNewMessages(Pageable pageable) {
    var messagesPage = messageRepository.findAllByReadedFalse(pageable);
    List<MessageResponse> messageResponses = new ArrayList<>();
    messagesPage
        .forEach(message -> messageResponses.add(messageMapper.messageToMessageResponse(message)));
    return new PageImpl<>(messageResponses, messagesPage.getPageable(),
        messagesPage.getTotalElements());
  }

  @Override
  public Message findById(Long id) {
    return messageRepository.findById(id).orElseThrow(() -> {
      log.error("Message with id {} does not exist", id);
      throw new IllegalStateException(String.format("Message with id %d does not exists", id));
    });
  }

  @Override
  public void create(CreateMessageRequest createMessageRequest) {
    messageRepository.save(Message
        .builder()
        .name(createMessageRequest.getName())
        .email(createMessageRequest.getEmail())
        .phone(createMessageRequest.getPhone())
        .message(createMessageRequest.getMessage())
        .sentDate(LocalDateTime.now())
        .readed(false)
        .build());
  }

  @Override
  @Transactional
  public void updateMessageAsRead(Long id) {
    var message = findById(id);
    message.setReaded(true);
    messageRepository.save(message);
  }

  @Override
  public int findNewMessagesAmount() {
    return messageRepository.countAllByReadedFalse();
  }

  @Override
  public int findNewMessagesAmountPerDay() {
    return messageRepository.countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(),
        LocalTime.of(START_HOUR_OF_STATISTIC, START_MINUTES_OF_STATISTIC)));
  }
}
