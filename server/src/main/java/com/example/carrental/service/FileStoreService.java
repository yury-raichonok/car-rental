package com.example.carrental.service;

import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface FileStoreService {

  void uploadPublicFile(String bucketName, String fileName, File file);

  void uploadFile(String bucketName, String fileName, File file);

  List<S3Object> downloadFiles(String bucketName, String directory);
}
