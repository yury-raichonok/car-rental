package com.example.carrental.repository;

import com.example.carrental.entity.car.CarClass;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarClassRepository extends JpaRepository<CarClass, Long> {

  Optional<CarClass> findByName(String name);
}
