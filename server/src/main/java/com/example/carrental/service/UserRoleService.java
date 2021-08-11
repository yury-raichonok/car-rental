package com.example.carrental.service;

import com.example.carrental.entity.user.UserRole;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleService {

  UserRole getById(Long id);

  UserRole getByRole(String role);
}
