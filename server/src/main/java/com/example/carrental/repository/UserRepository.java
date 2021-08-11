package com.example.carrental.repository;

import com.example.carrental.entity.user.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  int countAllByCreatedAtAfter(LocalDateTime date);
}
