package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.CAR_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarEngineType;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.entity.location.Location;
import com.example.carrental.repository.CarCriteriaRepository;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.service.CarClassService;
import com.example.carrental.service.CarClassTranslationService;
import com.example.carrental.service.CarModelService;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
class CarServiceImplTest {

  @Autowired
  private CarServiceImpl carService;

  @MockBean
  private CarRepository carRepository;

  @MockBean
  private CarCriteriaRepository carCriteriaRepository;

  @MockBean
  private CarModelService carModelService;

  @MockBean
  private CarClassService carClassService;

  @MockBean
  private LocationService locationService;

  @MockBean
  private FileStoreService fileStoreService;

  @MockBean
  private CarClassTranslationService carClassTranslationService;

  @MockBean
  private LocationTranslationService locationTranslationService;

  @Test
  void givenValidRequest_whenFindAllProfitableOffers_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var carsResponse = new PageImpl<>(Arrays.asList(Car.builder().id(1L).vin("vin")
        .carClass(carClass).model(carModel).build(), Car.builder().id(2L).vin("vin1")
        .carClass(carClass).model(carModel).build()));
    when(carRepository.findAllByInRentalIsTrue(any(Pageable.class))).thenReturn(carsResponse);
    doNothing().when(carClassTranslationService).setTranslation(any(), any());

    var carProfitableOfferResponse = carService.findAllProfitableOffers(ENGLISH);

