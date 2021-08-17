package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;

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

/**
 * The controller for Location REST endpoints.
 * <p>
 * This class handles the CRUD operations for Locations, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/locations")
@Validated
public class LocationController {

  private final LocationService locationService;

  /**
   * Handle the /locations endpoint.
   * @param language selected language.
   * @return locations list for select.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified car locations.
   * @throws NoContentException if list of locations is empty.
   */
  @GetMapping(path = "/select")
  public ResponseEntity<List<LocationNameResponse>> findAllForSelect(
      @NotNull @CookieValue(name = LANGUAGE_COOKIE_NAME) String language) throws NoContentException {
    var locations = locationService.findAllForSelect(language);
    return new ResponseEntity<>(locations, HttpStatus.OK);
  }

  /**
   * Handle the /locations/paged endpoint.
   * @param pageable page of car classes.
   * @return page of locations with translations.
   */
  @GetMapping(path = "/paged")
  public ResponseEntity<Page<LocationWithTranslationsResponse>> findAllPaged(Pageable pageable) {
    var locations = locationService.findAllPaged(pageable);
    return new ResponseEntity<>(locations, HttpStatus.OK);
  }

  /**
   * Handle the /locations endpoint.
   * @param createLocationRequest request with parameters.
   * Return one of the following status codes:
   * 200: successfully created new location.
   * 406: unable to create new location, because location same name already exists.
   * @throws EntityAlreadyExistsException if location with same name already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateLocationRequest createLocationRequest)
      throws EntityAlreadyExistsException {
    locationService.create(createLocationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /locations/{id} endpoint.
   * @param id of the location which updated.
   * @param createLocationRequest request with parameters.
   * Return one of the following status codes:
   * 200: location successfully updated.
   * 400: if bad request or invalid input parameters.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody CreateLocationRequest createLocationRequest) {
    locationService.update(id, createLocationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
