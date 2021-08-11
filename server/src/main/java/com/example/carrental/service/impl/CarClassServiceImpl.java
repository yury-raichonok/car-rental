package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.car.CarClassNameResponse;
import com.example.carrental.controller.dto.car.CarClassNameWithTranslationsResponse;
import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.mapper.CarClassMapper;
import com.example.carrental.repository.CarClassRepository;
import com.example.carrental.service.CarClassService;
import com.example.carrental.service.CarClassTranslationService;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarClassServiceImpl implements CarClassService {

  private final CarClassRepository carClassRepository;
  private final CarClassMapper carClassMapper;
  private final CarClassTranslationService carClassTranslationService;

  @Override
  @Transactional
  public String create(CreateCarClassRequest createCarClassRequest)
      throws EntityAlreadyExistsException {
    Optional<CarClass> carClass = carClassRepository.findByName(createCarClassRequest.getNameEn());
    if (carClass.isPresent()) {
      log.error("Car class with name {} already exists", createCarClassRequest.getNameEn());
      throw new EntityAlreadyExistsException(String.format("Car class with name %s already exists",
          createCarClassRequest.getNameEn()));
    }

    var carClassEn = CarClass.builder().name(createCarClassRequest.getNameEn())
        .createdAt(LocalDateTime.now()).build();

    carClassRepository.save(carClassEn);
    carClassTranslationService.create(createCarClassRequest, carClassEn);

    return "Success";
  }

  @Override
  public List<CarClassNameResponse> findAll(String language) throws NoContentException {
    List<CarClass> classes = carClassRepository.findAll(Sort.by("name"));
    List<CarClassNameResponse> carClassResponse = new ArrayList<>();
    classes.forEach(carClass -> {
      if (!"en".equals(language)) {
        carClassTranslationService.setTranslation(carClass, language);
      }
      carClassResponse.add(carClassMapper.carClassToCarClassNameResponse(carClass));
    });
    if (carClassResponse.isEmpty()) {
      throw new NoContentException("No content");
    }
    return carClassResponse;
  }

  @Override
  public Page<CarClassNameWithTranslationsResponse> findAllPaged(Pageable pageable) {
    Page<CarClass> carClassesPage = carClassRepository.findAll(pageable);
    List<CarClassNameWithTranslationsResponse> carClassResponses = new ArrayList<>();
    carClassesPage.forEach(
        carClass -> carClassResponses
            .add(carClassMapper.carClassToCarClassNameWithTranslationsResponse(carClass)));
    return new PageImpl<>(carClassResponses, carClassesPage.getPageable(),
        carClassesPage.getTotalElements());
  }

  @Override
  public CarClass findById(Long id) {
    Optional<CarClass> optionalCarClass = carClassRepository.findById(id);
    if (optionalCarClass.isEmpty()) {
      log.error("Car class with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car class with id %d does not exists", id));
    }
    return optionalCarClass.get();
  }

  @Override
  @Transactional
  public String update(Long id, CreateCarClassRequest createCarClassRequest) {
    var carClass = findById(id);

    carClass.setName(createCarClassRequest.getNameEn());
    carClass.setChangedAt(LocalDateTime.now());
    carClassRepository.save(carClass);

    carClassTranslationService.update(createCarClassRequest, carClass.getCarClassTranslations());
    return "Success";
  }
}
