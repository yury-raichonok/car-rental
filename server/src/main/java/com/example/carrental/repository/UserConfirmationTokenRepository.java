package com.example.carrental.repository;

import com.example.carrental.entity.user.UserConfirmationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfirmationTokenRepository extends
    JpaRepository<UserConfirmationToken, Long> {

  Optional<UserConfirmationToken> findByToken(String token);
}
