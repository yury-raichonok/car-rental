package com.example.carrental.repository;

import com.example.carrental.entity.user.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Role Repository.
 *
 * @author Yury Raichonak
 */
@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  /**
   * @param role of user.
   * @return optional user role.
   */
  Optional<UserRole> findByRole(String role);
}
