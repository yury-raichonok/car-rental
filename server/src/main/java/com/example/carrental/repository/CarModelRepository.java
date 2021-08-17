package com.example.carrental.repository;

import com.example.carrental.entity.car.CarModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Car Model Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

  /**
   * @param id of car brand.
   * @return list of brand models.
   */
  List<CarModel> findAllByBrandId(Long id);

  /**
   * @param name of car brand.
   * @return list of car models.
   */
  List<CarModel> findAllByBrandNameOrderByName(String name);

  /**
   * @param name of car model.
   * @param brandName name of brand.
   * @return optional car model.
   */
  Optional<CarModel> findByNameAndBrandName(String name, String brandName);
}
