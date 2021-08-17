package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

import com.example.carrental.controller.dto.car.CarAdminSearchResponse;
import com.example.carrental.controller.dto.car.CarByIdResponse;
import com.example.carrental.controller.dto.car.CarProfitableOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.service.CarService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The controller for Car REST endpoints.
 * <p>
 * This class handles the CRUD operations for Cars, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/cars")
@Validated
public class CarController {

  private final CarService carService;

  /**
   * Handle the /cars/profitable endpoint.
   * @param language selected language.
   * @return list of 3 cheapest cars.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified cars.
   * @throws NoContentException if list of profitable car offers is empty.
   */
  @GetMapping(path = "/profitable")
  public ResponseEntity<List<CarProfitableOfferResponse>> findAllProfitableOffers(
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) throws NoContentException {
    var cars = carService.findAllProfitableOffers(language);
    return new ResponseEntity<>(cars, HttpStatus.OK);
  }

  /**
   * Handle the /cars/search/{id} endpoint.
   * @param id of the car.
   * @param language selected language.
   * @return data of selected car.
   */
  @GetMapping(path = "/search/{id}")
  public ResponseEntity<CarByIdResponse> findCarById(@PathVariable Long id,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var carSearchByIdResponse = carService.findCarById(id, language);
    return new ResponseEntity<>(carSearchByIdResponse, HttpStatus.OK);
  }

  /**
   * Handle the /cars endpoint.
   * @param createCarRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully created new car.
   * 406: unable to create car, because car with same vin already exists.
   * @throws EntityAlreadyExistsException if car with same vin already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(@Valid @RequestBody CreateCarRequest createCarRequest)
      throws EntityAlreadyExistsException {
    carService.create(createCarRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /cars/search endpoint.
   * @param carSearchRequest search request parameters.
   * @param language selected language.
   * @return list of cars by specified parameters.
   */
  @PostMapping(path = "/search")
  public ResponseEntity<Page<CarSearchResponse>> searchCars(
      @Valid @RequestBody CarSearchRequest carSearchRequest,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var carsPageResponse = carService.searchCars(carSearchRequest, language);
    return new ResponseEntity<>(carsPageResponse, HttpStatus.OK);
  }

  /**
   * Handle the /cars/search endpoint.
   * @param carSearchRequest search request parameters.
   * @param language selected language.
   * @return list of cars by specified parameters.
   */
  @PostMapping(path = "/search/admin")
  public ResponseEntity<Page<CarAdminSearchResponse>> searchCarsByAdmin(
      @Valid @RequestBody CarSearchRequest carSearchRequest,
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) {
    var carsAdminPageResponse = carService.searchCarsByAdmin(carSearchRequest, language);
    return new ResponseEntity<>(carsAdminPageResponse, HttpStatus.OK);
  }

  /**
   * Handle the /cars/{id}/upload/image endpoint.
   * @param id of the car for which the image is loaded.
   * @param carFile image of car.
   * Return one of the following status codes:
   * 200: successfully uploaded car file.
   * 400: if bad request or invalid input parameters.
   */
  @PostMapping(path = "/{id}/upload/image")
  public ResponseEntity<HttpStatus> uploadCarImage(@NotNull @Positive @PathVariable Long id,
      @NotNull @RequestParam MultipartFile carFile) {
    carService.uploadCarImage(id, carFile);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /cars/{id} endpoint.
   * @param id of the car which updated.
   * @param updateCarRequest request with parameters.
   * Return one of the following status codes:
   * 200: car data successfully updated.
   * 400: if bad request or invalid input parameters.
   * 406: unable to update car, because car with same vin already exists.
   * @throws EntityAlreadyExistsException if car with same vin already exists.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateCarRequest updateCarRequest) throws EntityAlreadyExistsException {
    carService.update(id, updateCarRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /cars/update/status/{id} endpoint.
   * @param id of the car which rental status updated.
   * Return one of the following status codes:
   * 200: car rental status successfully updated (in rental / not in rental).
   * 400: if bad request or invalid input parameters.
   */
  @PutMapping(path = "/update/status/{id}")
  public ResponseEntity<HttpStatus> updateRentalStatus(@NotNull @Positive @PathVariable Long id) {
    carService.updateRentalStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }


}
