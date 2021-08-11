package com.example.carrental.controller;

import com.example.carrental.controller.dto.car.CarChipOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchRequest;
import com.example.carrental.controller.dto.car.CreateCarRequest;
import com.example.carrental.controller.dto.car.UpdateCarRequest;
import com.example.carrental.service.CarService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/cars")
@Validated
public class CarController {

  private final CarService carService;

  @GetMapping(path = "/profitable")
  public ResponseEntity<List<CarChipOfferResponse>> findProfitableCarOffers(
      @NotNull @CookieValue(name = "i18next") String language) {
    var cars = carService.findProfitableCarOffers(language);
    if (cars.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(cars, HttpStatus.OK);
  }

  @PostMapping(path = "/search")
  public ResponseEntity<?> searchCars(@RequestBody CarSearchRequest carSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var carsPageResponse = carService.searchCars(carSearchRequest, language);
      return new ResponseEntity<>(carsPageResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(path = "/search/admin")
  public ResponseEntity<?> searchCarsByAdmin(
      @RequestBody CarSearchRequest carSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var carsAdminPageResponse = carService.searchCarsByAdmin(carSearchRequest, language);
      return new ResponseEntity<>(carsAdminPageResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/search/{id}")
  public ResponseEntity<?> findCarById(@PathVariable Long id,
      @NotNull @CookieValue(name = "i18next") String language) {
    try {
      var carSearchByIdResponse = carService.findCarById(id, language);
      return new ResponseEntity<>(carSearchByIdResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<String> create(@Valid @RequestBody CreateCarRequest createCarRequest) {
    try {
      var response = carService.create(createCarRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(path = "/{id}/upload/image")
  public ResponseEntity<String> uploadCarImage(@NotNull @Positive @PathVariable Long id,
      @NotNull @RequestParam MultipartFile carFile) {
    try {
      var response = carService.uploadCarImage(id, carFile);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateCarRequest updateCarRequest) {
    try {
      var response = carService.update(id, updateCarRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/update/status/{id}")
  public ResponseEntity<String> updateRentalStatus(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = carService.updateRentalStatus(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
