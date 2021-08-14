package com.example.carrental.service.impl;

import com.example.carrental.entity.user.UserRole;
import com.example.carrental.repository.UserRoleRepository;
import com.example.carrental.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

  private final UserRoleRepository userRoleRepository;

  @Override
  public UserRole findById(Long id) {
    return userRoleRepository.findById(id).orElseThrow(() -> {
      log.error("User role with id {} does not exists", id);
      throw new IllegalStateException(String.format("User role with id %d does not exists", id));
    });
  }

  @Override
  public UserRole findByRole(String role) {
    return userRoleRepository.findByRole(role).orElseThrow(() -> {
      log.error("User role with role name {} does not exists", role);
      throw new IllegalStateException(String.format("User role with role name %s does not exists",
          role));
    });
  }
}