    assertThat(carProfitableOfferResponse).isNotNull();
    assertThat(carProfitableOfferResponse.size()).isEqualTo(carsResponse.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllProfitableOffersNoContent_thenThrowNoContentException() {
    when(carRepository.findAllByInRentalIsTrue(any(Pageable.class))).thenReturn(Page.empty());

    assertThrows(NoContentException.class, () -> carService.findAllProfitableOffers(ENGLISH));
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional
        .of(Car.builder().id(1L).vin("vin").carClass(carClass).model(carModel).build());
    when(carRepository.findById(1L)).thenReturn(response);

    var car = carService.findById(1L);

    assertThat(car).isNotNull();
    assertThat(car.getId()).isEqualTo(response.get().getId());
    assertThat(car.getModel()).isEqualTo(carModel);
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(carRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> carService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindByIdInRental_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional
        .of(Car.builder().id(1L).vin("vin").carClass(carClass).model(carModel).build());
    when(carRepository.findByIdAndInRentalIsTrue(1L)).thenReturn(response);

    var car = carService.findByIdInRental(1L);

    assertThat(car).isNotNull();
    assertThat(car.getId()).isEqualTo(response.get().getId());
    assertThat(car.getModel()).isEqualTo(carModel);
  }

  @Test
  void givenRequestWithNotExistingId_whenFindByIdInRental_thenThrowIllegalStateException() {
    when(carRepository.findByIdAndInRentalIsTrue(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> carService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindCarById_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional.of(Car.builder().id(1L).vin("vin").carClass(carClass).dateOfIssue(
        LocalDate.now()).model(carModel).build());
    when(carRepository.findByIdAndInRentalIsTrue(1L)).thenReturn(response);
    doNothing().when(carClassTranslationService).setTranslation(any(), any());
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var carByIdResponse = carService.findCarById(1L, ENGLISH);

    assertThat(carByIdResponse).isNotNull();
    assertThat(carByIdResponse.getId()).isEqualTo(response.get().getId());
    assertThat(carByIdResponse.getModel()).isEqualTo(carModel.getName());
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createCarRequest = CreateCarRequest.builder().brandId(1L).modelId(1L).vin("vin")
        .locationId(1L).carClassId(1L).dateOfIssue(LocalDate.now()).color("color")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5)
        .baggageAmt(3).autoTransmission(true).hasConditioner(true).isInRental(true).costPerHour(
            BigDecimal.valueOf(5)).build();
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var location = Location.builder().id(1L).name("locationName").build();
    when(carRepository.findByVin(createCarRequest.getVin())).thenReturn(Optional.empty());
    when(carModelService.findById(createCarRequest.getModelId())).thenReturn(carModel);
    when(carClassService.findById(createCarRequest.getCarClassId())).thenReturn(carClass);
    when(locationService.findById(createCarRequest.getLocationId())).thenReturn(location);
    when(carRepository.save(any(Car.class))).thenReturn(new Car());

    assertDoesNotThrow(() -> carService.create(createCarRequest));
  }

  @Test
  void givenRequestWithExistingCarVin_whenCreate_thenThrowEntityAlreadyExistsException() {
    var createCarRequest = CreateCarRequest.builder().brandId(1L).modelId(1L).vin("vin")
        .locationId(1L).carClassId(1L).dateOfIssue(LocalDate.now()).color("color")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5)
        .baggageAmt(3).autoTransmission(true).hasConditioner(true).isInRental(true).costPerHour(
            BigDecimal.valueOf(5)).build();

    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional.of(Car.builder().id(1L).vin("vin").carClass(carClass).dateOfIssue(
        LocalDate.now()).model(carModel).build());
    when(carRepository.findByVin(createCarRequest.getVin())).thenReturn(response);

    assertThrows(EntityAlreadyExistsException.class, () -> carService.create(createCarRequest));
  }

  @Test
  void givenValidRequest_whenSearchCars_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var carsResponse = new PageImpl<>(Arrays.asList(Car.builder().id(1L).vin("vin")
        .carClass(carClass).dateOfIssue(LocalDate.now()).model(carModel).build(), Car.builder()
        .id(2L).vin("vin1").dateOfIssue(LocalDate.now()).carClass(carClass).model(carModel)
        .build()));
    var carSearchRequest = new CarSearchRequest();
    when(carCriteriaRepository.findCars(carSearchRequest)).thenReturn(carsResponse);
    doNothing().when(carClassTranslationService).setTranslation(any(), any());
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var carSearchResponse = carService.searchCars(carSearchRequest, ENGLISH);

    assertThat(carSearchResponse).isNotNull();
    assertThat(carSearchResponse.getTotalElements()).isEqualTo(carsResponse.getTotalElements());
  }

  @Test
  void givenValidRequest_whenSearchCarsByAdmin_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var carsResponse = new PageImpl<>(Arrays.asList(Car.builder().id(1L).vin("vin")
        .carClass(carClass).model(carModel).build(), Car.builder().id(2L).vin("vin1")
        .carClass(carClass).model(carModel).build()));
    var carSearchRequest = new CarSearchRequest();
    when(carCriteriaRepository.findCars(carSearchRequest)).thenReturn(carsResponse);
    doNothing().when(carClassTranslationService).setTranslation(any(), any());
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var carAdminSearchResponse = carService.searchCarsByAdmin(carSearchRequest, ENGLISH);

    assertThat(carAdminSearchResponse).isNotNull();
    assertThat(carAdminSearchResponse.getTotalElements())
        .isEqualTo(carsResponse.getTotalElements());
  }

  @Test
  void givenValidRequest_whenUploadCarImage_thenSuccess() {
    var carFile = new MockMultipartFile("carFile", "carFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional.of(Car.builder().id(1L).vin("vin").carClass(carClass).dateOfIssue(
        LocalDate.now()).model(carModel).build());
    var imageLink = "Image link";
    when(carRepository.findById(1L)).thenReturn(response);
    when(fileStoreService.uploadPublicImage(CAR_IMAGE,
        String.format("%s/%s-%s", 1, 1, carFile.getOriginalFilename()), new File(
            Objects.requireNonNull(carFile.getOriginalFilename())))).thenReturn(imageLink);
    when(carRepository.save(any())).thenReturn(new Car());

    carService.uploadCarImage(1L, carFile);

