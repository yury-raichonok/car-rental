package com.example.carrental.service.impl;

import com.example.carrental.repository.UserConfirmationTokenRepository;
import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.service.UserConfirmationTokenService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for User Confirmation Tokens.
 * <p>
 * This class performs the CRUD operations for User Confirmation Tokens.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserConfirmationTokenServiceImpl implements UserConfirmationTokenService {

  private final UserConfirmationTokenRepository userConfirmationTokenRepository;

  /**
   * @param token for creating new confirmation token.
   */
  @Override
  @Transactional
  public void createUserConfirmationToken(UserConfirmationToken token) {
    userConfirmationTokenRepository.save(token);
  }

  /**
   * @param token data.
   * @return user confirmation token.
   */
  @Override
  public UserConfirmationToken getUserConfirmationTokenByToken(String token) {
    return userConfirmationTokenRepository.findByToken(token).orElseThrow(() -> {
      log.error("User confirmation token with token {} does not exist", token);
      throw new IllegalStateException("Confirmation token does not exist");
    });
  }

  /**
   * @param token data.
   */
  @Override
  @Transactional
  public void updateUserConfirmationTokenConfirmedAt(String token) {
    var userConfirmationToken = getUserConfirmationTokenByToken(token);
    userConfirmationToken.setConfirmationTime(LocalDateTime.now());
    userConfirmationTokenRepository.save(userConfirmationToken);
  }
}
