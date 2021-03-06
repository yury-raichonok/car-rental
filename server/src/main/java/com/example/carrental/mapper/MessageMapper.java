package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;

import com.example.carrental.controller.dto.message.MessageResponse;
import com.example.carrental.entity.message.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The interface for mapping Message entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface MessageMapper {

  /**
   * @param message data.
   * @return MessageResponse DTO.
   */
  @Mapping(target = "id", source = "message.id")
  @Mapping(target = "name", source = "message.name")
  @Mapping(target = "email", source = "message.email")
  @Mapping(target = "phone", source = "message.phone")
  @Mapping(target = "message", source = "message.messageText")
  @Mapping(target = "sentDate", source = "message.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  MessageResponse messageToMessageResponse(Message message);
}
