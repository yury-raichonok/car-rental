package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.NoDrivingLicenseDataException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
  public ResponseEntity<?> findDrivingLicenseById(@NotNull @Positive @PathVariable Long id) {
    try {
      var userPassportDataResponse = userDrivingLicenseService.findDrivingLicenseById(id);
      return new ResponseEntity<>(userPassportDataResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/data")
  public ResponseEntity<?> getUserDrivingLicenseData() {
    try {
      var userDrivingLicenseDataResponse = userDrivingLicenseService.getUserDrivingLicenseData();
      return new ResponseEntity<>(userDrivingLicenseDataResponse, HttpStatus.OK);
    } catch (NoDrivingLicenseDataException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<?> createOrUpdate(
      @Valid @RequestBody CreateOrUpdateUserDrivingLicenseRequest createOrUpdateUserDrivingLicenseRequest) {
    try {
      var response = userDrivingLicenseService.createOrUpdate(
          createOrUpdateUserDrivingLicenseRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}/status")
  public ResponseEntity<String> updateDrivingLicenseStatus(
      @NotNull @Positive @PathVariable Long id) {
    try {
      var response = userDrivingLicenseService.updateDrivingLicenseStatus(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (DocumentsNotConfirmedException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  @PostMapping(
      path = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> uploadFile(
      @NotNull @RequestParam MultipartFile drivingLicenseFile) {
    try {
      var response = userDrivingLicenseService.uploadFile(drivingLicenseFile);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/download")
  public ResponseEntity<?> downloadFiles(
      @Valid @RequestBody UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    try {
      var response = userDrivingLicenseService.downloadFiles(userDocumentsDownloadRequest);
      return ResponseEntity.ok()
          .contentLength(response.getByteArray().length)
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(response);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }
}
