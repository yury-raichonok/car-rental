package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarClassTranslation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CarClassTranslationService {

  void create(CreateCarClassRequest createCarClassRequest, CarClass carClass);

  void update(CreateCarClassRequest createCarClassRequest, List<CarClassTranslation> translations);

  void setTranslation(CarClass carClass, String language);
}
