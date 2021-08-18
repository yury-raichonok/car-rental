package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BRAND_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
import static com.example.carrental.service.util.MultipartFileUtil.validateMultipartImageFile;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.mapper.CarBrandMapper;
import com.example.carrental.repository.CarBrandRepository;
import com.example.carrental.service.CarBrandService;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for Car Brands.
 * <p>
 * This class performs the CRUD operations for Car Brands.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

  private final CarBrandMapper carBrandMapper;
  private final CarBrandRepository carBrandRepository;
  private final FileStoreService fileStoreService;

  /**
   * @return list of car brands.
   * @throws NoContentException if list of brands is empty.
   */
  @Override
  public List<CarBrandResponse> findAll() throws NoContentException {
    var brands = carBrandRepository.findAll();
    List<CarBrandResponse> brandsResponse = new ArrayList<>();
    brands.forEach(brand -> brandsResponse.add(carBrandMapper.carBrandToCarBrandResponse(brand)));
    if (brandsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return brandsResponse;
  }

  /**
   * @return list of car brands.
   * @throws NoContentException if list of brands is empty.
   */
  @Override
  public List<CarBrandResponse> findAllBrandsWithRentalOffers() throws NoContentException {
    var brands = carBrandRepository.findAllWithRentalOffers();
    List<CarBrandResponse> brandsResponse = new ArrayList<>();
    brands.forEach(brand -> brandsResponse.add(carBrandMapper.carBrandToCarBrandResponse(brand)));
    if (brandsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return brandsResponse;
  }

  /**
   * @param pageable data.
   * @return page of car brands.
   */
  @Override
  public Page<CarBrandResponse> findAllPaged(Pageable pageable) {
    var brands = carBrandRepository.findAll(pageable);
    List<CarBrandResponse> brandsResponse = new ArrayList<>();
    brands.forEach(brand -> brandsResponse.add(carBrandMapper.carBrandToCarBrandResponse(brand)));
    return new PageImpl<>(brandsResponse, brands.getPageable(), brands.getTotalElements());
  }

  /**
   * @param id of car brand.
   * @return car brand.
   * @throws IllegalStateException if car brand with specified id not exists.
   */
  @Override
  public CarBrand findById(Long id) {
    return carBrandRepository.findById(id).orElseThrow(() -> {
      log.error("Car brand with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car brand with id %d does not exists", id));
    });
  }

  /**
   * @param name of car brand.
   * @return car brand.
   * @throws IllegalStateException if car brand with specified name not exists.
   */
  @Override
  public CarBrand findByName(String name) {
    return carBrandRepository.findByName(name).orElseThrow(() -> {
      log.error("Car brand with name {} does not exist", name);
      throw new IllegalStateException(
          String.format("Car brand with name %s does not exists", name));
    });
  }

  /**
   * @param createCarBrandRequest request data for creating new brand.
   * @throws EntityAlreadyExistsException if brand with specified name already exists.
   */
  @Override
  public void create(CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException {
    validateCarBrandName(createCarBrandRequest.getName());
    carBrandRepository.save(CarBrand
        .builder()
        .name(createCarBrandRequest.getName())
        .createdAt(LocalDateTime.now())
        .build());
  }

  /**
   * @param id of car brand.
   * @param brandFile image of car brand.
   * @throws IllegalStateException if bad request or invalid input parameters.
   */
  @Override
  @Transactional
  public void uploadBrandImage(Long id, MultipartFile brandFile) {
    validateMultipartImageFile(brandFile);
    String imageLink;
    var carBrand = findById(id);
    var filename = String.format("%s/%s-%s", id, id, brandFile.getOriginalFilename());
    var file = new File(Objects.requireNonNull(brandFile.getOriginalFilename()));

    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(brandFile.getBytes());
      imageLink = fileStoreService.uploadPublicImage(BRAND_IMAGE, filename, file);
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }
    try {
      Files.delete(Path.of(file.getPath()));
    } catch (IOException e) {
      log.warn("File was not deleted after upload. Exception: {}", e.getMessage());
    }
    carBrand.setBrandImageLink(imageLink);
    carBrand.setChangedAt(LocalDateTime.now());
    carBrandRepository.save(carBrand);
  }

  /**
   * @param id of car brand.
   * @param createCarBrandRequest request data for updating car brand.
   * @throws EntityAlreadyExistsException if car brand with new name already exists.
   */
  @Override
  @Transactional
  public void update(Long id, CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException {
    validateCarBrandName(createCarBrandRequest.getName());

    var carBrand = findById(id);
    carBrand.setName(createCarBrandRequest.getName());
    carBrand.setChangedAt(LocalDateTime.now());
    carBrandRepository.save(carBrand);
  }

  /**
   * @param brandName of brand.
   * @throws EntityAlreadyExistsException if car brand with specified name already exists.
   */
  private void validateCarBrandName(String brandName) throws EntityAlreadyExistsException {
    if (carBrandRepository.findByName(brandName).isPresent()) {
      log.error("Car brand {} already exists", brandName);
      throw new EntityAlreadyExistsException(
          String.format("Car brand %s already exists", brandName));
    }
  }
}
