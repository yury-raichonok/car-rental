package com.example.carrental.repository;

import com.example.carrental.entity.car.Car;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Car Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

  /**
   * @param pageable request.
   * @return page of cars.
   */
  Page<Car> findAllByInRentalIsTrue(Pageable pageable);

  /**
   * @param id of car.
   * @return optional car by id if it has rental status true.
   */
  Optional<Car> findByIdAndInRentalIsTrue(Long id);

  /**
   * @param vin of car.
   * @return optional car.
   */
  Optional<Car> findByVin(String vin);

  /**
   * @param vin of car.
   * @param id of car.
   * @return optional car if it is not current.
   */
  Optional<Car> findByVinAndIdIsNot(String vin, Long id);
}
