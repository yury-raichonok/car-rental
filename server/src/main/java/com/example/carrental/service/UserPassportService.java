package com.example.carrental.service;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.NoPassportDataException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserPassportService {

  UserPassportDataResponse getUserPassportData() throws NoPassportDataException;

  UserPassport findById(Long id);

  UserPassportConfirmationDataResponse findPassportDataById(Long id);

  String createOrUpdate(CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest);

  String update(Long id, UserPassport userPassport);

  String updatePassportStatus(Long id) throws DocumentsNotConfirmedException;

  String uploadFile(MultipartFile passportFile);

  ByteArrayResource downloadFiles(UserDocumentsDownloadRequest userDocumentsDownloadRequest);
}
