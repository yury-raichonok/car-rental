package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.exceptions.NoContentException;
import com.example.carrental.service.exceptions.PassportNotConfirmedException;
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

/**
 * The controller for Passports REST endpoints.
 * <p>
 * This class handles the CRUD operations for Passports, via HTTP actions.
 * </p>
 * @author Yury Raichonak
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/passports")
@Validated
public class UserPassportController {

  private final UserPassportService userPassportService;

  /**
   * Handle the /passports/data endpoint.
   * @return user passport data.
   * Return one of the following status codes:
   * 200: successfully received data.
   * 204: no specified passport data.
   * @throws NoContentException if no specified passport data.
   */
  @GetMapping(path = "/data")
  public ResponseEntity<UserPassportDataResponse> getUserPassportData() throws NoContentException {
    var userPassportDataResponse = userPassportService.getUserPassportData();
    return new ResponseEntity<>(userPassportDataResponse, HttpStatus.OK);
  }

  /**
   * Handle the /passports/data endpoint.
   * @param id of the user passport.
   * @return user passport data for confirmation request.
   */
  @GetMapping(path = "/{id}")
  public ResponseEntity<UserPassportConfirmationDataResponse> findPassportDataById(
      @NotNull @Positive @PathVariable Long id) {
    var userPassportDataResponse = userPassportService.findPassportDataById(id);
    return new ResponseEntity<>(userPassportDataResponse, HttpStatus.OK);
  }

  /**
   * Handle the /passports endpoint.
   * @param createOrUpdateUserPassportRequest request with parameters.
   * @return status 200 if passport data successfully created of updated.
   */
  @PostMapping
  public ResponseEntity<HttpStatus> createOrUpdate(
      @Valid @RequestBody CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest) {
    userPassportService.createOrUpdate(createOrUpdateUserPassportRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /passports/{id}/status endpoint.
   * @param id of the passport which status updated (confirmed / not confirmed).
   * Return one of the following status codes:
   * 200: if passport status successfully updated.
   * 405: if trying to update not confirmed passport status.
   * @throws PassportNotConfirmedException if passport not confirmed.
   */
  @PutMapping(path = "/{id}/status")
  public ResponseEntity<HttpStatus> updatePassportStatus(@NotNull @Positive @PathVariable Long id)
      throws PassportNotConfirmedException {
    userPassportService.updatePassportStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Handle the /passports/download endpoint.
   * @param userDocumentsDownloadRequest request with parameters.
   * @return zipped file of specified documents scans for confirmation.
   * Return one of the following status codes:
   * 200: successfully downloaded passport confirmation files.
   * 400: if bad request or invalid input parameters.
   */
  @PutMapping(path = "/download")
  public ResponseEntity<ByteArrayResource> downloadFiles(
      @Valid @RequestBody UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    var response = userPassportService.downloadFiles(userDocumentsDownloadRequest);
    return ResponseEntity.ok().contentLength(response.getByteArray().length)
        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(response);
  }

  /**
   * Handle the /passports/upload endpoint.
   * @param passportFile passport confirmation file.
   * Return one of the following status codes:
   * 200: successfully uploaded passport confirmation file.
   * 400: if bad request or invalid input parameters.
   */
  @PostMapping(
      path = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<HttpStatus> uploadFile(@NotNull @RequestParam MultipartFile passportFile) {
    userPassportService.uploadFile(passportFile);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
