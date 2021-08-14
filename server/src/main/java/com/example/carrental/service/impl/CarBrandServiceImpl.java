package com.example.carrental.service.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

  private final CarBrandRepository carBrandRepository;
  private final FileStoreService fileStoreService;
  private final CarBrandMapper carBrandMapper;

  @Value("${amazon.region}")
  private String region;

  @Value("${amazon.brand.images.bucket}")
  private String brandImagesBucket;

  @Override
  public List<CarBrandResponse> findAll() throws NoContentException {
    var carBrands = carBrandRepository.findAll();
    List<CarBrandResponse> brandsResponse = new ArrayList<>();
    carBrands
        .forEach(brand -> brandsResponse.add(carBrandMapper.carBrandToCarBrandResponse(brand)));
    if (brandsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return brandsResponse;
  }

  @Override
  public List<CarBrandResponse> findAllBrandsWithRentalOffers() throws NoContentException {
    var carBrands = carBrandRepository.findAllWithRentalOffers();
    List<CarBrandResponse> brandsResponse = new ArrayList<>();
    carBrands
        .forEach(brand -> brandsResponse.add(carBrandMapper.carBrandToCarBrandResponse(brand)));
    if (brandsResponse.isEmpty()) {
      throw new NoContentException(NO_CONTENT);
    }
    return brandsResponse;
  }

  @Override
  public Page<CarBrandResponse> findAllPaged(Pageable pageable) {
    var carBrandsPage = carBrandRepository.findAll(pageable);
    List<CarBrandResponse> brandsResponse = new ArrayList<>();
    carBrandsPage
        .forEach(brand -> brandsResponse.add(carBrandMapper.carBrandToCarBrandResponse(brand)));
    return new PageImpl<>(brandsResponse, carBrandsPage.getPageable(),
        carBrandsPage.getTotalElements());
  }

  @Override
  public CarBrand findById(Long id) {
    return carBrandRepository.findById(id).orElseThrow(() -> {
      log.error("Car brand with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car brand with id %d does not exists", id));
    });
  }

  @Override
  public CarBrand findByName(String name) {
    return carBrandRepository.findByName(name).orElseThrow(() -> {
      log.error("Car brand with name {} does not exist", name);
      throw new IllegalStateException(
          String.format("Car brand with name %s does not exists", name));
    });
  }

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

  @Override
  @Transactional
  public void uploadBrandImage(Long id, MultipartFile brandFile) {
    validateMultipartImageFile(brandFile);
    var carBrand = findById(id);
    var filename = String.format("%s/%s-%s", id, id, brandFile.getOriginalFilename());
    var imageLink = String.format("https://%s.s3.%s.amazonaws.com/%s",
        brandImagesBucket, region, filename);

    var file = new File(Objects.requireNonNull(brandFile.getOriginalFilename()));

    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(brandFile.getBytes());
      fileStoreService.uploadPublicFile(brandImagesBucket, filename, file);
      Files.delete(Path.of(file.getPath()));
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }

    carBrand.setBrandImageLink(imageLink);
    carBrand.setChangedAt(LocalDateTime.now());
    carBrandRepository.save(carBrand);
  }

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

  private void validateCarBrandName(String brandName) throws EntityAlreadyExistsException {
    if (carBrandRepository.findByName(brandName).isPresent()) {
      log.error("Car brand {} already exists", brandName);
      throw new EntityAlreadyExistsException(
          String.format("Car brand %s already exists", brandName));
    }
  }
}
