package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.NoPassportDataException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
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
@RequestMapping("/passports")
@Validated
public class UserPassportController {

  private final UserPassportService userPassportService;

  @GetMapping(path = "/{id}")
  public ResponseEntity<?> findPassportDataById(@NotNull @Positive @PathVariable Long id) {
    try {
      var userPassportDataResponse = userPassportService.findPassportDataById(id);
      return new ResponseEntity<>(userPassportDataResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/data")
  public ResponseEntity<?> getUserPassportData() {
    try {
      var userPassportDataResponse = userPassportService.getUserPassportData();
      return new ResponseEntity<>(userPassportDataResponse, HttpStatus.OK);
    } catch (NoPassportDataException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<?> createOrUpdate(
      @Valid @RequestBody CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest) {
    try {
      var response = userPassportService.createOrUpdate(createOrUpdateUserPassportRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}/status")
  public ResponseEntity<String> updatePassportStatus(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = userPassportService.updatePassportStatus(id);
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
  public ResponseEntity<String> uploadFile(@NotNull @RequestParam MultipartFile passportFile) {
    try {
      var response = userPassportService.uploadFile(passportFile);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/download")
  public ResponseEntity<?> downloadFiles(
      @Valid @RequestBody UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    try {
      var response = userPassportService.downloadFiles(userDocumentsDownloadRequest);
      return ResponseEntity.ok()
          .contentLength(response.getByteArray().length)
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(response);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
