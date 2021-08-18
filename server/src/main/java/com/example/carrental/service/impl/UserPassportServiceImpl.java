package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.PASSPORT_FILE;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.mapper.UserPassportMapper;
import com.example.carrental.repository.UserPassportRepository;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.NoContentException;
import com.example.carrental.service.exceptions.PassportNotConfirmedException;
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
 * The service for User Passports.
 * <p>
 * This class performs the CRUD operations for User Passports.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserPassportServiceImpl implements UserPassportService {

  private final FileStoreService fileStoreService;
  private final UserPassportMapper userPassportMapper;
  private final UserPassportRepository userPassportRepository;
  private final UserSecurityService userSecurityService;
  private final UserService userService;

  /**
   * @return user passport data.
   * @throws NoContentException if user passport is not specified.
   */
  @Override
  public UserPassportDataResponse getUserPassportData() throws NoContentException {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
    var passport = Optional.ofNullable(user.getPassport()).orElseThrow(() -> {
      log.error("Passport of user {}  is not specified!", userEmail);
      throw new NoContentException("Passport data is not specified!");
    });
    return userPassportMapper.userPassportToUserPassportDataResponse(passport);
  }

  /**
   * @param id of user passport.
   * @return user passport.
   */
  @Override
  public UserPassport findById(Long id) {
    return userPassportRepository.findById(id).orElseThrow(() -> {
      log.error("User passport with id {} does not exist", id);
      throw new IllegalStateException(
          String.format("User passport with id %d does not exists", id));
    });
  }

  /**
   * @param id of user passport.
   * @return user passport response.
   */
  @Override
  public UserPassportConfirmationDataResponse findPassportDataById(Long id) {
    var passport = findById(id);
    return userPassportMapper.userPassportToUserPassportConfirmationDataResponse(passport);
  }

  /**
   * @param createOrUpdateUserPassportRequest data for creating or updating user passport.
   */
  @Override
  @Transactional
  public void createOrUpdate(
      CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
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
  }

  /**
   * @param id of user passport.
   * @param userPassport data for updating.
   */
  @Override
  @Transactional
  public void update(Long id, UserPassport userPassport) {
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
  }

  /**
   * @param id of user passport.
   * @throws PassportNotConfirmedException if user passport is pending.
   */
  @Override
  @Transactional
  public void updatePassportStatus(Long id) throws PassportNotConfirmedException {
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
        throw new PassportNotConfirmedException(
            "Passport is pending. You cannot change the status");
    }
    userPassportRepository.save(passport);
  }

  /**
   * @param userDocumentsDownloadRequest directory of files.
   * @return files from specified directory.
   */
  @Override
  public ByteArrayResource downloadFiles(
      UserDocumentsDownloadRequest userDocumentsDownloadRequest) {
    return new ByteArrayResource(fileStoreService.downloadZippedFiles(PASSPORT_FILE,
        userDocumentsDownloadRequest.getDirectory()).toByteArray());
  }

  /**
   * @param passportFile file for passport confirmation.
   */
  @Override
  @Transactional
  public void uploadFile(MultipartFile passportFile) {
    if (passportFile.isEmpty()) {
      log.error("Cannot upload empty file [{}]", passportFile.getSize());
      throw new IllegalStateException(String.format("Cannot upload empty file [%s]",
          passportFile.getSize()));
    }

    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);
    var passport = Optional.ofNullable(user.getPassport()).orElseThrow(() -> {
      log.error("Passport of user {} is not specified!", user.getEmail());
      throw new IllegalStateException(String.format("Passport of user %s is not specified!",
          user.getEmail()));
    });

    var filename = String.format("%s/%s-%s", passport.getId(), passport.getId(),
        passportFile.getOriginalFilename());
    var filesLink = String.valueOf(passport.getId());

    File file = new File(Objects.requireNonNull(passportFile.getOriginalFilename()));
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(passportFile.getBytes());
      fileStoreService.uploadFile(PASSPORT_FILE, filename, file);
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
    passport.setDocumentsFileLink(filesLink);
    passport.setChangedAt(LocalDateTime.now());
    userPassportRepository.save(passport);
  }
}
