package com.example.carrental.repository;

import com.example.carrental.entity.faq.FaqTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqTranslationRepository extends JpaRepository<FaqTranslation, Long> {

}
