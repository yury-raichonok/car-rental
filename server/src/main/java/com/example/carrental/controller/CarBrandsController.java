package com.example.carrental.controller;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.service.CarBrandService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The controller for Car Brand REST endpoints.
 * <p>
 * This class handles the CRUD operations for Brands of Cars, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/brands")
@Validated
public class CarBrandsController {

  private final CarBrandService carBrandService;

  /**
   * Handle the /brands/all endpoint.
   * @return list of all car brands.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified car brands.
   * @throws NoContentException if list of brands is empty.
   */
  @GetMapping(path = "/all")
  public ResponseEntity<List<CarBrandResponse>> findAll() throws NoContentException {
    var brands = carBrandService.findAll();
    return new ResponseEntity<>(brands, HttpStatus.OK);
  }

  /**
   * Handle the /brands endpoint.
   * @return list of car brands with rental offers.
   * @throws NoContentException if list of brands is empty.
   */
  @GetMapping
  public ResponseEntity<List<CarBrandResponse>> findAllBrandsWithRentalOffers()
      throws NoContentException {
    var brands = carBrandService.findAllBrandsWithRentalOffers();
    return new ResponseEntity<>(brands, HttpStatus.OK);
  }

  /**
   * Handle the /brands/all/paged endpoint.
   * @param pageable page of car brands.
   * @return page of all car brands.
   */
  @GetMapping(path = "/all/paged")
  public ResponseEntity<Page<CarBrandResponse>> findAllPaged(Pageable pageable) {
    var brands = carBrandService.findAllPaged(pageable);
    return new ResponseEntity<>(brands, HttpStatus.OK);
  }

  /**
   * Handle the /brands endpoint.
   * @param createCarBrandRequest request data for creating new car brand.
   * Return one of the following status codes:
   * 200: successfully created new car brand.
   * 406: unable to create car brand, because brand with same name already exists.
   * @throws EntityAlreadyExistsException if brand with same name already exists.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> create(
      @Valid @RequestBody CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException {
    carBrandService.create(createCarBrandRequest);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Handle the /brands/{id}/upload/image endpoint.
   * @param id of the brand for which the image is loaded.
   * @param brandFile image of car brand.
   * Return one of the following status codes:
   * 200: successfully uploaded car brand file.
   * 400: if bad request or invalid input parameters.
   */
  @PostMapping(
      path = "/{id}/upload/image",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<HttpStatus> uploadBrandImage(@NotNull @Positive @PathVariable Long id,
      @NotNull @RequestParam MultipartFile brandFile) {
    carBrandService.uploadBrandImage(id, brandFile);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /brands/{id} endpoint.
   * @param id of the brand which updated.
   * @param createCarBrandRequest request with parameters.
   * Return one of the following status codes:
   * 200: car brand successfully updated.
   * 400: if bad request or invalid input parameters.
   * 406: unable to update car brand, because brand with same name already exists.
   * @throws EntityAlreadyExistsException if brand with same name already exists.
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException {
    carBrandService.update(id, createCarBrandRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
