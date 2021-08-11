package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CarModelService {

  List<CarModelResponse> findAll();

  Page<CarModelBrandNameResponse> findAllModelsWithBrandName(Pageable pageable);

  CarModel getById(Long id);

  String create(CreateCarModelRequest createCarModelRequest)
      throws EntityAlreadyExistsException;

  String update(Long id, UpdateCarModelRequest updateCarModelRequest)
      throws EntityAlreadyExistsException;

  CarModel findModelByNameAndBrandName(String name, String brandName);

  List<CarModelResponse> findModelsByBrandName(String name);

  List<CarModelResponse> findModelsByBrandId(Long id);
}
