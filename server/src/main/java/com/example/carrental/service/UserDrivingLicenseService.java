package com.example.carrental.service;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseDataResponse;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.NoDrivingLicenseDataException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserDrivingLicenseService {

  UserDrivingLicenseConfirmationDataResponse findDrivingLicenseById(Long id);

  UserDrivingLicenseDataResponse getUserDrivingLicenseData() throws NoDrivingLicenseDataException;

  UserDrivingLicense findById(Long id);

  String createOrUpdate(
      CreateOrUpdateUserDrivingLicenseRequest createOrUpdateUserDrivingLicenseRequest);

  String update(Long id, UserDrivingLicense userDrivingLicense);

  String updateDrivingLicenseStatus(Long id) throws DocumentsNotConfirmedException;

  String uploadFile(MultipartFile drivingLicenseFile);

  ByteArrayResource downloadFiles(UserDocumentsDownloadRequest userDocumentsDownloadRequest);
}
