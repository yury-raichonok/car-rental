package com.example.carrental.service;

import com.example.carrental.entity.user.UserConfirmationToken;
import org.springframework.stereotype.Service;

/**
 * The service for User Confirmation Tokens.
 * <p>
 * This interface describes actions on User Confirmation Tokens.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserConfirmationTokenService {

  void createUserConfirmationToken(UserConfirmationToken token);

  UserConfirmationToken getUserConfirmationTokenByToken(String token);

  void updateUserConfirmationTokenConfirmedAt(String token);
}
