package com.example.carrental.controller;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.service.CarModelService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/models")
public class CarModelController {

  private final CarModelService carModelService;

  @GetMapping
  public ResponseEntity<List<CarModelResponse>> findAll() {
    var models = carModelService.findAll();
    if (models.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  @GetMapping(path = "/list")
  public ResponseEntity<Page<CarModelBrandNameResponse>> findAllModelsWithBrandName(
      Pageable pageable) {
    var models = carModelService.findAllModelsWithBrandName(pageable);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  @GetMapping(path = "/brand/id/{id}")
  public ResponseEntity<?> findModelsByBrandId(@PathVariable Long id) {
    try {
      var models = carModelService.findModelsByBrandId(id);
      if (models.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(models, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/brand/{name}")
  public ResponseEntity<?> findModelsByBrandName(
      @NotNull @PathVariable String name) {
    try {
      var models = carModelService.findModelsByBrandName(name);
      if (models.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(models, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestBody CreateCarModelRequest createCarModelRequest) {
    try {
      var response = carModelService.create(createCarModelRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateCarModelRequest updateCarModelRequest) {
    try {
      var response = carModelService.update(id, updateCarModelRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (EntityAlreadyExistsException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
