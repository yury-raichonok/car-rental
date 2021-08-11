package com.example.carrental.service;

import com.example.carrental.entity.user.UserRole;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleService {

  UserRole findById(Long id);

  UserRole findByRole(String role);
}
