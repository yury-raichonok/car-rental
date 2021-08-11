package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.entity.car.CarBrand;
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
import java.util.Optional;
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
  @Transactional
  public String create(CreateCarModelRequest createCarModelRequest)
      throws EntityAlreadyExistsException {
    CarBrand carBrand = carBrandService.findById(createCarModelRequest.getBrandId());

    Optional<CarModel> optionalCarModel = carModelRepository
        .findByNameAndBrandName(createCarModelRequest.getName(), carBrand.getName());
    if (optionalCarModel.isPresent()) {
      log.error("Car model of brand {} with name {} already exists", carBrand.getName(),
          createCarModelRequest.getName());
      throw new EntityAlreadyExistsException(String
          .format("Car model of brand %s with name %s already exists", carBrand.getName(),
              createCarModelRequest.getName()));
    }

    carModelRepository.save(CarModel.builder()
        .name(createCarModelRequest.getName())
        .brand(carBrand)
        .createdAt(LocalDateTime.now())
        .build());
    return "Success";
  }

  @Override
  public List<CarModelResponse> findAll() throws NoContentException {
    List<CarModel> models = carModelRepository.findAll();
    List<CarModelResponse> modelsResponse = new ArrayList<>();
    models.forEach(model -> modelsResponse.add(carModelMapper.carModelToCarModelResponse(model)));
    if (modelsResponse.isEmpty()) {
      throw new NoContentException("No content");
    }
    return modelsResponse;
  }

  @Override
  public Page<CarModelBrandNameResponse> findAllModelsWithBrandName(Pageable pageable) {
    Page<CarModel> modelsPage = carModelRepository.findAll(pageable);
    List<CarModelBrandNameResponse> carModelBrandNameResponses = new ArrayList<>();
    modelsPage.forEach(
        m -> carModelBrandNameResponses.add(carModelMapper.carModelToCarModelBrandNameResponse(m)));
    return new PageImpl<>(carModelBrandNameResponses, modelsPage.getPageable(),
        modelsPage.getTotalElements());
  }

  @Override
  public CarModel findById(Long id) {
    Optional<CarModel> optionalCarModel = carModelRepository.findById(id);
    if (optionalCarModel.isEmpty()) {
      log.error("Car model with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car model with id %d does not exists", id));
    }
    return optionalCarModel.get();
  }

  @Override
  public CarModel findModelByNameAndBrandName(String name, String brandName) {
    Optional<CarModel> optionalModel = carModelRepository.findByNameAndBrandName(name, brandName);

    if (optionalModel.isEmpty()) {
      log.error("Car model with brand name {} and name {} does not exist", brandName, name);
      throw new IllegalStateException(String
          .format("Car model with brand name %s and name %s does not exist", brandName, name));
    }

    return optionalModel.get();
  }

  @Override
  public List<CarModelResponse> findModelsByBrandId(Long id) throws NoContentException {
    carBrandService.findById(id);

    var models = carModelRepository.findAllByBrandId(id);
    List<CarModelResponse> modelsResponse = new ArrayList<>();

    models.forEach(model -> modelsResponse.add(carModelMapper.carModelToCarModelResponse(model)));

    if (modelsResponse.isEmpty()) {
      throw new NoContentException("No content");
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
      throw new NoContentException("No content");
    }

    return modelsResponse;
  }

  @Override
  @Transactional
  public String update(Long id, UpdateCarModelRequest updateCarModelRequest)
      throws EntityAlreadyExistsException {
    Optional<CarModel> optionalCarModel = carModelRepository
        .findByNameAndBrandName(updateCarModelRequest.getName(), updateCarModelRequest.getBrand());
    if (optionalCarModel.isPresent()) {
      log.error("Car model of brand {} with name {} already exists",
          updateCarModelRequest.getBrand(), updateCarModelRequest.getName());
      throw new EntityAlreadyExistsException(String
          .format("Car model of brand %s with name %s already exists",
              updateCarModelRequest.getBrand(), updateCarModelRequest.getName()));
    }

    CarModel carModel = findById(id);
    CarBrand carBrand = carBrandService.findByName(updateCarModelRequest.getBrand());

    carModel.setName(updateCarModelRequest.getName());
    carModel.setBrand(carBrand);
    carModel.setChangedAt(LocalDateTime.now());
    carModelRepository.save(carModel);
    return "Success";
  }
}
