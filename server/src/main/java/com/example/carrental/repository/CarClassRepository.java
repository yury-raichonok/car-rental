package com.example.carrental.repository;

import com.example.carrental.entity.car.CarClass;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Car Class Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface CarClassRepository extends JpaRepository<CarClass, Long> {

  /**
   * @param name of car class.
   * @return optional car class.
   */
  Optional<CarClass> findByName(String name);
}
