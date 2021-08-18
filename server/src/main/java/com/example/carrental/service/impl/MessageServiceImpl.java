package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;

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

/**
 * The service for Messages.
 * <p>
 * This class performs the CRUD operations for Messages.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageMapper messageMapper;
  private final MessageRepository messageRepository;

  /**
   * @param pageable data.
   * @return page of message response.
   */
  @Override
  public Page<MessageResponse> findAll(Pageable pageable) {
    var messagesPage = messageRepository.findAll(pageable);
    List<MessageResponse> messagesResponse = new ArrayList<>();
    messagesPage
        .forEach(message -> messagesResponse.add(messageMapper.messageToMessageResponse(message)));
    return new PageImpl<>(messagesResponse, messagesPage.getPageable(),
        messagesPage.getTotalElements());
  }

  /**
   * @param pageable data.
   * @return page of message response.
   */
  @Override
  public Page<MessageResponse> findAllNewMessages(Pageable pageable) {
    var messagesPage = messageRepository.findAllByReadedFalse(pageable);
    List<MessageResponse> messageResponses = new ArrayList<>();
    messagesPage
        .forEach(message -> messageResponses.add(messageMapper.messageToMessageResponse(message)));
    return new PageImpl<>(messageResponses, messagesPage.getPageable(),
        messagesPage.getTotalElements());
  }

  /**
   * @param id of message.
   * @return message.
   */
  @Override
  public Message findById(Long id) {
    return messageRepository.findById(id).orElseThrow(() -> {
      log.error("Message with id {} does not exist", id);
      throw new IllegalStateException(String.format("Message with id %d does not exists", id));
    });
  }

  /**
   * @param createMessageRequest data for creating new message.
   */
  @Override
  public void create(CreateMessageRequest createMessageRequest) {
    messageRepository.save(Message
        .builder()
        .name(createMessageRequest.getName())
        .email(createMessageRequest.getEmail())
        .phone(createMessageRequest.getPhone())
        .messageText(createMessageRequest.getMessage())
        .sentDate(LocalDateTime.now())
        .readed(false)
        .build());
  }

  /**
   * @param id of message.
   */
  @Override
  @Transactional
  public void updateMessageAsRead(Long id) {
    var message = findById(id);
    message.setReaded(true);
    messageRepository.save(message);
  }

  /**
   * @return amount of new messages.
   */
  @Override
  public int findNewMessagesAmount() {
    return messageRepository.countAllByReadedFalse();
  }

  /**
   * @return amount of new messages per day.
   */
  @Override
  public int findNewMessagesAmountPerDay() {
    return messageRepository.countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(),
        LocalTime.of(HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY,
            MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY)));
  }
}
