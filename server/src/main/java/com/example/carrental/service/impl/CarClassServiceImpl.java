package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
import static com.example.carrental.constants.ApplicationConstants.SORT_BY_NAME;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for Car Classes.
 * <p>
 * This class performs the CRUD operations for Car Brands.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CarClassServiceImpl implements CarClassService {

  private final CarClassMapper carClassMapper;
  private final CarClassRepository carClassRepository;
  private final CarClassTranslationService carClassTranslationService;

  /**
   * @param language selected language.
   * @return list of car classes.
   * @throws NoContentException if list of car classes if empty.
   */
  @Override
  public List<CarClassNameResponse> findAll(String language) throws NoContentException {
    var classes = carClassRepository.findAll(Sort.by(SORT_BY_NAME));
    List<CarClassNameResponse> classesResponse = new ArrayList<>();
    classes.forEach(carClass -> {
      carClassTranslationService.setTranslation(carClass, language);
      classesResponse.add(carClassMapper.carClassToCarClassNameResponse(carClass));
    });
    if (classesResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return classesResponse;
  }

  /**
   * @param pageable data.
   * @return page of car classes with translations.
   */
  @Override
  public Page<CarClassNameWithTranslationsResponse> findAllPaged(Pageable pageable) {
    var classes = carClassRepository.findAll(pageable);
    List<CarClassNameWithTranslationsResponse> classesResponse = new ArrayList<>();
    classes.forEach(carClass -> classesResponse
        .add(carClassMapper.carClassToCarClassNameWithTranslationsResponse(carClass)));
    return new PageImpl<>(classesResponse, classes.getPageable(), classes.getTotalElements());
  }

  /**
   * @param id of car class.
   * @return car class.
   * @throws IllegalStateException if car class with such id does not exists.
   */
  @Override
  public CarClass findById(Long id) {
    return carClassRepository.findById(id).orElseThrow(() -> {
      log.error("Car class with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car class with id %d does not exists", id));
    });
  }

  /**
   * @param createCarClassRequest data.
   * @throws EntityAlreadyExistsException if car class with such name already exists.
   */
  @Override
  @Transactional
  public void create(CreateCarClassRequest createCarClassRequest)
      throws EntityAlreadyExistsException {
    if (carClassRepository.findByName(createCarClassRequest.getNameEn()).isPresent()) {
      log.error("Car class with name {} already exists", createCarClassRequest.getNameEn());
      throw new EntityAlreadyExistsException(String.format("Car class with name %s already exists",
          createCarClassRequest.getNameEn()));
    }
    var carClassEn = CarClass.builder().name(createCarClassRequest.getNameEn())
        .createdAt(LocalDateTime.now()).build();
    carClassRepository.save(carClassEn);
    carClassTranslationService.create(createCarClassRequest, carClassEn);
  }

  /**
   * @param id of updated car class.
   * @param createCarClassRequest data for updating.
   */
  @Override
  @Transactional
  public void update(Long id, CreateCarClassRequest createCarClassRequest) {
    var carClass = findById(id);
    carClass.setName(createCarClassRequest.getNameEn());
    carClass.setChangedAt(LocalDateTime.now());
    carClassRepository.save(carClass);
    carClassTranslationService.update(createCarClassRequest, carClass.getCarClassTranslations());
  }
}
