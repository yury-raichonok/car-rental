package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.mapper.CarModelMapper;
import com.example.carrental.repository.CarModelRepository;
import com.example.carrental.service.CarBrandService;
import com.example.carrental.service.CarModelService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarModelServiceImpl implements CarModelService {

  private final CarModelRepository carModelRepository;
  private final CarBrandService carBrandService;
  private final CarModelMapper carModelMapper;

  @Override
  public List<CarModelResponse> findAll() throws NoContentException {
    var models = carModelRepository.findAll();
    List<CarModelResponse> modelsResponse = new ArrayList<>();
    models.forEach(model -> modelsResponse.add(carModelMapper.carModelToCarModelResponse(model)));
    if (modelsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return modelsResponse;
  }

  @Override
  public Page<CarModelBrandNameResponse> findAllModelsWithBrandName(Pageable pageable) {
    var modelsPage = carModelRepository.findAll(pageable);
    List<CarModelBrandNameResponse> modelsResponse = new ArrayList<>();
    modelsPage.forEach(model -> modelsResponse
        .add(carModelMapper.carModelToCarModelBrandNameResponse(model)));
    return new PageImpl<>(modelsResponse, modelsPage.getPageable(), modelsPage.getTotalElements());
  }

  @Override
  public CarModel findById(Long id) {
    return carModelRepository.findById(id).orElseThrow(() -> {
      log.error("Car model with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car model with id %d does not exists", id));
    });
  }

  @Override
  public CarModel findModelByNameAndBrandName(String name, String brandName) {
    return carModelRepository.findByNameAndBrandName(name, brandName).orElseThrow(() -> {
      log.error("Car model with brand name {} and name {} does not exist", brandName, name);
      throw new IllegalStateException(String
          .format("Car model with brand name %s and name %s does not exist", brandName, name));
    });
  }

  @Override
  public List<CarModelResponse> findModelsByBrandId(Long id) throws NoContentException {
    carBrandService.findById(id);
    var models = carModelRepository.findAllByBrandId(id);
    List<CarModelResponse> modelsResponse = new ArrayList<>();
    models.forEach(model -> modelsResponse.add(carModelMapper.carModelToCarModelResponse(model)));
    if (modelsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return modelsResponse;
  }

  @Override
  public List<CarModelResponse> findModelsByBrandName(String name) throws NoContentException {
    carBrandService.findByName(name);
    var models = carModelRepository.findAllByBrandNameOrderByName(name);
    List<CarModelResponse> modelsResponse = new ArrayList<>();
    models.forEach(model -> modelsResponse.add(carModelMapper.carModelToCarModelResponse(model)));
    if (modelsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return modelsResponse;
  }

  @Override
  @Transactional
  public void create(CreateCarModelRequest createCarModelRequest)
      throws EntityAlreadyExistsException {
    var carBrand = carBrandService.findById(createCarModelRequest.getBrandId());
    validateCarModelByBrandAndName(createCarModelRequest.getName(), carBrand.getName());
    carModelRepository.save(CarModel.builder()
        .name(createCarModelRequest.getName())
        .brand(carBrand)
        .createdAt(LocalDateTime.now())
        .build());
  }

  @Override
  @Transactional
  public void update(Long id, UpdateCarModelRequest updateCarModelRequest)
      throws EntityAlreadyExistsException {
    validateCarModelByBrandAndName(updateCarModelRequest.getBrand(),
        updateCarModelRequest.getName());
    var carModel = findById(id);
    var carBrand = carBrandService.findByName(updateCarModelRequest.getBrand());

    carModel.setName(updateCarModelRequest.getName());
    carModel.setBrand(carBrand);
    carModel.setChangedAt(LocalDateTime.now());
    carModelRepository.save(carModel);
  }

  private void validateCarModelByBrandAndName(String modelName, String brandName)
      throws EntityAlreadyExistsException {
    if (carModelRepository.findByNameAndBrandName(modelName, brandName).isPresent()) {
      log.error("Car model of brand {} with name {} already exists", brandName, modelName);
      throw new EntityAlreadyExistsException(String
          .format("Car model of brand %s with name %s already exists", brandName, modelName));
    }
  }
}
