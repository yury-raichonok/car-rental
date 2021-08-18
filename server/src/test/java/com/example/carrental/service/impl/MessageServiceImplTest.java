package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.message.CreateMessageRequest;
import com.example.carrental.entity.message.Message;
import com.example.carrental.repository.MessageRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class MessageServiceImplTest {

  @Autowired
  private MessageServiceImpl messageService;

  @MockBean
  private MessageRepository messageRepository;

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Arrays.asList(Message.builder().id(1L).messageText("message")
        .build(), Message.builder().id(2L).messageText("message1").build()));
    when(messageRepository.findAll(pageable)).thenReturn(response);

    var messageResponse = messageService.findAll(pageable);

    assertThat(messageResponse).isNotNull();
    assertThat(messageResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNewMessages_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Arrays.asList(Message.builder().id(1L).messageText("message")
        .build(), Message.builder().id(2L).messageText("message1").build()));
    when(messageRepository.findAllByReadedFalse(pageable)).thenReturn(response);

    var messageResponse = messageService.findAllNewMessages(pageable);

    assertThat(messageResponse).isNotNull();
    assertThat(messageResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var response = Optional.of(Message.builder().id(1L).messageText("message").build());
    when(messageRepository.findById(1L)).thenReturn(response);

    var message = messageService.findById(1L);

    assertThat(message).isNotNull();
    assertThat(message.getId()).isEqualTo(response.get().getId());
    assertThat(message.getMessageText()).isEqualTo(response.get().getMessageText());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(messageRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> messageService.findById(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createMessageRequest = CreateMessageRequest.builder().name("name").email("email@gmail.com")
        .phone("+375111234567").message("message").build();
    when(messageRepository.save(any(Message.class))).thenReturn(new Message());

    assertDoesNotThrow(() -> messageService.create(createMessageRequest));
  }

  @Test
  void givenValidRequest_whenUpdateMessageAsRead_thenSuccess() {
    var response = Optional
        .of(Message.builder().id(1L).messageText("message").readed(false).build());
    when(messageRepository.findById(1L)).thenReturn(response);

    messageService.updateMessageAsRead(1L);

    assertTrue(response.get().isReaded());
  }

  @Test
  void givenValidRequest_whenFindNewMessagesAmount_thenSuccess() {
    when(messageRepository.countAllByReadedFalse()).thenReturn(8);

    var expectedAmount = 8;
    var amount = messageService.findNewMessagesAmount();

    assertEquals(expectedAmount, amount);
  }

  @Test
  void givenValidRequest_whenFindNewMessagesAmountPerDay_thenSuccess() {
    when(messageRepository.countAllBySentDateAfter(any())).thenReturn(8);

    var expectedAmount = 8;
    var amount = messageService.findNewMessagesAmountPerDay();

    assertEquals(expectedAmount, amount);
  }
}