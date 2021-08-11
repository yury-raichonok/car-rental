package com.example.carrental.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;
import com.example.carrental.repository.FileStoreRepository;
import com.example.carrental.service.FileStoreService;
import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStoreServiceImpl implements FileStoreService {

  private final FileStoreRepository fileStoreRepository;

  @Override
  public String uploadPublicFile(String bucketName, String fileName, File file) {
    try {
      return fileStoreRepository.uploadPublicFile(bucketName, fileName, file);
    } catch (AmazonServiceException e) {
      log.error("Failed to store file {} to s3. Exception: {}", fileName, e.getMessage());
      throw new IllegalStateException(String.format("Failed to store file %s to s3. Exception: %s",
          fileName, e.getMessage()));
    }
  }

  @Override
  public String uploadFile(String bucketName, String fileName, File file) {
    try {
      return fileStoreRepository.uploadFile(bucketName, fileName, file);
    } catch (AmazonServiceException e) {
      log.error("Failed to store file {} to s3. Exception: {}", fileName, e.getMessage());
      throw new IllegalStateException(String.format("Failed to store file %s to s3. Exception: %s",
          fileName, e.getMessage()));
    }
  }

  @Override
  public List<S3Object> downloadFiles(String bucketName, String directory) {
    try {
      return fileStoreRepository.downloadFiles(bucketName, directory);
    } catch (AmazonServiceException e) {
      log.error("Failed to download files from {}. Exception: {}", directory, e.getMessage());
      throw new IllegalStateException(
          String.format("Failed to download files from %s. Exception: %s", directory,
              e.getMessage()));
    }
  }
}
