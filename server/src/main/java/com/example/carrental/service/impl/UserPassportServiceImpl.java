package com.example.carrental.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.mapper.UserPassportMapper;
import com.example.carrental.repository.UserPassportRepository;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.NoPassportDataException;
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
public class UserPassportServiceImpl implements UserPassportService {

  private final UserPassportRepository userPassportRepository;
  private final UserService userService;
  private final UserPassportMapper userPassportMapper;
  private final FileStoreService fileStoreService;

  @Value("${amazon.passport.files.bucket}")
  private String passportFilesBucket;

  @Override
  public UserPassportDataResponse getUserPassportData() throws NoPassportDataException {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var user = userService.findUserByEmail(username);
    Optional<UserPassport> optionalPassport = Optional.ofNullable(user.getPassport());

    if (optionalPassport.isEmpty()) {
      log.error("Passport of user {}  is not specified!", username);
      throw new NoPassportDataException("Passport data is not specified!");
    }

    var passport = optionalPassport.get();

    return userPassportMapper.userPassportToUserPassportDataResponse(passport);
  }

  @Override
  public UserPassport findById(Long id) {
    var optionalPassport = userPassportRepository.findById(id);
    if (optionalPassport.isEmpty()) {
      log.error("User passport with id {} does not exist", id);
      throw new IllegalStateException(
          String.format("User passport with id %d does not exists", id));
    }
    return optionalPassport.get();
  }

  @Override
  public UserPassportConfirmationDataResponse findPassportDataById(Long id) {
    var passport = findById(id);
    return userPassportMapper.userPassportToUserPassportConfirmationDataResponse(passport);
  }

  @Override
  @Transactional
  public String createOrUpdate(
      CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }
    var user = userService.findUserByEmail(username);
    var passport = user.getPassport();

    if (Optional.ofNullable(passport).isPresent()) {
      passport.setFirstName(createOrUpdateUserPassportRequest.getFirstName());
      passport.setMiddleName(createOrUpdateUserPassportRequest.getMiddleName());
      passport.setLastName(createOrUpdateUserPassportRequest.getLastName());
      passport.setDateOfBirth(createOrUpdateUserPassportRequest.getDateOfBirth());
      passport.setPassportSeries(createOrUpdateUserPassportRequest.getPassportSeries());
      passport.setPassportNumber(createOrUpdateUserPassportRequest.getPassportNumber());
      passport.setDateOfIssue(createOrUpdateUserPassportRequest.getDateOfIssue());
      passport.setValidityPeriod(createOrUpdateUserPassportRequest.getValidityPeriod());
      passport
          .setOrganizationThatIssued(createOrUpdateUserPassportRequest.getOrganizationThatIssued());
      passport.setChangedAt(LocalDateTime.now());
      passport.setStatus(UserDocumentStatus.NOT_CONFIRMED);
      passport.setConfirmedAt(null);
      userPassportRepository.save(passport);
    } else {
      userPassportRepository.save(UserPassport
          .builder()
          .firstName(createOrUpdateUserPassportRequest.getFirstName())
          .middleName(createOrUpdateUserPassportRequest.getMiddleName())
          .lastName(createOrUpdateUserPassportRequest.getLastName())
          .dateOfBirth(createOrUpdateUserPassportRequest.getDateOfBirth())
          .passportSeries(createOrUpdateUserPassportRequest.getPassportSeries())
          .passportNumber(createOrUpdateUserPassportRequest.getPassportNumber())
          .dateOfIssue(createOrUpdateUserPassportRequest.getDateOfIssue())
          .validityPeriod(createOrUpdateUserPassportRequest.getValidityPeriod())
          .organizationThatIssued(createOrUpdateUserPassportRequest.getOrganizationThatIssued())
          .createdAt(LocalDateTime.now())
          .status(UserDocumentStatus.NOT_CONFIRMED)
          .user(user)
          .build());
    }
    return "Success";
  }

  @Override
  @Transactional
  public String update(Long id, UserPassport userPassport) {
    var passport = findById(id);

    passport.setFirstName(userPassport.getFirstName());
    passport.setMiddleName(userPassport.getMiddleName());
    passport.setLastName(userPassport.getLastName());
    passport.setDateOfBirth(userPassport.getDateOfBirth());
    passport.setPassportSeries(userPassport.getPassportSeries());
    passport.setPassportNumber(userPassport.getPassportNumber());
    passport.setDateOfIssue(userPassport.getDateOfIssue());
    passport.setValidityPeriod(userPassport.getValidityPeriod());
    passport.setOrganizationThatIssued(userPassport.getOrganizationThatIssued());
    passport.setStatus(userPassport.getStatus());
    passport.setCreatedAt(userPassport.getCreatedAt());
    passport.setChangedAt(userPassport.getChangedAt());
    passport.setConfirmedAt(userPassport.getConfirmedAt());
    passport.setDocumentsFileLink(userPassport.getDocumentsFileLink());

    userPassportRepository.save(passport);
    return "Success";
  }

  @Override
  @Transactional
  public String updatePassportStatus(Long id) throws DocumentsNotConfirmedException {
    var passport = findById(id);
    switch (passport.getStatus()) {
      case CONFIRMED:
        passport.setStatus(UserDocumentStatus.NOT_CONFIRMED);
        passport.setConfirmedAt(null);
        break;
      case NOT_CONFIRMED:
        passport.setStatus(UserDocumentStatus.CONFIRMED);
        passport.setConfirmedAt(LocalDateTime.now());
        break;
      case UNDER_CONSIDERATION:
        log.error("Passport is pending. You cannot change the status");
        throw new DocumentsNotConfirmedException(
            "Passport is pending. You cannot change the status");
    }
    userPassportRepository.save(passport);
    return "Success";
  }

  @Override
  @Transactional
  public String uploadFile(MultipartFile passportFile) {
    if (passportFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", passportFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          passportFile.getSize()));
    }

    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }
    var user = userService.findUserByEmail(username);

    var optionalPassport = Optional.ofNullable(user.getPassport());
    if (optionalPassport.isEmpty()) {
      log.error("Passport of user {} is not specified!", user.getEmail());
      throw new IllegalStateException(String.format("Passport of user %s is not specified!",
          user.getEmail()));
    }

    var passport = optionalPassport.get();

    String filename = String.format("%s/%s-%s", passport.getId(), passport.getId(),
        passportFile.getOriginalFilename());
    String filesLink = String.valueOf(passport.getId());

    try {
      File file = new File(Objects.requireNonNull(passportFile.getOriginalFilename()));
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(passportFile.getBytes());
      fileOutputStream.close();

      fileStoreService.uploadFile(passportFilesBucket, filename, file);

      file.delete();
    } catch (IOException e) {
      log.error("Error while uploading file to storage: {}", e.getMessage());
      throw new IllegalStateException(String.format("Error while uploading file to storage: %s",
          e.getMessage()));
    }

    passport.setDocumentsFileLink(filesLink);
    passport.setChangedAt(LocalDateTime.now());
    userPassportRepository.save(passport);
    return "Success";
  }

  @Override
  public ByteArrayResource downloadFiles(
      UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    List<S3Object> objects = fileStoreService
        .downloadFiles(passportFilesBucket, userDocumentsDownloadRequest.getDirectory());

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
        log.error("Error when packing passport files of user with id = {}. Message: {}",
            object.getKey(), e.getMessage());
        throw new IllegalStateException(String
            .format("Error when packing passport files of user with id = %s. Message: %s",
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
