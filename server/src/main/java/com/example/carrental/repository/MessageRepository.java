package com.example.carrental.repository;

import com.example.carrental.entity.message.Message;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  Page<Message> findAllByReadedFalse(Pageable pageable);

  int countAllByReadedFalse();

  int countAllBySentDateAfter(LocalDateTime date);
}
