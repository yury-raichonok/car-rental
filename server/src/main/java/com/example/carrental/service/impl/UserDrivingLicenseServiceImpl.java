package com.example.carrental.service.impl;

import com.amazonaws.services.s3.model.S3Object;
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
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.NoDrivingLicenseDataException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @Value("${amazon.drivinglicense.files.bucket}")
  private String drivingLicenseFilesBucket;

  @Override
  public UserDrivingLicenseConfirmationDataResponse findDrivingLicenseById(Long id) {
    var drivingLicense = findById(id);
    return userDrivingLicenseMapper
        .drivingLicenseToUserDrivingLicenseConfirmationDataResponse(drivingLicense);
  }

  @Override
  public UserDrivingLicenseDataResponse getUserDrivingLicenseData()
      throws NoDrivingLicenseDataException {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var user = userService.findUserByEmail(username);
    Optional<UserDrivingLicense> optionalDrivingLicense = Optional
        .ofNullable(user.getDrivingLicense());

    if (optionalDrivingLicense.isEmpty()) {
      log.error("Driving license of user {}  is not specified!", username);
      throw new NoDrivingLicenseDataException("Driving license data is not specified!");
    }

    var passport = optionalDrivingLicense.get();

    return UserDrivingLicenseDataResponse
        .builder()
        .dateOfIssue(passport.getDateOfIssue())
        .validityPeriod(passport.getValidityPeriod())
        .organizationThatIssued(passport.getOrganizationThatIssued())
        .build();
  }

  @Override
  public UserDrivingLicense findById(Long id) {
    var optionalDrivingLicense = userDrivingLicenseRepository.findById(id);
    if (optionalDrivingLicense.isEmpty()) {
      log.error("User driving license with id {} does not exist", id);
      throw new IllegalStateException(
          String.format("User driving license with id %d does not exists", id));
    }
    return optionalDrivingLicense.get();
  }

  @Override
  @Transactional
  public String createOrUpdate(
      CreateOrUpdateUserDrivingLicenseRequest createOrUpdateUserDrivingLicenseRequest) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var user = userService.findUserByEmail(username);
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
    return "Success";
  }

  @Override
  @Transactional
  public String update(Long id, UserDrivingLicense userDrivingLicense) {
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
    return "Success";
  }

  @Override
  @Transactional
  public String updateDrivingLicenseStatus(Long id) throws DocumentsNotConfirmedException {
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
        throw new DocumentsNotConfirmedException(
            "Driver's license is pending. You cannot change the status");
    }
    userDrivingLicenseRepository.save(drivingLicense);
    return "Success";
  }

  @Override
  public String uploadFile(MultipartFile drivingLicenseFile) {
    if (drivingLicenseFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", drivingLicenseFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          drivingLicenseFile.getSize()));
    }

    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }
    var user = userService.findUserByEmail(username);

    var optionalDrivingLicense = Optional.ofNullable(user.getDrivingLicense());
    if (optionalDrivingLicense.isEmpty()) {
      log.error("Driving license of user {} is not specified!", user.getEmail());
      throw new IllegalStateException(String.format("Driving license of user %s is not specified!",
          user.getEmail()));
    }

    var drivingLicense = optionalDrivingLicense.get();

    String filename = String.format("%s/%s-%s", drivingLicense.getId(), drivingLicense.getId(),
        drivingLicenseFile.getOriginalFilename());
    String filesLink = String.valueOf(drivingLicense.getId());

    try {
      File file = new File(Objects.requireNonNull(drivingLicenseFile.getOriginalFilename()));
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(drivingLicenseFile.getBytes());
      fileOutputStream.close();

      fileStoreService.uploadFile(drivingLicenseFilesBucket, filename, file);

      file.delete();
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }

    drivingLicense.setDocumentsFileLink(filesLink);
    drivingLicense.setChangedAt(LocalDateTime.now());
    userDrivingLicenseRepository.save(drivingLicense);
    return "Success";
  }

  @Override
  public ByteArrayResource downloadFiles(
      UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    List<S3Object> objects = fileStoreService
        .downloadFiles(drivingLicenseFilesBucket, userDocumentsDownloadRequest.getDirectory());

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);

    objects.forEach(object -> {
      try {
        S3ObjectInputStream inputStream = object.getObjectContent();
        byte[] content = IOUtils.toByteArray(inputStream);
        ZipEntry entry = new ZipEntry(object.getKey());
        entry.setSize(content.length);
        zos.putNextEntry(entry);
        zos.write(content);
        inputStream.close();
      } catch (IOException e) {
        log.error("Error when packing driving license files of user with id = {}. Message: {}",
            object.getKey(), e.getMessage());
        throw new IllegalStateException(String
            .format("Error when packing driving license files of user with id = %s. Message: %s",
                object.getKey(), e.getMessage()));
      }
    });
    try {
      zos.closeEntry();
      zos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ByteArrayResource(byteArrayOutputStream.toByteArray());
  }
}
