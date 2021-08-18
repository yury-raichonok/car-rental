package com.example.carrental.service.impl;

import com.example.carrental.repository.UserRepository;
import com.example.carrental.service.UserSecurityService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The service for User Security.
 *
 * @author Yury Raichonak
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSecurityServiceImpl implements UserSecurityService {

  private final UserRepository userRepository;

  /**
   * @return user email from security context.
   * @throws IllegalStateException if user is not authenticated.
   */
  @Override
  public String getUserEmailFromSecurityContext() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName())
        .orElseThrow(() -> {
          log.error("User not authenticated!");
          throw new IllegalStateException("User not authenticated");
        });
  }

  /**
   * @param email of user.
   * @return user details.
   * @throws UsernameNotFoundException if user with specified email does not exists.
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email).orElseThrow(() -> {
      log.error("User with email {} does not exist", email);
      throw new UsernameNotFoundException(String.format("User with email %s not found", email));
    });
  }
}
