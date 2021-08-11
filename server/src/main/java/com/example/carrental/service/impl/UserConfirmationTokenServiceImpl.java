package com.example.carrental.service.impl;

import com.example.carrental.repository.UserConfirmationTokenRepository;
import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.service.UserConfirmationTokenService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserConfirmationTokenServiceImpl implements UserConfirmationTokenService {

  private final UserConfirmationTokenRepository userConfirmationTokenRepository;

  @Override
  @Transactional
  public String createUserConfirmationToken(UserConfirmationToken token) {
    userConfirmationTokenRepository.save(token);
    return "Success";
  }

  @Override
  public UserConfirmationToken getUserConfirmationTokenByToken(String token) {
    var optionalUserConfirmationToken = userConfirmationTokenRepository.findByToken(token);
    if (optionalUserConfirmationToken.isEmpty()) {
      log.error("User confirmation token with token {} does not exist", token);
      throw new IllegalStateException("Confirmation token does not exist");
    }
    return optionalUserConfirmationToken.get();
  }

  @Override
  @Transactional
  public String updateUserConfirmationTokenConfirmedAt(String token) {
    var userConfirmationToken = getUserConfirmationTokenByToken(token);
    userConfirmationToken.setConfirmationTime(LocalDateTime.now());
    userConfirmationTokenRepository.save(userConfirmationToken);
    return "Success";
  }
}
