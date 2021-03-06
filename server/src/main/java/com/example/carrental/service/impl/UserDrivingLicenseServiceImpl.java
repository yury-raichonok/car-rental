package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.DRIVING_LICENSE_FILE;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseDataResponse;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.mapper.UserDrivingLicenseMapper;
import com.example.carrental.repository.UserDrivingLicenseRepository;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.NoContentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for User Driving Licensea.
 * <p>
 * This class performs the CRUD operations for User Driving Licenses.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDrivingLicenseServiceImpl implements UserDrivingLicenseService {

  private final FileStoreService fileStoreService;
  private final UserDrivingLicenseMapper userDrivingLicenseMapper;
  private final UserDrivingLicenseRepository userDrivingLicenseRepository;
  private final UserSecurityService userSecurityService;
  private final UserService userService;

  /**
   * @param id of driving license.
   * @return user driving license confirmation data.
   */
  @Override
  public UserDrivingLicenseConfirmationDataResponse findDrivingLicenseById(Long id) {
    var drivingLicense = findById(id);
    return userDrivingLicenseMapper
        .drivingLicenseToUserDrivingLicenseConfirmationDataResponse(drivingLicense);
  }

  /**
   * @return user driving license data.
   * @throws NoContentException if driving license is not specified.
   */
  @Override
  public UserDrivingLicenseDataResponse findUserDrivingLicenseData() throws NoContentException {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
    var optionalDrivingLicense = Optional.ofNullable(user.getDrivingLicense());
    if (optionalDrivingLicense.isEmpty()) {
      log.error("Driving license of user {}  is not specified!", userEmail);
      throw new NoContentException("Driving license data is not specified!");
    }
    var drivingLicense = optionalDrivingLicense.get();

    return UserDrivingLicenseDataResponse
        .builder()
        .dateOfIssue(drivingLicense.getDateOfIssue())
        .validityPeriod(drivingLicense.getValidityPeriod())
        .organizationThatIssued(drivingLicense.getOrganizationThatIssued())
        .build();
  }

  /**
   * @param id of driving license.
   * @return driving license.
   * @throws IllegalStateException if driving license with specified id does not exists.
   */
  @Override
  public UserDrivingLicense findById(Long id) {
    return userDrivingLicenseRepository.findById(id).orElseThrow(() -> {
      log.error("User driving license with id {} does not exist", id);
      throw new IllegalStateException(
          String.format("User driving license with id %d does not exists", id));
    });
  }

  /**
   * @param createOrUpdateUserDrivingLicenseRequest data for create of update driving license.
   */
  @Override
  @Transactional
  public void createOrUpdate(
      CreateOrUpdateUserDrivingLicenseRequest createOrUpdateUserDrivingLicenseRequest) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
    var drivingLicense = user.getDrivingLicense();

    if (Optional.ofNullable(drivingLicense).isPresent()) {
      drivingLicense.setDateOfIssue(createOrUpdateUserDrivingLicenseRequest.getDateOfIssue());
      drivingLicense.setValidityPeriod(createOrUpdateUserDrivingLicenseRequest.getValidityPeriod());
      drivingLicense.setOrganizationThatIssued(
          createOrUpdateUserDrivingLicenseRequest.getOrganizationThatIssued());
      drivingLicense.setChangedAt(LocalDateTime.now());
      drivingLicense.setConfirmedAt(null);
      drivingLicense.setStatus(UserDocumentStatus.NOT_CONFIRMED);
      userDrivingLicenseRepository.save(drivingLicense);
    } else {
      userDrivingLicenseRepository.save(UserDrivingLicense
          .builder()
          .dateOfIssue(createOrUpdateUserDrivingLicenseRequest.getDateOfIssue())
          .validityPeriod(createOrUpdateUserDrivingLicenseRequest.getValidityPeriod())
          .organizationThatIssued(
              createOrUpdateUserDrivingLicenseRequest.getOrganizationThatIssued())
          .createdAt(LocalDateTime.now())
          .status(UserDocumentStatus.NOT_CONFIRMED)
          .user(user)
          .build());
    }
  }

  /**
   * @param id of user driving license.
   * @param userDrivingLicense such updated.
   */
  @Override
  @Transactional
  public void update(Long id, UserDrivingLicense userDrivingLicense) {
    var drivingLicense = findById(id);

    drivingLicense.setDateOfIssue(userDrivingLicense.getDateOfIssue());
    drivingLicense.setValidityPeriod(userDrivingLicense.getValidityPeriod());
    drivingLicense.setOrganizationThatIssued(userDrivingLicense.getOrganizationThatIssued());
    drivingLicense.setStatus(userDrivingLicense.getStatus());
    drivingLicense.setCreatedAt(userDrivingLicense.getCreatedAt());
    drivingLicense.setChangedAt(userDrivingLicense.getChangedAt());
    drivingLicense.setConfirmedAt(userDrivingLicense.getConfirmedAt());
    userDrivingLicense.setDocumentsFileLink(userDrivingLicense.getDocumentsFileLink());

    userDrivingLicenseRepository.save(drivingLicense);
  }

  /**
   * @param id of driving license.
   * @throws DrivingLicenseNotConfirmedException if driving license is pending.
   */
  @Override
  @Transactional
  public void updateDrivingLicenseStatus(Long id) throws DrivingLicenseNotConfirmedException {
    var drivingLicense = findById(id);
    switch (drivingLicense.getStatus()) {
      case CONFIRMED:
        drivingLicense.setStatus(UserDocumentStatus.NOT_CONFIRMED);
        drivingLicense.setConfirmedAt(null);
        break;
      case NOT_CONFIRMED:
        drivingLicense.setStatus(UserDocumentStatus.CONFIRMED);
        drivingLicense.setConfirmedAt(LocalDateTime.now());
        break;
      case UNDER_CONSIDERATION:
        log.error("Driver's license is pending. You cannot change the status");
        throw new DrivingLicenseNotConfirmedException(
            "Driver's license is pending. You cannot change the status");
    }
    userDrivingLicenseRepository.save(drivingLicense);
  }

  /**
   * @param drivingLicenseFile file for confirmation documents.
   * @throws IllegalStateException if wrong input data.
   */
  @Override
  public void uploadFile(MultipartFile drivingLicenseFile) {
    if (drivingLicenseFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", drivingLicenseFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          drivingLicenseFile.getSize()));
    }

    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
    var drivingLicense = Optional.ofNullable(user.getDrivingLicense()).orElseThrow(() -> {
      log.error("Driving license of user {} is not specified!", user.getEmail());
      throw new IllegalStateException(String.format("Driving license of user %s is not specified!",
          user.getEmail()));
    });

    var filename = String.format("%s/%s-%s", drivingLicense.getId(), drivingLicense.getId(),
        drivingLicenseFile.getOriginalFilename());
    var filesLink = String.valueOf(drivingLicense.getId());

    var file = new File(Objects.requireNonNull(drivingLicenseFile.getOriginalFilename()));
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(drivingLicenseFile.getBytes());
      fileStoreService.uploadFile(DRIVING_LICENSE_FILE, filename, file);
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
    drivingLicense.setDocumentsFileLink(filesLink);
    drivingLicense.setChangedAt(LocalDateTime.now());
    userDrivingLicenseRepository.save(drivingLicense);
  }

  /**
   * @param userDocumentsDownloadRequest directory from which download files.
   * @return files from specified directory.
   */
  @Override
  public ByteArrayResource downloadFiles(
      UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    return new ByteArrayResource(fileStoreService.downloadZippedFiles(DRIVING_LICENSE_FILE,
        userDocumentsDownloadRequest.getDirectory()).toByteArray());
  }
}
