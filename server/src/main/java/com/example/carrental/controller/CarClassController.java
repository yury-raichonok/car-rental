package com.example.carrental.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/carclasses")
@Validated
public class CarClassController {

  private final CarClassService carClassService;

  @PostMapping
  public ResponseEntity<String> create(
      @Valid @RequestBody CreateCarClassRequest createCarClassRequest)
      throws EntityAlreadyExistsException {
    var response = carClassService.create(createCarClassRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<CarClassNameResponse>> findAll(
      @NotNull @CookieValue(name = "i18next") String language) throws NoContentException {
    var carClasses = carClassService.findAll(language);
    return new ResponseEntity<>(carClasses, HttpStatus.OK);
  }

  @GetMapping(path = "/paged")
  public ResponseEntity<Page<CarClassNameWithTranslationsResponse>> findAllPaged(
      Pageable pageable) {
    var carClasses = carClassService.findAllPaged(pageable);
    return new ResponseEntity<>(carClasses, HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody CreateCarClassRequest createCarClassRequest) {
    var response = carClassService.update(id, createCarClassRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
