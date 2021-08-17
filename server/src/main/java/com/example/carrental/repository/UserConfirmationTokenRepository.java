package com.example.carrental.repository;

import com.example.carrental.entity.user.UserConfirmationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User Confirmation Token Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface UserConfirmationTokenRepository extends
    JpaRepository<UserConfirmationToken, Long> {

  /**
   * @param token of user.
   * @return optional confirmation token.
   */
  Optional<UserConfirmationToken> findByToken(String token);
}
