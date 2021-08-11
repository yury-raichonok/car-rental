package com.example.carrental.service;

import com.example.carrental.controller.dto.user.UserPhoneConfirmationRequest;
import com.example.carrental.controller.dto.user.UserSmsRequest;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.TokenExpireException;
import org.springframework.stereotype.Service;

@Service
public interface UserPhoneService {

  UserPhone findById(Long id);

  String sendConfirmationSms(UserSmsRequest userSmsRequest) throws EntityAlreadyExistsException;

  String create(UserPhoneConfirmationRequest userPhoneConfirmationRequest)
      throws EntityAlreadyExistsException, TokenExpireException;

  String updatePhoneStatus(Long id) throws EntityAlreadyExistsException;
}
