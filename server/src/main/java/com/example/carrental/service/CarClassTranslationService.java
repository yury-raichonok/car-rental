package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarClassTranslation;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * The service for Car Class.
 * <p>
 * This interface describes actions on Car Classes.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface CarClassTranslationService {

  void create(CreateCarClassRequest createCarClassRequest, CarClass carClass);

  void update(CreateCarClassRequest createCarClassRequest, List<CarClassTranslation> translations);

  void setTranslation(CarClass carClass, String language);
}
