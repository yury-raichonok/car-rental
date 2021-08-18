package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.repository.CarModelRepository;
import com.example.carrental.service.CarBrandService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class CarModelServiceImplTest {

  @Autowired
  private CarModelServiceImpl carModelService;

  @MockBean
  private CarBrandService carBrandService;

  @MockBean
  private CarModelRepository carModelRepository;

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var response = Arrays.asList(CarModel.builder().id(1L).name("name").build(),
        CarModel.builder().id(2L).name("name1").build());
    when(carModelRepository.findAll()).thenReturn(response);

    var carModelResponse = carModelService.findAll();

    assertThat(carModelResponse).isNotNull();
    assertThat(carModelResponse.size()).isEqualTo(response.size());
  }

  @Test
  void givenValidRequest_whenFindAllNoContent_thenThrowNoContentException() {
    var response = new ArrayList<CarModel>();
    when(carModelRepository.findAll()).thenReturn(response);

    assertThrows(NoContentException.class, () -> carModelService.findAll());
  }

  @Test
  void givenValidRequest_whenFindAllModelsWithBrandName_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Arrays.asList(CarModel.builder().id(1L).name("name").build(),
        CarModel.builder().id(2L).name("name1").build()));
    when(carModelRepository.findAll(pageable)).thenReturn(response);

    var carModelBrandNameResponse = carModelService.findAllModelsWithBrandName(pageable);

    assertThat(carModelBrandNameResponse).isNotNull();
    assertThat(carModelBrandNameResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var response = Optional.of(CarModel.builder().id(1L).name("name").build());
    when(carModelRepository.findById(1L)).thenReturn(response);

    var carModel = carModelService.findById(1L);

    assertThat(carModel).isNotNull();
    assertThat(carModel.getId()).isEqualTo(response.get().getId());
    assertThat(carModel.getName()).isEqualTo(response.get().getName());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(carModelRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> carModelService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindModelByNameAndBrandName_thenSuccess() {
    var response = Optional.of(CarModel.builder().id(1L).name("name").build());
    when(carModelRepository.findByNameAndBrandName("modelName", "brandName")).thenReturn(response);

    var carModel = carModelService.findModelByNameAndBrandName("modelName", "brandName");

    assertThat(carModel).isNotNull();
    assertThat(carModel.getId()).isEqualTo(response.get().getId());
    assertThat(carModel.getName()).isEqualTo(response.get().getName());
  }

  @Test
  void givenRequestWithNotExistingModel_whenFindModelByNameAndBrandName_thenThrowIllegalStateException() {
    when(carModelRepository.findByNameAndBrandName("modelName", "brandName"))
        .thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class,
        () -> carModelService.findModelByNameAndBrandName("modelName", "brandName"));
  }

  @Test
  void givenValidRequest_whenFindModelsByBrandId_thenSuccess() {
    var response = Arrays.asList(CarModel.builder().id(1L).name("name").build(),
        CarModel.builder().id(2L).name("name1").build());
    when(carModelRepository.findAllByBrandId(1L)).thenReturn(response);

    var carModelResponse = carModelService.findModelsByBrandId(1L);

    assertThat(carModelResponse).isNotNull();
    assertThat(carModelResponse.size()).isEqualTo(response.size());
  }

  @Test
  void givenValidRequest__whenFindModelsByBrandIdNoContent_thenThrowNoContentException() {
    var response = new ArrayList<CarModel>();
    when(carModelRepository.findAllByBrandId(1L)).thenReturn(response);

    assertThrows(NoContentException.class, () -> carModelService.findModelsByBrandId(1L));
  }

  @Test
  void givenValidRequest_whenFindModelsByBrandName_thenSuccess() {
    var response = Arrays.asList(CarModel.builder().id(1L).name("name").build(),
        CarModel.builder().id(2L).name("name1").build());
    when(carModelRepository.findAllByBrandNameOrderByName("name")).thenReturn(response);

    var carModelResponse = carModelService.findModelsByBrandName("name");

    assertThat(carModelResponse).isNotNull();
    assertThat(carModelResponse.size()).isEqualTo(response.size());
  }

  @Test
  void givenValidRequest__whenFindModelsByBrandNameNoContent_thenThrowNoContentException() {
    var response = new ArrayList<CarModel>();
    when(carModelRepository.findAllByBrandNameOrderByName("name")).thenReturn(response);

    assertThrows(NoContentException.class, () -> carModelService.findModelsByBrandName("name"));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createCarModelRequest = CreateCarModelRequest.builder().brandId(1L).name("name").build();
    var carBrand = CarBrand.builder().id(1L).name("brandName").build();
    when(carBrandService.findById(createCarModelRequest.getBrandId())).thenReturn(carBrand);
    when(carModelRepository
        .findByNameAndBrandName(createCarModelRequest.getName(), carBrand.getName()))
        .thenReturn(Optional.empty());
    when(carModelRepository.save(any(CarModel.class))).thenReturn(new CarModel());

    carModelService.create(createCarModelRequest);

    assertThat(carBrand).isNotNull();
  }

  @Test
  void givenRequestWithExistingBrandModel_whenCreate_thenThrowEntityAlreadyExistsException() {
    var createCarModelRequest = CreateCarModelRequest.builder().brandId(1L).name("name").build();
    var carBrand = CarBrand.builder().id(1L).name("brandName").build();
    when(carBrandService.findById(createCarModelRequest.getBrandId())).thenReturn(carBrand);
    when(carModelRepository
        .findByNameAndBrandName(createCarModelRequest.getName(), carBrand.getName()))
        .thenReturn(Optional.of(new CarModel()));

    assertThrows(EntityAlreadyExistsException.class,
        () -> carModelService.create(createCarModelRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("brandName").name("modelName")
        .build();
    var carBrand = CarBrand.builder().id(1L).name("brandName").build();
    var carModel = Optional.of(CarModel.builder().id(1L).name("oldName").build());
    when(carModelRepository.findByNameAndBrandName(updateCarModelRequest.getBrand(),
        updateCarModelRequest.getName())).thenReturn(Optional.empty());
    when(carModelRepository.findById(1L)).thenReturn(carModel);
    when(carBrandService.findByName(updateCarModelRequest.getBrand())).thenReturn(carBrand);
    when(carModelRepository.save(any(CarModel.class))).thenReturn(new CarModel());

    carModelService.update(1L, updateCarModelRequest);

    assertThat(carModel).isNotNull();
    assertThat(carModel.get().getName()).isEqualTo(updateCarModelRequest.getName());
    assertThat(carModel.get().getBrand()).isEqualTo(carBrand);
  }

  @Test
  void givenRequestWithExistingBrandModel_whenUpdate_thenThrowEntityAlreadyExistsException() {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("brandName").name("modelName")
        .build();
    when(carModelRepository.findByNameAndBrandName(updateCarModelRequest.getBrand(),
        updateCarModelRequest.getName())).thenReturn(Optional.of(new CarModel()));

    assertThrows(EntityAlreadyExistsException.class,
        () -> carModelService.update(1L, updateCarModelRequest));
  }
}