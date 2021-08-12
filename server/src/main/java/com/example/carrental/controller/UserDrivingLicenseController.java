package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseDataResponse;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.NoContentException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/drivinglicenses")
@Validated
public class UserDrivingLicenseController {

  private final UserDrivingLicenseService userDrivingLicenseService;

  @GetMapping(path = "/{id}")
  public ResponseEntity<UserDrivingLicenseConfirmationDataResponse> findDrivingLicenseById(
      @NotNull @Positive @PathVariable Long id) {
    var userPassportDataResponse = userDrivingLicenseService.findDrivingLicenseById(id);
    return new ResponseEntity<>(userPassportDataResponse, HttpStatus.OK);
  }

  @GetMapping(path = "/data")
  public ResponseEntity<UserDrivingLicenseDataResponse> findUserDrivingLicenseData()
      throws NoContentException {
    var userDrivingLicenseDataResponse = userDrivingLicenseService.findUserDrivingLicenseData();
    return new ResponseEntity<>(userDrivingLicenseDataResponse, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<HttpStatus> createOrUpdate(
      @Valid @RequestBody CreateOrUpdateUserDrivingLicenseRequest
          createOrUpdateUserDrivingLicenseRequest) {
    userDrivingLicenseService.createOrUpdate(createOrUpdateUserDrivingLicenseRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}/status")
  public ResponseEntity<HttpStatus> updateDrivingLicenseStatus(
      @NotNull @Positive @PathVariable Long id) throws DrivingLicenseNotConfirmedException {
    userDrivingLicenseService.updateDrivingLicenseStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(
      path = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<HttpStatus> uploadFile(
      @NotNull @RequestParam MultipartFile drivingLicenseFile) {
    userDrivingLicenseService.uploadFile(drivingLicenseFile);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/download")
  public ResponseEntity<ByteArrayResource> downloadFiles(
      @Valid @RequestBody UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    var response = userDrivingLicenseService.downloadFiles(userDocumentsDownloadRequest);
    return ResponseEntity.ok().contentLength(response.getByteArray().length)
        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(response);
  }
}
