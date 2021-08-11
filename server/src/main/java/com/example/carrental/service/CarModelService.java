package com.example.carrental.service;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CarModelService {

  String create(CreateCarModelRequest createCarModelRequest)
      throws EntityAlreadyExistsException;

  List<CarModelResponse> findAll() throws NoContentException;

  Page<CarModelBrandNameResponse> findAllModelsWithBrandName(Pageable pageable);

  CarModel findById(Long id);

  CarModel findModelByNameAndBrandName(String name, String brandName);

  List<CarModelResponse> findModelsByBrandId(Long id) throws NoContentException;

  List<CarModelResponse> findModelsByBrandName(String name) throws NoContentException;

  String update(Long id, UpdateCarModelRequest updateCarModelRequest)
      throws EntityAlreadyExistsException;
}
