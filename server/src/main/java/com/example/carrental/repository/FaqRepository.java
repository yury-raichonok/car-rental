package com.example.carrental.repository;

import com.example.carrental.entity.faq.Faq;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Faq Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

  /**
   * @param question of FAQ.
   * @return optional FAQ.
   */
  Optional<Faq> findByQuestion(String question);
}
