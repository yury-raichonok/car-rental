package com.example.carrental.repository;

import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStoreRepository {

  List<S3Object> downloadFiles(String bucketName, String directory);

  void uploadFile(String bucketName, String fileName, File file);

  void uploadPublicFile(String bucketName, String fileName, File file);
}
