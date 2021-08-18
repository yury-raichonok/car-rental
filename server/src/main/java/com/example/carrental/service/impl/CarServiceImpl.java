package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.CAR_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
import static com.example.carrental.service.util.MultipartFileUtil.validateMultipartImageFile;

import com.example.carrental.controller.dto.car.CarAdminSearchResponse;
import com.example.carrental.controller.dto.car.CarByIdResponse;
import com.example.carrental.controller.dto.car.CarProfitableOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.entity.car.Car;
import com.example.carrental.mapper.CarMapper;
import com.example.carrental.repository.CarCriteriaRepository;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.service.CarClassService;
import com.example.carrental.service.CarClassTranslationService;
import com.example.carrental.service.CarModelService;
import com.example.carrental.service.CarService;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for Cars.
 * <p>
 * This class performs the CRUD operations for Cars.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

  private static final String PROFITABLE_OFFERS_SORT_PARAMETER = "costPerHour";
  private static final int PROFITABLE_OFFERS_DISPLAYED_AMOUNT = 3;
  private static final int PROFITABLE_OFFERS_DISPLAYED_PAGE = 0;

  private final CarRepository carRepository;
  private final CarCriteriaRepository carCriteriaRepository;
  private final CarModelService carModelService;
  private final CarClassService carClassService;
  private final LocationService locationService;
  private final FileStoreService fileStoreService;
  private final CarClassTranslationService carClassTranslationService;
  private final LocationTranslationService locationTranslationService;
  private final CarMapper carMapper;

  /**
   * @param language selected language.
   * @return list of maximum 3 car profitable offers (cheapest by cost).
   * @throws NoContentException if car list is empty.
   */
  @Override
  public List<CarProfitableOfferResponse> findAllProfitableOffers(String language)
      throws NoContentException {
    var pageable = PageRequest.of(PROFITABLE_OFFERS_DISPLAYED_PAGE,
        PROFITABLE_OFFERS_DISPLAYED_AMOUNT,
        Sort.by(Direction.ASC, PROFITABLE_OFFERS_SORT_PARAMETER));
    var cars = carRepository.findAllByInRentalIsTrue(pageable);
    List<CarProfitableOfferResponse> carsResponse = new ArrayList<>();
    cars.getContent().forEach(car -> {
      carClassTranslationService.setTranslation(car.getCarClass(), language);
      carsResponse.add(carMapper.carToCarChipOfferResponse(car));
    });
    if (carsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return carsResponse;
  }

  /**
   * @param id of car.
   * @return car.
   * @throws IllegalStateException if car with such if does not exists.
   */
  @Override
  public Car findById(Long id) {
    return carRepository.findById(id).orElseThrow(() -> {
      log.error("Car with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car with id %d does not exists", id));
    });
  }

  /**
   * @param id of car
   * @return car.
   * @throws IllegalStateException if car with such if does not exists, or has status inRental false.
   */
  @Override
  public Car findByIdInRental(Long id) {
    return carRepository.findByIdAndInRentalIsTrue(id).orElseThrow(() -> {
      log.error("Car with id {} does not exist, or set as \"Not in rental\"", id);
      throw new IllegalStateException(
          String.format("Car with id %d does not exists, or set as \"Not in rental\"", id));
    });
  }

  /**
   * @param id of car.
   * @param language selected language.
   * @return car by id response.
   */
  @Override
  public CarByIdResponse findCarById(Long id, String language) {
    var car = findByIdInRental(id);
    carClassTranslationService.setTranslation(car.getCarClass(), language);
    locationTranslationService.setTranslation(car.getLocation(), language);
    return carMapper.carToCarByIdResponse(car);
  }

  /**
   * @param createCarRequest data for creating.
   * @throws EntityAlreadyExistsException if car with specivied VIN already exists.
   */
  @Override
  @Transactional
  public void create(CreateCarRequest createCarRequest) throws EntityAlreadyExistsException {
    if (carRepository.findByVin(createCarRequest.getVin()).isPresent()) {
      log.error("Car with vin {} already exists", createCarRequest.getVin());
      throw new EntityAlreadyExistsException(
          String.format("Car with vin %s already exists", createCarRequest.getVin()));
    }
    var carModel = carModelService.findById(createCarRequest.getModelId());
    var carClass = carClassService.findById(createCarRequest.getCarClassId());
    var location = locationService.findById(createCarRequest.getLocationId());

    var car = Car
        .builder()
        .model(carModel)
        .carClass(carClass)
        .location(location)
        .vin(createCarRequest.getVin())
        .dateOfIssue(createCarRequest.getDateOfIssue())
        .color(createCarRequest.getColor())
        .bodyType(createCarRequest.getBodyType())
        .isAutomaticTransmission(createCarRequest.isAutoTransmission())
        .engineType(createCarRequest.getEngineType())
        .passengersAmt(createCarRequest.getPassengersAmt())
        .baggageAmt(createCarRequest.getBaggageAmt())
        .hasConditioner(createCarRequest.isHasConditioner())
        .inRental(true)
        .costPerHour(createCarRequest.getCostPerHour())
        .createdAt(LocalDateTime.now())
        .build();

    carRepository.save(car);
  }

  /**
   * @param carSearchRequest search parameters.
   * @param language selected language.
   * @return page of cars response, filtered by parameters.
   */
  @Override
  public Page<CarSearchResponse> searchCars(CarSearchRequest carSearchRequest, String language) {
    var carsPage = carCriteriaRepository.findCars(carSearchRequest);
    List<CarSearchResponse> carsResponse = new ArrayList<>();
    carsPage.getContent().forEach(car -> {
      carClassTranslationService.setTranslation(car.getCarClass(), language);
      locationTranslationService.setTranslation(car.getLocation(), language);
      carsResponse.add(carMapper.carToCarSearchResponse(car));
    });
    return new PageImpl<>(carsResponse, carsPage.getPageable(), carsPage.getTotalElements());
  }

  /**
   * @param carSearchRequest search parameters.
   * @param language selected language.
   * @return page of cars admin search response, filtered by parameters.
   */
  @Override
  public Page<CarAdminSearchResponse> searchCarsByAdmin(
      CarSearchRequest carSearchRequest, String language) {
    var carsPage = carCriteriaRepository.findCars(carSearchRequest);
    List<CarAdminSearchResponse> carsResponse = new ArrayList<>();
    carsPage.getContent().forEach(car -> {
      carClassTranslationService.setTranslation(car.getCarClass(), language);
      locationTranslationService.setTranslation(car.getLocation(), language);
      carsResponse.add(carMapper.carToCarCarAdminSearchResponse(car));
    });
    return new PageImpl<>(carsResponse, carsPage.getPageable(), carsPage.getTotalElements());
  }

  /**
   * @param id of car.
   * @param carFile image of car.
   * @throws IllegalStateException if bad request or invalid input parameters.
   */
  @Override
  @Transactional
  public void uploadCarImage(Long id, MultipartFile carFile) {
    validateMultipartImageFile(carFile);
    String imageLink;
    var car = findById(id);
    var filename = String.format("%s/%s-%s", id, id, carFile.getOriginalFilename());
    var file = new File(Objects.requireNonNull(carFile.getOriginalFilename()));

    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(carFile.getBytes());
      imageLink = fileStoreService.uploadPublicImage(CAR_IMAGE, filename, file);
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }
    try {
      Files.delete(Path.of(file.getPath()));
    } catch (IOException e) {
      log.warn("File was not deleted after upload. Exception: {}", e.getMessage());
    }
    car.setCarImageLink(imageLink);
    car.setChangedAt(LocalDateTime.now());
    carRepository.save(car);
  }

  /**
   * @param id of car.
   * @param updateCarRequest data for car updating.
   * @throws EntityAlreadyExistsException if car with new vin and not current id exists.
   */
  @Override
  @Transactional
  public void update(Long id, UpdateCarRequest updateCarRequest)
      throws EntityAlreadyExistsException {
    if (carRepository.findByVinAndIdIsNot(updateCarRequest.getVin(), id).isPresent()) {
      log.error("Car with vin {} already exists", updateCarRequest.getVin());
      throw new EntityAlreadyExistsException(String.format("Car with vin %s already exists",
          updateCarRequest.getVin()));
    }

    var car = findById(id);
    var carModel = carModelService.findModelByNameAndBrandName(updateCarRequest.getModel(),
        updateCarRequest.getBrand());
    var location = locationService.findById(updateCarRequest.getLocation());
    var carClass = carClassService.findById(updateCarRequest.getCarClass());

    car.setModel(carModel);
    car.setCarClass(carClass);
    car.setVin(updateCarRequest.getVin());
    car.setLocation(location);
    car.setDateOfIssue(updateCarRequest.getDateOfIssue());
    car.setColor(updateCarRequest.getColor());
    car.setBodyType(updateCarRequest.getBodyType());
    car.setEngineType(updateCarRequest.getEngineType());
    car.setPassengersAmt(updateCarRequest.getPassengersAmt());
    car.setBaggageAmt(updateCarRequest.getBaggageAmt());
    car.setAutomaticTransmission(updateCarRequest.isAutoTransmission());
    car.setHasConditioner(updateCarRequest.isHasConditioner());
    car.setCostPerHour(updateCarRequest.getCostPerHour());
    car.setChangedAt(LocalDateTime.now());
    carRepository.save(car);
  }

  /**
   * @param id of updated car.
   * Set status inRental, that is responsible for displaying the car in search results.
   */
  @Override
  @Transactional
  public void updateRentalStatus(Long id) {
    var car = findById(id);
    car.setInRental(!car.isInRental());
    carRepository.save(car);
  }
}
