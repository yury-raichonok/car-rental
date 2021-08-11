package com.example.carrental.repository;

import com.example.carrental.entity.car.CarClassTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarClassTranslationRepository extends JpaRepository<CarClassTranslation, Long> {

}
