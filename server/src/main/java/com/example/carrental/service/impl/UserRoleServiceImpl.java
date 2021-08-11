package com.example.carrental.service.impl;

import com.example.carrental.repository.UserRoleRepository;
import com.example.carrental.entity.user.UserRole;
import com.example.carrental.service.UserRoleService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

  private final UserRoleRepository userRoleRepository;

  @Override
  public UserRole getById(Long id) {
    Optional<UserRole> optionalRole = userRoleRepository.findById(id);
    if (optionalRole.isEmpty()) {
      log.error("User role with id {} does not exists", id);
      throw new IllegalStateException(String.format("User role with id %d does not exists", id));
    }
    return optionalRole.get();
  }

  @Override
  public UserRole getByRole(String role) {
    Optional<UserRole> optionalRole = userRoleRepository.findByRole(role);
    if (optionalRole.isEmpty()) {
      log.error("User role with role name {} does not exists", role);
      throw new IllegalStateException(
          String.format("User role with role name %s does not exists", role));
    }
    return optionalRole.get();
  }
}
