package com.example.carrental.service;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseDataResponse;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.NoContentException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The service for User Driving Licenses.
 * <p>
 * This interface describes actions on User Driving Licenses.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserDrivingLicenseService {

  UserDrivingLicenseConfirmationDataResponse findDrivingLicenseById(Long id);

  UserDrivingLicenseDataResponse findUserDrivingLicenseData()
      throws NoContentException;

  UserDrivingLicense findById(Long id);

  void createOrUpdate(
      CreateOrUpdateUserDrivingLicenseRequest createOrUpdateUserDrivingLicenseRequest);

  void update(Long id, UserDrivingLicense userDrivingLicense);

  void updateDrivingLicenseStatus(Long id) throws DrivingLicenseNotConfirmedException;

  void uploadFile(MultipartFile drivingLicenseFile);

  ByteArrayResource downloadFiles(UserDocumentsDownloadRequest userDocumentsDownloadRequest);
}
