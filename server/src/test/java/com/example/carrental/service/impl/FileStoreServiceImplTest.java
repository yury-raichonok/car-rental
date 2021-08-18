package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BRAND_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.CAR_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.DRIVING_LICENSE_FILE;
import static com.example.carrental.constants.ApplicationConstants.PASSPORT_FILE;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.carrental.config.AmazonConfig;
import java.io.File;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
class FileStoreServiceImplTest {

  @Autowired
  private FileStoreServiceImpl fileStoreService;

  @MockBean
  private AmazonConfig amazonConfig;

  @MockBean
  private AmazonS3 amazonS3;

  @Test
  void givenValidRequest_whenUploadPublicCarImage_thenSuccess() {
    var carFile = new MockMultipartFile("carFile", "carFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(carFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

    var expectedImageLink = String.format("https://%s.s3.%s.amazonaws.com/%s",
        amazonConfig.getCarImagesBucket(), amazonConfig.getRegion(), carFile.getOriginalFilename());
    var imageLink = fileStoreService.uploadPublicImage(CAR_IMAGE, carFile.getOriginalFilename(), file);

    assertThat(imageLink).isNotNull();
    assertThat(imageLink).isEqualTo(expectedImageLink);
  }

  @Test
  void givenValidRequest_whenUploadPublicBrandImage_thenSuccess() {
    var brandFile = new MockMultipartFile("brandFile", "brandFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(brandFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

    var expectedImageLink = String.format("https://%s.s3.%s.amazonaws.com/%s",
        amazonConfig.getCarImagesBucket(), amazonConfig.getRegion(), brandFile.getOriginalFilename());
    var imageLink = fileStoreService.uploadPublicImage(BRAND_IMAGE, brandFile.getOriginalFilename(), file);

    assertThat(imageLink).isNotNull();
    assertThat(imageLink).isEqualTo(expectedImageLink);
  }

  @Test
  void givenInvalidRequestWithWrongFileType_whenUploadPublicImage_thenThrowIllegalStateException() {
    var carFile = new MockMultipartFile("carFile", "carFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(carFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

    assertThrows(IllegalStateException.class, () -> fileStoreService.uploadPublicImage("Wrong type",
        carFile.getOriginalFilename(), file));
  }

  @Test
  void givenValidRequest_whenUploadPublicBrandImageFailed_thenThrowIllegalStateException() {
    var brandFile = new MockMultipartFile("brandFile", "brandFile.img",
        IMAGE_JPEG.getMimeType(), "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(brandFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenThrow(new AmazonServiceException(""));

    assertThrows(IllegalStateException.class, () -> fileStoreService.uploadPublicImage(BRAND_IMAGE,
        brandFile.getOriginalFilename(), file));
  }

  @Test
  void givenValidRequest_whenUploadPassportFile_thenSuccess() {
    var passportFile = new MockMultipartFile("passportFile", "passportFile.txt",
        "any type", "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(passportFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

    assertDoesNotThrow(() -> fileStoreService.uploadFile(PASSPORT_FILE, passportFile.getOriginalFilename(), file));
  }

  @Test
  void givenValidRequest_whenUploadDrivingLicenseFile_thenSuccess() {
    var drivingLicenseFile = new MockMultipartFile("drivingLicenseFile", "drivingLicenseFile.txt",
        "any type", "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(drivingLicenseFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

    assertDoesNotThrow(() -> fileStoreService.uploadFile(DRIVING_LICENSE_FILE, drivingLicenseFile.getOriginalFilename(), file));
  }

  @Test
  void givenInvalidRequestWithWrongFileType_whenUploadPassportFile_thenThrowIllegalStateException() {
    var passportFile = new MockMultipartFile("passportFile", "passportFile.txt",
        "any type", "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(passportFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

    assertThrows(IllegalStateException.class, () -> fileStoreService.uploadFile("Wrong type", passportFile.getOriginalFilename(), file));
  }

  @Test
  void givenValidRequest_whenUploadPassportFile_thenThrowIllegalStateException() {
    var passportFile = new MockMultipartFile("passportFile", "passportFile.txt",
        "any type", "Some dataset...".getBytes());
    var file = new File(Objects.requireNonNull(passportFile.getOriginalFilename()));
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenThrow(new AmazonServiceException(""));

    assertThrows(IllegalStateException.class, () -> fileStoreService.uploadFile(PASSPORT_FILE, passportFile.getOriginalFilename(), file));
  }

  @Test
  void givenValidRequest_whenDownloadZippedPassportFiles_thenSuccess() {
    when(amazonS3.listObjects(any(), any())).thenReturn(new ObjectListing());
    var byteArrayOutputStream = fileStoreService.downloadZippedFiles(PASSPORT_FILE, "directory");

    assertThat(byteArrayOutputStream).isNotNull();
  }

  @Test
  void givenValidRequest_whenDownloadZippedDrivingLicenseFiles_thenSuccess() {
    when(amazonS3.listObjects(any(), any())).thenReturn(new ObjectListing());
    var byteArrayOutputStream = fileStoreService.downloadZippedFiles(DRIVING_LICENSE_FILE, "directory");

    assertThat(byteArrayOutputStream).isNotNull();
  }

  @Test
  void givenInvalidRequestWithWrongFileType_whenDownloadZippedFiles_thenThrowIllegalStateException() {
    when(amazonS3.listObjects(any(), any())).thenReturn(new ObjectListing());

    assertThrows(IllegalStateException.class, () -> fileStoreService.downloadZippedFiles("Wrong type", "directory"));
  }

  @Test
  void givenValidRequest_whenDownloadZippedFilesFailed_thenThrowIllegalStateException() {
    when(amazonS3.listObjects(any(), any())).thenThrow(new AmazonServiceException(""));

    assertThrows(IllegalStateException.class, () -> fileStoreService.downloadZippedFiles(PASSPORT_FILE, "directory"));
  }
}