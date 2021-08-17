package com.example.carrental.repository;

import com.example.carrental.entity.user.UserPhone;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User Phone Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, Long> {

  /**
   * @param phone of user.
   * @return optional user phone.
   */
  Optional<UserPhone> findByPhoneAndActiveTrue(String phone);
}