    assertThat(response.get().getCarImageLink()).isEqualTo(imageLink);
  }

  @Test
  void givenRequestWithEmptyCarFile_whenUploadCarImage_thenThrowIllegalStateException() {
    var carFile = new MockMultipartFile("carFile", "carFile.img",
        IMAGE_JPEG.getMimeType(), new byte[0]);

    Exception exception = assertThrows(IllegalStateException.class,
        () -> carService.uploadCarImage(1L, carFile));
    String expectedMessage = "Cannot upload empty file [carFile.img]";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenRequestWithCarFileNotAnImage_whenUploadCarImage_thenThrowIllegalStateException() {
    var carFile = new MockMultipartFile("carFile", "carFile.img",
        "multipart/form-data", "Some dataset...".getBytes());

    Exception exception = assertThrows(IllegalStateException.class,
        () -> carService.uploadCarImage(1L, carFile));
    String expectedMessage = "File must be an image [multipart/form-data]";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenValidRequest_whenUploadCarImageToStorageFailed_thenThrowIllegalStateException() {
    var carFile = new MockMultipartFile("carFile", "carFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional.of(Car.builder().id(1L).vin("vin").carClass(carClass).dateOfIssue(
        LocalDate.now()).model(carModel).build());
    when(carRepository.findById(1L)).thenReturn(response);
    when(fileStoreService.uploadPublicImage(CAR_IMAGE,
        String.format("%s/%s-%s", 1, 1, carFile.getOriginalFilename()), new File(
            Objects.requireNonNull(carFile.getOriginalFilename()))))
        .thenThrow(new IllegalStateException("Save failed"));

    Exception exception = assertThrows(IllegalStateException.class,
        () -> carService.uploadCarImage(1L, carFile));
    String expectedMessage = "Save failed";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var location = Location.builder().id(1L).name("locationName").build();
    var car = Optional.of(Car.builder().id(1L).vin("vin").build());
    when(carRepository.findByVinAndIdIsNot(updateCarRequest.getVin(), 1L))
        .thenReturn(Optional.empty());
    when(carRepository.findById(1L)).thenReturn(car);

    when(carModelService.findModelByNameAndBrandName(updateCarRequest.getModel(),
        updateCarRequest.getBrand())).thenReturn(carModel);
    when(locationService.findById(updateCarRequest.getLocation())).thenReturn(location);
    when(carClassService.findById(updateCarRequest.getCarClass())).thenReturn(carClass);
    when(carRepository.save(any(Car.class))).thenReturn(new Car());

    carService.update(1L, updateCarRequest);

    assertThat(car).isNotEmpty();
    assertThat(car.get().getModel()).isEqualTo(carModel);
    assertThat(car.get().getLocation()).isEqualTo(location);
    assertThat(car.get().getCarClass()).isEqualTo(carClass);
  }

  @Test
  void givenRequestWithExistingCarVin_whenUpdate_thenThrowEntityAlreadyExistsException() {
    var updateCarRequest = UpdateCarRequest.builder().brand("name").model("name").vin("name")
        .location(1L).carClass(1L).dateOfIssue(LocalDate.of(2000, 1, 1)).color("name")
        .bodyType(CarBodyType.COUPE).engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(5)
        .autoTransmission(true).hasConditioner(true).costPerHour(BigDecimal.valueOf(5)).build();
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional.of(Car.builder().id(1L).vin("vin").carClass(carClass).dateOfIssue(
        LocalDate.now()).model(carModel).build());
    when(carRepository.findByVinAndIdIsNot(updateCarRequest.getVin(), 1L)).thenReturn(response);

    assertThrows(EntityAlreadyExistsException.class, () -> carService.update(1L, updateCarRequest));
  }

  @Test
  void givenValidRequest_whenUpdateRentalStatus_thenSuccess() {
    var carClass = CarClass.builder().id(1L).name("carClass").build();
    var carBrand = CarBrand.builder().id(1L).name("carBrand").build();
    var carModel = CarModel.builder().id(1L).name("carModel").brand(carBrand).build();
    var response = Optional.of(Car.builder().id(1L).vin("vin").carClass(carClass).dateOfIssue(
        LocalDate.now()).model(carModel).inRental(false).build());
    when(carRepository.findById(1L)).thenReturn(response);
    when(carRepository.save(any(Car.class))).thenReturn(new Car());

    carService.updateRentalStatus(1L);

    assertTrue(response.get().isInRental());
  }
}