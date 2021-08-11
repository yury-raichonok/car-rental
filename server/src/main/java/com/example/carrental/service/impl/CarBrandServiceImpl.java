package com.example.carrental.service.impl;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
  public String create(CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException {
    if (carBrandRepository.findByName(createCarBrandRequest.getName()).isPresent()) {
      log.error("Car brand {} already exists", createCarBrandRequest.getName());
      throw new EntityAlreadyExistsException(String.format("Car brand %s already exists",
          createCarBrandRequest.getName()));
    }

    carBrandRepository.save(CarBrand
        .builder()
        .name(createCarBrandRequest.getName())
        .createdAt(LocalDateTime.now())
        .build());
    return "Success";
  }

  @Override
  public List<CarBrandResponse> findAll() throws NoContentException {
    List<CarBrand> carBrands = carBrandRepository.findAll();
    List<CarBrandResponse> carBrandResponses = new ArrayList<>();
    carBrands.forEach(b -> carBrandResponses.add(carBrandMapper.carBrandToCarBrandResponse(b)));
    if (carBrandResponses.isEmpty()) {
      throw new NoContentException("No content");
    }
    return carBrandResponses;
  }

  @Override
  public List<CarBrandResponse> findAllBrandsWithRentalOffers() throws NoContentException {
    List<CarBrand> carBrands = carBrandRepository.findAllWithRentalOffers();
    List<CarBrandResponse> carBrandResponses = new ArrayList<>();
    carBrands.forEach(b -> carBrandResponses.add(carBrandMapper.carBrandToCarBrandResponse(b)));
    if (carBrandResponses.isEmpty()) {
      throw new NoContentException("No content");
    }
    return carBrandResponses;
  }

  @Override
  public Page<CarBrandResponse> findAllPaged(Pageable pageable) {
    Page<CarBrand> carBrandsPage = carBrandRepository.findAll(pageable);
    List<CarBrandResponse> carBrandResponses = new ArrayList<>();
    carBrandsPage.forEach(b -> carBrandResponses.add(carBrandMapper.carBrandToCarBrandResponse(b)));
    return new PageImpl<>(carBrandResponses, carBrandsPage.getPageable(),
        carBrandsPage.getTotalElements());
  }

  @Override
  public CarBrand findById(Long id) {
    Optional<CarBrand> optionalCarBrand = carBrandRepository.findById(id);
    if (optionalCarBrand.isEmpty()) {
      log.error("Car brand with id {} does not exist", id);
      throw new IllegalStateException(String.format("Car brand with id %d does not exists", id));
    }
    return optionalCarBrand.get();
  }

  @Override
  public CarBrand findByName(String name) {
    Optional<CarBrand> optionalCarBrand = carBrandRepository.findByName(name);
    if (optionalCarBrand.isEmpty()) {
      log.error("Car brand with name {} does not exist", name);
      throw new IllegalStateException(
          String.format("Car brand with name %s does not exists", name));
    }
    return optionalCarBrand.get();
  }

  @Override
  @Transactional
  public String update(Long id, CreateCarBrandRequest createCarBrandRequest)
      throws EntityAlreadyExistsException {

    if (carBrandRepository.findByName(createCarBrandRequest.getName()).isPresent()) {
      log.error("Car brand {} already exists", createCarBrandRequest.getName());
      throw new EntityAlreadyExistsException(String.format("Car brand %s already exists",
          createCarBrandRequest.getName()));
    }

    var carBrand = findById(id);

    carBrand.setName(createCarBrandRequest.getName());
    carBrand.setChangedAt(LocalDateTime.now());
    carBrandRepository.save(carBrand);
    return "Success";
  }

  @Override
  @Transactional
  public String uploadBrandImage(Long id, MultipartFile brandFile) {
    if (brandFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", brandFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          brandFile.getSize()));
    }

    if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType())
        .contains(brandFile.getContentType())) {
      log.error("File must be an image [{}]", brandFile.getContentType());
      throw new IllegalStateException(String.format("File must be an image [%s]",
          brandFile.getContentType()));
    }

    var carBrand = findById(id);

    String filename = String.format("%s/%s-%s", id, id, brandFile.getOriginalFilename());
    String imageLink = String.format("https://%s.s3.%s.amazonaws.com/%s",
        brandImagesBucket, region, filename);

    try {
      File file = new File(Objects.requireNonNull(brandFile.getOriginalFilename()));
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(brandFile.getBytes());
      fileOutputStream.close();

      fileStoreService.uploadPublicFile(brandImagesBucket, filename, file);

      file.delete();
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }

    carBrand.setBrandImageLink(imageLink);
    carBrand.setChangedAt(LocalDateTime.now());
    carBrandRepository.save(carBrand);

    return "Success";
  }
}
