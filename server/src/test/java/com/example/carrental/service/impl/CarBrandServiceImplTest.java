package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BRAND_IMAGE;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.repository.CarBrandRepository;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
class CarBrandServiceImplTest {

  @Autowired
  private CarBrandServiceImpl carBrandService;

  @MockBean
  private CarBrandRepository carBrandRepository;

  @MockBean
  private FileStoreService fileStoreService;

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var response = Arrays.asList(CarBrand.builder().id(1L).name("name").brandImageLink("link")
        .build(), CarBrand.builder().id(2L).name("name1").brandImageLink("link1").build());
    when(carBrandRepository.findAll()).thenReturn(response);

    var carBrandResponse = carBrandService.findAll();

    assertThat(carBrandResponse).isNotNull();
    assertThat(carBrandResponse.size()).isEqualTo(response.size());
  }

  @Test
  void givenValidRequest_whenFindAllWithNoContent_thenThrowNoContentException() {
    var response = new ArrayList<CarBrand>();
    when(carBrandRepository.findAll()).thenReturn(response);

    assertThrows(NoContentException.class, () -> carBrandService.findAll());
  }

  @Test
  void givenValidRequest_whenFindAllBrandsWithRentalOffers_thenSuccess() {
    var response = Arrays.asList(CarBrand.builder().id(1L).name("name").brandImageLink("link")
        .build(), CarBrand.builder().id(2L).name("name1").brandImageLink("link1").build());
    when(carBrandRepository.findAllWithRentalOffers()).thenReturn(response);

    var carBrandResponse = carBrandService.findAllBrandsWithRentalOffers();

    assertThat(carBrandResponse).isNotNull();
    assertThat(carBrandResponse.size()).isEqualTo(2);
  }

  @Test
  void givenValidRequest_whenFindAllBrandsWithRentalOffersWithNoContent_thenThrowNoContentException() {
    var response = new ArrayList<CarBrand>();
    when(carBrandRepository.findAllWithRentalOffers()).thenReturn(response);

    assertThrows(NoContentException.class, () -> carBrandService.findAllBrandsWithRentalOffers());
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenSuccess() {
    var response = Arrays.asList(CarBrand.builder().id(1L).name("name").brandImageLink("link")
        .build(), CarBrand.builder().id(2L).name("name1").brandImageLink("link1").build());
    var pageable = Pageable.ofSize(10).withPage(0);
    when(carBrandRepository.findAll(pageable)).thenReturn(new PageImpl<>(response));

    var carBrandResponse = carBrandService.findAllPaged(pageable);

    assertThat(carBrandResponse).isNotNull();
    assertThat(carBrandResponse.getTotalElements()).isEqualTo(response.size());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var response = Optional
        .of(CarBrand.builder().id(1L).name("name").brandImageLink("link").build());
    when(carBrandRepository.findById(1L)).thenReturn(response);

    var carBrand = carBrandService.findById(1L);

    assertThat(carBrand).isNotNull();
    assertThat(carBrand.getId()).isEqualTo(response.get().getId());
    assertThat(carBrand.getName()).isEqualTo(response.get().getName());
    assertThat(carBrand.getBrandImageLink()).isEqualTo(response.get().getBrandImageLink());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(carBrandRepository.findById(1L)).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> carBrandService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindByName_thenSuccess() {
    var response = Optional
        .of(CarBrand.builder().id(1L).name("name").brandImageLink("link").build());
    when(carBrandRepository.findByName("name")).thenReturn(response);

    var carBrand = carBrandService.findByName("name");

    assertThat(carBrand).isNotNull();
    assertThat(carBrand.getId()).isEqualTo(response.get().getId());
    assertThat(carBrand.getName()).isEqualTo(response.get().getName());
    assertThat(carBrand.getBrandImageLink()).isEqualTo(response.get().getBrandImageLink());
  }

  @Test
  void givenRequestWithNotExistingName_whenFindByName_thenThrowIllegalStateException() {
    when(carBrandRepository.findByName("name")).thenThrow(new IllegalStateException());

    assertThrows(IllegalStateException.class, () -> carBrandService.findByName("name"));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();
    var carBrand = CarBrand.builder().name("name").build();
    when(carBrandRepository.findByName("name")).thenReturn(Optional.empty());
    when(carBrandRepository.save(any())).thenReturn(carBrand);
    carBrandService.create(createCarBrandRequest);

    assertThat(carBrand.getName()).isEqualTo(createCarBrandRequest.getName());
  }

  @Test
  void givenRequestWithExistingName_whenCreate_thenThrowEntityAlreadyExistsException() {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();
    var brandResponse = Optional.of(CarBrand.builder().id(1L).name("name").build());
    when(carBrandRepository.findByName("name")).thenReturn(brandResponse);
    assertThrows(EntityAlreadyExistsException.class,
        () -> carBrandService.create(createCarBrandRequest));

  }

  @Test
  void givenValidRequest_whenUploadBrandImage_thenSuccess() {
    var brandFile = new MockMultipartFile("brandFile", "brandFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var carBrand = Optional.of(CarBrand.builder().name("name").build());
    var carBrandResponse = CarBrand.builder().name("name").build();
    var imageLink = "Image link";
    when(carBrandRepository.findById(1L)).thenReturn(carBrand);
    when(fileStoreService.uploadPublicImage(BRAND_IMAGE,
        String.format("%s/%s-%s", 1, 1, brandFile.getOriginalFilename()), new File(
            Objects.requireNonNull(brandFile.getOriginalFilename())))).thenReturn(imageLink);
    when(carBrandRepository.save(any())).thenReturn(carBrandResponse);

    carBrandService.uploadBrandImage(1L, brandFile);

    assertThat(carBrand.get().getBrandImageLink()).isEqualTo(imageLink);
  }

  @Test
  void givenRequestWithEmptyBrandFile_whenUploadBrandImage_thenThrowIllegalStateException() {
    var brandFile = new MockMultipartFile("brandFile", "brandFile.img",
        IMAGE_JPEG.getMimeType(), new byte[0]);

    Exception exception = assertThrows(IllegalStateException.class,
        () -> carBrandService.uploadBrandImage(1L, brandFile));
    String expectedMessage = "Cannot upload empty file [brandFile.img]";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenRequestWithBrandFileNotAnImage_whenUploadBrandImage_thenThrowIllegalStateException() {
    var brandFile = new MockMultipartFile("brandFile", "brandFile.txt",
        "multipart/form-data", "Some dataset...".getBytes());

    Exception exception = assertThrows(IllegalStateException.class,
        () -> carBrandService.uploadBrandImage(1L, brandFile));
    String expectedMessage = "File must be an image [multipart/form-data]";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenValidRequest_whenUploadBrandImageToStorageFailed_thenThrowIllegalStateException() {
    var brandFile = new MockMultipartFile("brandFile", "brandFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var carBrand = Optional.of(CarBrand.builder().name("name").build());
    var carBrandResponse = CarBrand.builder().name("name").build();
    when(carBrandRepository.findById(1L)).thenReturn(carBrand);
    when(fileStoreService.uploadPublicImage(BRAND_IMAGE,
        String.format("%s/%s-%s", 1, 1, brandFile.getOriginalFilename()),
        new File(Objects.requireNonNull(brandFile.getOriginalFilename()))))
        .thenThrow(new IllegalStateException("Save failed"));
    when(carBrandRepository.save(any())).thenReturn(carBrandResponse);

    Exception exception = assertThrows(IllegalStateException.class,
        () -> carBrandService.uploadBrandImage(1L, brandFile));
    String expectedMessage = "Save failed";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var response = Optional
        .of(CarBrand.builder().id(1L).name("name").brandImageLink("link").build());
    var carBrandResponse = CarBrand.builder().name("name").build();
    when(carBrandRepository.findById(1L)).thenReturn(response);
    when(carBrandRepository.findByName("name")).thenReturn(Optional.empty());
    when(carBrandRepository.save(any())).thenReturn(carBrandResponse);

    var carBrand = carBrandService.findById(1L);

    assertThat(carBrand).isNotNull();
    assertThat(carBrand.getId()).isEqualTo(response.get().getId());
    assertThat(carBrand.getName()).isEqualTo(response.get().getName());
    assertThat(carBrand.getBrandImageLink()).isEqualTo(response.get().getBrandImageLink());
  }
}