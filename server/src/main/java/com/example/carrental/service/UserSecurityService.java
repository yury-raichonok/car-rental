package com.example.carrental.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The service for User Security.
 * <p>
 * This interface describes actions on User Security.
 * </p>
 * @author Yury Raichonak
 */
public interface UserSecurityService extends UserDetailsService {

  String getUserEmailFromSecurityContext();

  @Override
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
