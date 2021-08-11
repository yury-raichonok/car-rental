package com.example.carrental.repository;

import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.entity.car.Car;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCriteriaRepository {

  Page<Car> findCars(CarSearchRequest carSearchRequest);
}
