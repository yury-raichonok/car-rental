package com.example.carrental.repository;

import com.example.carrental.entity.user.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Repository.
 *
 * @author Yury Raichonak
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * @param date data.
   * @return amount of new users per day.
   */
  int countAllByCreatedAtAfter(LocalDateTime date);

  /**
   * @param email of user.
   * @return optional user.
   */
  Optional<User> findByEmail(String email);
}
