package com.example.carrental.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserSecurityService extends UserDetailsService {

  @Override
  default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return null;
  }
}
