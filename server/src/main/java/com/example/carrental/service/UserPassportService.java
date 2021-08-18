package com.example.carrental.service;

import com.example.carrental.service.exceptions.PassportNotConfirmedException;
import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.service.exceptions.NoContentException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for User Passports.
 * <p>
 * This interface describes actions on User Passports.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserPassportService {

  UserPassportDataResponse getUserPassportData() throws NoContentException;

  UserPassport findById(Long id);

  UserPassportConfirmationDataResponse findPassportDataById(Long id);

  void createOrUpdate(CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest);

  void update(Long id, UserPassport userPassport);

  void updatePassportStatus(Long id) throws PassportNotConfirmedException;

  ByteArrayResource downloadFiles(UserDocumentsDownloadRequest userDocumentsDownloadRequest);

  void uploadFile(MultipartFile passportFile);
}
