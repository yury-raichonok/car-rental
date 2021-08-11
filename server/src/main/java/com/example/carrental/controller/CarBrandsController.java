package com.example.carrental.controller;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.service.CarBrandService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/brands")
@Validated
public class CarBrandsController {

  private final CarBrandService carBrandService;

  @GetMapping
  public ResponseEntity<List<CarBrandResponse>> findAllBrandsWithRentalOffers() {
    var brands = carBrandService.findAllBrandsWithRentalOffers();
    if (brands.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(brands, HttpStatus.OK);
  }

  @GetMapping(path = "/all")
  public ResponseEntity<List<CarBrandResponse>> findAll() {
    var brands = carBrandService.findAll();
    if (brands.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(brands, HttpStatus.OK);
  }

  @GetMapping(path = "/all/paged")
  public ResponseEntity<Page<CarBrandResponse>> findAllPaged(Pageable pageable) {
    var brands = carBrandService.findAllPaged(pageable);
    return new ResponseEntity<>(brands, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestBody CreateCarBrandRequest createCarBrandRequest) {
    try {
      var response = carBrandService.create(createCarBrandRequest);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(
      path = "/{id}/upload/image",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> uploadBrandImage(@NotNull @Positive @PathVariable Long id,
      @NotNull @RequestParam MultipartFile brandFile) {
    try {
      var response = carBrandService.uploadBrandImage(id, brandFile);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody CreateCarBrandRequest createCarBrandRequest) {
    try {
      var response = carBrandService.update(id, createCarBrandRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
