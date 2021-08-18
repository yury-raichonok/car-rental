package com.example.carrental.service;

import com.example.carrental.entity.user.UserRole;
import org.springframework.stereotype.Service;

/**
 * The service for User Roles.
 * <p>
 * This interface describes actions on User Roles.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserRoleService {

  UserRole findById(Long id);

  UserRole findByRole(String role);
}
