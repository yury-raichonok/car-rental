package com.example.carrental.repository;

import com.example.carrental.entity.car.CarModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

  List<CarModel> findAllByBrandId(Long id);

  List<CarModel> findAllByBrandNameOrderByName(String name);

  Optional<CarModel> findByNameAndBrandName(String name, String brandName);
}
