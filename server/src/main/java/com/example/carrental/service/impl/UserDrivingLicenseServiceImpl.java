package com.example.carrental.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDrivingLicenseServiceImpl implements UserDrivingLicenseService {

  private final UserDrivingLicenseRepository userDrivingLicenseRepository;
  private final UserService userService;
  private final UserDrivingLicenseMapper userDrivingLicenseMapper;
  private final FileStoreService fileStoreService;
  private final UserSecurityService userSecurityService;

  @Value("${amazon.drivinglicense.files.bucket}")
  private String drivingLicenseFilesBucket;

  @Override
  public UserDrivingLicenseConfirmationDataResponse findDrivingLicenseById(Long id) {
    var drivingLicense = findById(id);
    return userDrivingLicenseMapper
        .drivingLicenseToUserDrivingLicenseConfirmationDataResponse(drivingLicense);
  }

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

  @Override
  public UserDrivingLicense findById(Long id) {
    return userDrivingLicenseRepository.findById(id).orElseThrow(() -> {
      log.error("User driving license with id {} does not exist", id);
      throw new IllegalStateException(
          String.format("User driving license with id %d does not exists", id));
    });
  }

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

  @Override
  public void uploadFile(MultipartFile drivingLicenseFile) {
    if (drivingLicenseFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", drivingLicenseFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          drivingLicenseFile.getSize()));
    }

    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
    var optionalDrivingLicense = Optional.ofNullable(user.getDrivingLicense());
    if (optionalDrivingLicense.isEmpty()) {
      log.error("Driving license of user {} is not specified!", user.getEmail());
      throw new IllegalStateException(String.format("Driving license of user %s is not specified!",
          user.getEmail()));
    }
    var drivingLicense = optionalDrivingLicense.get();

    var filename = String.format("%s/%s-%s", drivingLicense.getId(), drivingLicense.getId(),
        drivingLicenseFile.getOriginalFilename());
    var filesLink = String.valueOf(drivingLicense.getId());

    var file = new File(Objects.requireNonNull(drivingLicenseFile.getOriginalFilename()));
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(drivingLicenseFile.getBytes());
      fileStoreService.uploadFile(drivingLicenseFilesBucket, filename, file);
      Files.delete(Path.of(file.getPath()));
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }

    drivingLicense.setDocumentsFileLink(filesLink);
    drivingLicense.setChangedAt(LocalDateTime.now());
    userDrivingLicenseRepository.save(drivingLicense);
  }

  @Override
  public ByteArrayResource downloadFiles(
      UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    var objects = fileStoreService.downloadFiles(drivingLicenseFilesBucket,
        userDocumentsDownloadRequest.getDirectory());
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
      objects.forEach(object -> {
        try (S3ObjectInputStream inputStream = object.getObjectContent()) {
          byte[] content = IOUtils.toByteArray(inputStream);
          ZipEntry entry = new ZipEntry(object.getKey());
          entry.setSize(content.length);
          zos.putNextEntry(entry);
          zos.write(content);
        } catch (IOException e) {
          log.error("Error when packing driving license files of user with id = {}. Message: {}",
              object.getKey(), e.getMessage());
          throw new IllegalStateException(String
              .format("Error when packing driving license files of user with id = %s. Message: %s",
                  object.getKey(), e.getMessage()));
        }
      });
    } catch (IOException e) {
      log.error("Error when downloading driving license files. Message: {}", e.getMessage());
      throw new IllegalStateException(String
          .format("Error when downloading driving license files. Message: %s", e.getMessage()));
    }
    return new ByteArrayResource(byteArrayOutputStream.toByteArray());
  }
}
