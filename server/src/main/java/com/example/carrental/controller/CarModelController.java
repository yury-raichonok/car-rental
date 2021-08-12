package com.example.carrental.controller;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.service.CarModelService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<CarModelResponse>> findAll() throws NoContentException {
    var models = carModelService.findAll();
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  @GetMapping(path = "/list")
  public ResponseEntity<Page<CarModelBrandNameResponse>> findAllModelsWithBrandName(
      Pageable pageable) {
    var models = carModelService.findAllModelsWithBrandName(pageable);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  @GetMapping(path = "/brand/id/{id}")
  public ResponseEntity<List<CarModelResponse>> findModelsByBrandId(@PathVariable Long id)
      throws NoContentException {
    var models = carModelService.findModelsByBrandId(id);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  @GetMapping(path = "/brand/{name}")
  public ResponseEntity<List<CarModelResponse>> findModelsByBrandName(
      @NotNull @PathVariable String name) throws NoContentException {
    var models = carModelService.findModelsByBrandName(name);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateCarModelRequest createCarModelRequest)
      throws EntityAlreadyExistsException {
    carModelService.create(createCarModelRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateCarModelRequest updateCarModelRequest)
      throws EntityAlreadyExistsException {
    carModelService.update(id, updateCarModelRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
