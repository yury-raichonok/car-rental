package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.message.Message;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageMapperTest {

  @Autowired
  private MessageMapper messageMapper;

  @Test
  void messageToMessageResponse() {
    var sentDate = LocalDateTime.now();
    var message = Message.builder().id(1L).name("name").email("test@gmail.com")
        .phone("+375111234567").messageText("message").sentDate(sentDate).build();

    var messageResponse = messageMapper.messageToMessageResponse(message);

    assertThat(messageResponse).isNotNull();
    assertThat(messageResponse.getId()).isEqualTo(message.getId());
    assertThat(messageResponse.getName()).isEqualTo(message.getName());
    assertThat(messageResponse.getEmail()).isEqualTo(message.getEmail());
    assertThat(messageResponse.getPhone()).isEqualTo(message.getPhone());
    assertThat(messageResponse.getMessage()).isEqualTo(message.getMessageText());
    assertThat(messageResponse.getSentDate()).isEqualTo(message.getSentDate().format(DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }
}