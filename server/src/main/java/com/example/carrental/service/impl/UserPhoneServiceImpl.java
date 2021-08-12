package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.user.UserPhoneConfirmationRequest;
import com.example.carrental.controller.dto.user.UserSmsRequest;
import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.repository.UserPhoneRepository;
import com.example.carrental.service.SmsSenderService;
import com.example.carrental.service.UserConfirmationTokenService;
import com.example.carrental.service.UserPhoneService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.twilio.type.PhoneNumber;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPhoneServiceImpl implements UserPhoneService {

  private final SmsSenderService smsSenderService;
  private final UserConfirmationTokenService userConfirmationTokenService;
  private final UserService userService;
  private final UserPhoneRepository userPhoneRepository;

  @Value("${twilio.trial_number}")
  private String twilioTrialNumber;

  @Override
  public UserPhone findById(Long id) {
    var optionalPhone = userPhoneRepository.findById(id);
    if (optionalPhone.isEmpty()) {
      log.error("Phone with id {} does not exists!", id);
      throw new IllegalStateException(
          String.format("Phone with id %d does not exists!", id));
    }
    return optionalPhone.get();
  }

  @Override
  @Transactional
  public void create(UserPhoneConfirmationRequest userPhoneConfirmationRequest)
      throws EntityAlreadyExistsException, TokenExpireException {
    var phone = userPhoneRepository.findByPhoneAndActiveTrue(userPhoneConfirmationRequest.getPhoneNumber());
    if (phone.isPresent()) {
      log.error("Phone with number {} already exists!", phone);
      throw new EntityAlreadyExistsException(String.format("Phone with number %s already exists!",
          phone));
    }

    var confirmationToken = userConfirmationTokenService
        .getUserConfirmationTokenByToken(userPhoneConfirmationRequest.getToken());
    if (confirmationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
      log.error("Token {} expired", userPhoneConfirmationRequest.getToken());
      throw new TokenExpireException("Token expired!");
    }

    userConfirmationTokenService
        .updateUserConfirmationTokenConfirmedAt(userPhoneConfirmationRequest.getToken());
    userPhoneRepository.save(UserPhone
        .builder()
        .phone(userPhoneConfirmationRequest.getPhoneNumber())
        .active(true)
        .user(confirmationToken.getUser())
        .build());
  }

  @Override
  @Transactional
  public void sendConfirmationSms(UserSmsRequest userSmsRequest)
      throws EntityAlreadyExistsException {
    var phone = userPhoneRepository.findByPhoneAndActiveTrue(userSmsRequest.getPhoneNumber());
    if (phone.isPresent()) {
      log.error("Phone with number {} already exists!", phone);
      throw new EntityAlreadyExistsException(String.format("Phone with number %s already exists!",
          phone));
    }

    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var random = new Random();
    int token = 1000 + random.nextInt(9999 - 1000);

    var user = userService.findUserByEmail(username);

    var confirmationToken = UserConfirmationToken
        .builder()
        .token(String.valueOf(token))
        .creationTime(LocalDateTime.now())
        .expirationTime(LocalDateTime.now().plusMinutes(1))
        .user(user)
        .build();

    userConfirmationTokenService.createUserConfirmationToken(confirmationToken);

    smsSenderService.sendSms(new PhoneNumber(String.format("+%s", userSmsRequest.getPhoneNumber())),
        new PhoneNumber(twilioTrialNumber), String.valueOf(token));
  }

  @Override
  public void updatePhoneStatus(Long id) throws EntityAlreadyExistsException {
    var phone = findById(id);
    if (phone.isActive()) {
      phone.setActive(false);
    } else if (userPhoneRepository.findByPhoneAndActiveTrue(phone.getPhone()).isPresent()){
      log.error("Phone with number {} already exists!", phone);
      throw new EntityAlreadyExistsException(String.format("Phone with number %s already exists!",
          phone));
    }
    phone.setActive(!phone.isActive());
    userPhoneRepository.save(phone);
  }
}
