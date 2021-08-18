package com.example.carrental.service;

import com.example.carrental.controller.dto.user.UserPhoneConfirmationRequest;
import com.example.carrental.controller.dto.user.UserSmsRequest;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.TokenExpireException;
import org.springframework.stereotype.Service;

/**
 * The service for User Phones.
 * <p>
 * This interface describes actions on User Phones.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserPhoneService {

  UserPhone findById(Long id);

  void create(UserPhoneConfirmationRequest userPhoneConfirmationRequest)
      throws EntityAlreadyExistsException, TokenExpireException;

  void sendConfirmationSms(UserSmsRequest userSmsRequest) throws EntityAlreadyExistsException;

  void updatePhoneStatus(Long id) throws EntityAlreadyExistsException;
}
