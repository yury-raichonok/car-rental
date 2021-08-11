package com.example.carrental.service;

import com.example.carrental.entity.user.UserConfirmationToken;
import org.springframework.stereotype.Service;

@Service
public interface UserConfirmationTokenService {

  String createUserConfirmationToken(UserConfirmationToken token);

  UserConfirmationToken getUserConfirmationTokenByToken(String token);

  String updateUserConfirmationTokenConfirmedAt(String token);
}
