package com.example.carrental.repository;

import com.example.carrental.entity.faq.Faq;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

  Optional<Faq> findByQuestion(String question);
}
