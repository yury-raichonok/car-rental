package com.example.carrental.repository;

import com.example.carrental.entity.car.Car;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

  Page<Car> findAllByInRentalIsTrue(Pageable pageable);

  Optional<Car> findByIdAndInRentalIsTrue(Long id);

  Optional<Car> findByVin(String Vin);
}
