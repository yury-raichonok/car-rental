package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.SORT_BY_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.repository.CarClassRepository;
import com.example.carrental.service.CarClassTranslationService;
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
import org.springframework.data.domain.Sort;

@SpringBootTest
@AutoConfigureMockMvc
class CarClassServiceImplTest {

  @Autowired
  private CarClassServiceImpl carClassService;

  @MockBean
  private CarClassRepository carClassRepository;

  @MockBean
  private CarClassTranslationService carClassTranslationService;

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var response = Arrays.asList(CarClass.builder().id(1L).name("name").build(),
        CarClass.builder().id(2L).name("name1").build());
    when(carClassRepository.findAll(Sort.by(SORT_BY_NAME))).thenReturn(response);
    doNothing().when(carClassTranslationService).setTranslation(any(), any());

    var carClassNameResponse = carClassService.findAll(ENGLISH);

    assertThat(carClassNameResponse).isNotNull();
    assertThat(carClassNameResponse.size()).isEqualTo(response.size());
  }

  @Test
  void givenValidRequest_whenFindAllWithNoContent_thenThrowNoContentException() {
    var response = new ArrayList<CarClass>();
    when(carClassRepository.findAll(Sort.by(SORT_BY_NAME))).thenReturn(response);
    doNothing().when(carClassTranslationService).setTranslation(any(), any());

    assertThrows(NoContentException.class, () -> carClassService.findAll(ENGLISH));
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenSuccess() {
    var response = new PageImpl<>(Arrays.asList(CarClass.builder().id(1L).name("name").build(),
        CarClass.builder().id(2L).name("name1").build()));
    var pageable = Pageable.ofSize(10).withPage(0);
    when(carClassRepository.findAll(pageable)).thenReturn(response);

    var carClassNameWithTranslationsResponse = carClassService.findAllPaged(pageable);

    assertThat(carClassNameWithTranslationsResponse).isNotNull();
    assertThat(carClassNameWithTranslationsResponse.getTotalElements())
        .isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var response = Optional.of(CarClass.builder().id(1L).name("name").build());
    when(carClassRepository.findById(1L)).thenReturn(response);

    var carClass = carClassService.findById(1L);

    assertThat(carClass).isNotNull();
    assertThat(carClass.getId()).isEqualTo(response.get().getId());
    assertThat(carClass.getName()).isEqualTo(response.get().getName());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(carClassRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> carClassService.findById(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createCarClassRequest = CreateCarClassRequest.builder().nameBe("nameBe").nameEn("nameEn")
        .nameRu("nameRu").build();
    var carClass = CarClass.builder().name("carClassEn").build();
    when(carClassRepository.findByName(createCarClassRequest.getNameEn()))
        .thenReturn(Optional.empty());
    when(carClassRepository.save(any())).thenReturn(carClass);
    doNothing().when(carClassTranslationService).create(any(), any());

    carClassService.create(createCarClassRequest);

    assertThat(carClass).isNotNull();
  }

  @Test
  void givenRequestWithExistingName_whenCreate_thenThrowEntityAlreadyExistsException() {
    var createCarClassRequest = CreateCarClassRequest.builder().nameBe("nameBe").nameEn("nameEn")
        .nameRu("nameRu").build();
    var carClass = Optional.of(CarClass.builder().name("carClassEn").build());
    when(carClassRepository.findByName(createCarClassRequest.getNameEn())).thenReturn(carClass);

    Exception exception = assertThrows(EntityAlreadyExistsException.class,
        () -> carClassService.create(createCarClassRequest));
    String expectedMessage = "Car class with name nameEn already exists";
    String actualMessage = exception.getMessage();
    System.out.println(expectedMessage + " - " + actualMessage);
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var createCarClassRequest = CreateCarClassRequest.builder().nameBe("nameBe").nameEn("nameEn1")
        .nameRu("nameRu").build();
    var response = Optional.of(CarClass.builder().name("carClassEn").build());
    var carClassResponse = CarClass.builder().name("carClassEn").build();
    when(carClassRepository.findById(1L)).thenReturn(response);
    when(carClassRepository.save(any())).thenReturn(carClassResponse);
    doNothing().when(carClassTranslationService).update(any(), any());

    carClassService.update(1L, createCarClassRequest);

    assertThat(createCarClassRequest.getNameEn()).isEqualTo(response.get().getName());
  }
}