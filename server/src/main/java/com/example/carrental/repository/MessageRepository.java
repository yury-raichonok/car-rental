package com.example.carrental.repository;

import com.example.carrental.entity.message.Message;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Message Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  /**
   * @param pageable data.
   * @return page of messages.
   */
  Page<Message> findAllByReadedFalse(Pageable pageable);

  /**
   * @return amount of new messages.
   */
  int countAllByReadedFalse();

  /**
   * @param date countdown.
   * @return amount of messages after specified date.
   */
  int countAllBySentDateAfter(LocalDateTime date);
}
