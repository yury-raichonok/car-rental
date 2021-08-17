package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

import com.example.carrental.controller.dto.car.CarClassNameResponse;
import com.example.carrental.controller.dto.car.CarClassNameWithTranslationsResponse;
import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.service.CarClassService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for Car Class REST endpoints.
 * <p>
 * This class handles the CRUD operations for Classes of Cars, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/carclasses")
@Validated
public class CarClassController {

  private final CarClassService carClassService;

  /**
   * Handle the /carclasses endpoint.
   * @param language selected language.
   * @return list of all car classes.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified car classes.
   * @throws NoContentException if list of car classes is empty.
   */
  @GetMapping
  public ResponseEntity<List<CarClassNameResponse>> findAll(
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language)
      throws NoContentException {
    var carClasses = carClassService.findAll(language);
    return new ResponseEntity<>(carClasses, HttpStatus.OK);
  }

  /**
   * Handle the /carclasses/paged endpoint.
   * @param pageable page of car classes.
   * @return page of all car classes.
   */
  @GetMapping(path = "/paged")
  public ResponseEntity<Page<CarClassNameWithTranslationsResponse>> findAllPaged(
      Pageable pageable) {
    var carClasses = carClassService.findAllPaged(pageable);
    return new ResponseEntity<>(carClasses, HttpStatus.OK);
  }

  /**
   * Handle the /carclasses endpoint.
   * @param createCarClassRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully created new car class.
   * 406: unable to create car class, because class with same name already exists.
   * @throws EntityAlreadyExistsException if class with same name already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateCarClassRequest createCarClassRequest)
      throws EntityAlreadyExistsException {
    carClassService.create(createCarClassRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /carclasses/{id} endpoint.
   * @param id of the car class which updated.
   * @param createCarClassRequest request with parameters.
   * Return one of the following status codes:
   * 200: if car class successfully updated.
   * 400: if bad request or invalid input parameters.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody CreateCarClassRequest createCarClassRequest) {
    carClassService.update(id, createCarClassRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
