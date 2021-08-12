package com.example.carrental.controller;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.service.LocationService;
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
@RequestMapping("/locations")
@Validated
public class LocationController {

  private final LocationService locationService;

  @GetMapping(path = "/select")
  public ResponseEntity<List<LocationNameResponse>> findAllForSelect(
      @NotNull @CookieValue(name = "i18next") String language) throws NoContentException {
    var locations = locationService.findAllForSelect(language);
    return new ResponseEntity<>(locations, HttpStatus.OK);
  }

  @GetMapping(path = "/paged")
  public ResponseEntity<Page<LocationWithTranslationsResponse>> findAllPaged(Pageable pageable) {
    var locations = locationService.findAllPaged(pageable);
    return new ResponseEntity<>(locations, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateLocationRequest createLocationRequest)
      throws EntityAlreadyExistsException {
    locationService.create(createLocationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody CreateLocationRequest createLocationRequest) {
    locationService.update(id, createLocationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
