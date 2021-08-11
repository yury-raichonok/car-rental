package com.example.carrental.service.impl;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

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
import com.example.carrental.service.RentalDetailsService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

  private final CarRepository carRepository;
  private final CarCriteriaRepository carCriteriaRepository;
  private final CarModelService carModelService;
  private final CarClassService carClassService;
  private final LocationService locationService;
  private final FileStoreService fileStoreService;
  private final CarClassTranslationService carClassTranslationService;
  private final LocationTranslationService locationTranslationService;
  private final CarMapper carMapper;

  private RentalDetailsService rentalDetailsService;

  @Value("${amazon.region}")
  private String region;
  @Value("${amazon.car.images.bucket}")
  private String carImagesBucket;

  @Autowired
  public void setRentalDetailsService(RentalDetailsService rentalDetailsService) {
    this.rentalDetailsService = rentalDetailsService;
  }

  @Override
  @Transactional
  public String create(CreateCarRequest createCarRequest) throws EntityAlreadyExistsException {
    var optionalCar = carRepository.findByVin(createCarRequest.getVin());
    if (optionalCar.isPresent()) {
      log.error("Car with vin {} already exists", createCarRequest.getVin());
      throw new EntityAlreadyExistsException(String.format("Car with vin %s already exists",
          createCarRequest.getVin()));
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
    return "success";
  }

  @Override
  public List<CarProfitableOfferResponse> findAllProfitableOffers(String language)
      throws NoContentException {
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Direction.ASC, "costPerHour"));
    var cars = carRepository.findAllByInRentalIsTrue(pageable);
    List<CarProfitableOfferResponse> profitableOffersResponse = new ArrayList<>();
    cars.getContent().forEach(car -> {
      if (!"en".equals(language)) {
        var carClass = car.getCarClass();
        carClassTranslationService.setTranslation(carClass, language);
        car.setCarClass(carClass);
      }
      profitableOffersResponse.add(carMapper.carToCarChipOfferResponse(car));
    });
    if (profitableOffersResponse.isEmpty()) {
      throw new NoContentException("No content");
    }
    return profitableOffersResponse;
  }

  @Override
  public Car findById(Long id) {
    var optionalCar = carRepository.findById(id);
    if (optionalCar.isEmpty()) {
      log.error("Car with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car with id %d does not exists", id));
    }
    return optionalCar.get();
  }

  @Override
  public Car findByIdInRental(Long id) {
    var optionalCar = carRepository.findByIdAndInRentalIsTrue(id);
    if (optionalCar.isEmpty()) {
      log.error("Car with id {} does not exist, or set as \"Not in rental\"", id);
      throw new IllegalStateException(
          String.format("Car with id %d does not exists, or set as \"Not in rental\"", id));
    }
    return optionalCar.get();
  }

  @Override
  public CarByIdResponse findCarById(Long id, String language) {
    var car = findByIdInRental(id);

    if (!"en".equals(language)) {
      var carClass = car.getCarClass();
      carClassTranslationService.setTranslation(carClass, language);
      car.setCarClass(carClass);

      var location = car.getLocation();
      locationTranslationService.setTranslation(location, language);
      car.setLocation(location);
    }

    var rentalDetails = rentalDetailsService.getRentalDetails();

    BigDecimal costPerHourUpToWeek = car.getCostPerHour()
        .multiply(rentalDetails.getFromDayToWeekCoefficient());
    BigDecimal costPerHourMoreThanWeek = car.getCostPerHour()
        .multiply(rentalDetails.getFromWeekCoefficient());

    return carMapper
        .carAndRentalDetailsToCarByIdResponse(car, costPerHourUpToWeek, costPerHourMoreThanWeek);
  }

  @Override
  public Page<CarSearchResponse> searchCars(CarSearchRequest carSearchRequest, String language) {
    var carsPage = carCriteriaRepository.findCars(carSearchRequest);
    List<CarSearchResponse> cars = new ArrayList<>();
    carsPage.getContent().forEach(car -> {
      if (!"en".equals(language)) {
        var carClass = car.getCarClass();
        carClassTranslationService.setTranslation(carClass, language);
        car.setCarClass(carClass);

        var location = car.getLocation();
        locationTranslationService.setTranslation(location, language);
        car.setLocation(location);
      }
      cars.add(carMapper.carToCarSearchResponse(car));
    });
    return new PageImpl<>(cars, carsPage.getPageable(), carsPage.getTotalElements());
  }

  @Override
  public Page<CarAdminSearchResponse> searchCarsByAdmin(
      CarSearchRequest carSearchRequest, String language) {
    var carsPage = carCriteriaRepository.findCars(carSearchRequest);
    List<CarAdminSearchResponse> cars = new ArrayList<>();
    carsPage.getContent().forEach(car -> {
      if (!"en".equals(language)) {
        var carClass = car.getCarClass();
        carClassTranslationService.setTranslation(carClass, language);
        car.setCarClass(carClass);

        var location = car.getLocation();
        locationTranslationService.setTranslation(location, language);
        car.setLocation(location);
      }
      cars.add(carMapper.carToCarCarAdminSearchResponse(car));
    });
    return new PageImpl<>(cars, carsPage.getPageable(), carsPage.getTotalElements());
  }

  @Override
  @Transactional
  public String update(Long id, UpdateCarRequest updateCarRequest)
      throws EntityAlreadyExistsException {
    var optionalCar = carRepository.findByVin(updateCarRequest.getVin());

    if (optionalCar.isPresent() && !optionalCar.get().getId().equals(id)) {
      log.error("Car with vin {} already exists", updateCarRequest.getVin());
      throw new EntityAlreadyExistsException(String.format("Car with vin %s already exists",
          updateCarRequest.getVin()));
    }

    Car car = findById(id);
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
    return "Success";
  }

  @Override
  @Transactional
  public String updateRentalStatus(Long id) {
    var car = findById(id);
    var inRental = car.isInRental();

    car.setInRental(!inRental);
    carRepository.save(car);
    return "Success";
  }

  @Override
  @Transactional
  public String uploadCarImage(Long id, MultipartFile carFile) {
    if (carFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", carFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          carFile.getSize()));
    }

    if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType())
        .contains(carFile.getContentType())) {
      log.error("File must be an image [{}]", carFile.getContentType());
      throw new IllegalStateException(String.format("File must be an image [%s]",
          carFile.getContentType()));
    }

    var car = findById(id);

    var filename = String.format("%s/%s-%s", id, id, carFile.getOriginalFilename());
    var imageLink = String.format("https://%s.s3.%s.amazonaws.com/%s",
        carImagesBucket, region, filename);

    try {
      File file = new File(Objects.requireNonNull(carFile.getOriginalFilename()));
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(carFile.getBytes());
      fileOutputStream.close();

      fileStoreService.uploadPublicFile(carImagesBucket, filename, file);

      file.delete();
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }

    car.setCarImageLink(imageLink);
    car.setChangedAt(LocalDateTime.now());
    carRepository.save(car);
    return "Success";
  }
}
