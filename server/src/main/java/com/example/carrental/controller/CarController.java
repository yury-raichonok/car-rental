package com.example.carrental.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/cars")
@Validated
public class CarController {

  private final CarService carService;

  @GetMapping(path = "/profitable")
  public ResponseEntity<List<CarProfitableOfferResponse>> findAllProfitableOffers(
      @NotNull @CookieValue(name = "i18next") String language) throws NoContentException {
    var cars = carService.findAllProfitableOffers(language);
    return new ResponseEntity<>(cars, HttpStatus.OK);
  }

  @GetMapping(path = "/search/{id}")
  public ResponseEntity<CarByIdResponse> findCarById(@PathVariable Long id,
      @NotNull @CookieValue(name = "i18next") String language) {
    var carSearchByIdResponse = carService.findCarById(id, language);
    return new ResponseEntity<>(carSearchByIdResponse, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> create(@Valid @RequestBody CreateCarRequest createCarRequest)
      throws EntityAlreadyExistsException {
    carService.create(createCarRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/search")
  public ResponseEntity<Page<CarSearchResponse>> searchCars(
      @RequestBody CarSearchRequest carSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    var carsPageResponse = carService.searchCars(carSearchRequest, language);
    return new ResponseEntity<>(carsPageResponse, HttpStatus.OK);
  }

  @PostMapping(path = "/search/admin")
  public ResponseEntity<Page<CarAdminSearchResponse>> searchCarsByAdmin(
      @RequestBody CarSearchRequest carSearchRequest,
      @NotNull @CookieValue(name = "i18next") String language) {
    var carsAdminPageResponse = carService.searchCarsByAdmin(carSearchRequest, language);
    return new ResponseEntity<>(carsAdminPageResponse, HttpStatus.OK);
  }

  @PostMapping(path = "/{id}/upload/image")
  public ResponseEntity<HttpStatus> uploadCarImage(@NotNull @Positive @PathVariable Long id,
      @NotNull @RequestParam MultipartFile carFile) {
    carService.uploadCarImage(id, carFile);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateCarRequest updateCarRequest) throws EntityAlreadyExistsException {
    carService.update(id, updateCarRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/update/status/{id}")
  public ResponseEntity<HttpStatus> updateRentalStatus(@NotNull @Positive @PathVariable Long id) {
    carService.updateRentalStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }


}
