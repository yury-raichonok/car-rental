package com.example.carrental.repository;

import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.entity.car.Car;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

/**
 * Car Criteria Repository.
 *
 * @author Yury Raichonak
 */
@Repository
public interface CarCriteriaRepository {

  /**
   * @param carSearchRequest search parameters.
   * @return page of cars filtered by parameters.
   */
  Page<Car> findCars(CarSearchRequest carSearchRequest);
}
