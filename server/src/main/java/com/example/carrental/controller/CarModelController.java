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

/**
 * The controller for Car Model REST endpoints.
 * <p>
 * This class handles the CRUD operations for Models of Cars, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/models")
public class CarModelController {

  private final CarModelService carModelService;

  /**
   * Handle the /models endpoint.
   * @return list of all car models.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified car models.
   * @throws NoContentException if list of models is empty.
   */
  @GetMapping
  public ResponseEntity<List<CarModelResponse>> findAll() throws NoContentException {
    var models = carModelService.findAll();
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  /**
   * Handle the /models/list endpoint.
   * @param pageable page of car models.
   * @return page of all car models.
   */
  @GetMapping(path = "/list")
  public ResponseEntity<Page<CarModelBrandNameResponse>> findAllModelsWithBrandName(
      Pageable pageable) {
    var models = carModelService.findAllModelsWithBrandName(pageable);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  /**
   * Handle the /models/brand/id/{id} endpoint.
   * @param id of the brand to which the models belong.
   * @return list of brand models.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified brand car models.
   * @throws NoContentException if list of brand models is empty.
   */
  @GetMapping(path = "/brand/id/{id}")
  public ResponseEntity<List<CarModelResponse>> findModelsByBrandId(@PathVariable Long id)
      throws NoContentException {
    var models = carModelService.findModelsByBrandId(id);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  /**
   * Handle the /models/brand/{name} endpoint.
   * @param name of the brand to which the models belong.
   * @return list of brand models.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified brand car models.
   * @throws NoContentException if list of brand models is empty.
   */
  @GetMapping(path = "/brand/{name}")
  public ResponseEntity<List<CarModelResponse>> findModelsByBrandName(
      @NotNull @PathVariable String name) throws NoContentException {
    var models = carModelService.findModelsByBrandName(name);
    return new ResponseEntity<>(models, HttpStatus.OK);
  }

  /**
   * Handle the /models endpoint.
   * @param createCarModelRequest request data for creating new car model.
   * Return one of the following status codes:
   * 200: successfully created new car model.
   * 406: unable to create car model, because brand model with same name already exists.
   * @throws EntityAlreadyExistsException if brand model with same name already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateCarModelRequest createCarModelRequest)
      throws EntityAlreadyExistsException {
    carModelService.create(createCarModelRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /models/{id} endpoint.
   * @param id of the model which updated.
   * @param updateCarModelRequest request with parameters.
   * Return one of the following status codes:
   * 200: car model successfully updated.
   * 400: if bad request or invalid input parameters.
   * 406: unable to update car model, because brand model with same name already exists.
   * @throws EntityAlreadyExistsException if brand model with same name already exists.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UpdateCarModelRequest updateCarModelRequest)
      throws EntityAlreadyExistsException {
    carModelService.update(id, updateCarModelRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
