package com.example.carrental.service;

import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface FileStoreService {

  String uploadPublicFile(String bucketName, String fileName, File file);

  String uploadFile(String bucketName, String fileName, File file);

  List<S3Object> downloadFiles(String bucketName, String directory);
}
