package com.example.carrental.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import org.springframework.stereotype.Service;

@Service
public interface FileStoreService {

  String uploadPublicImage(String fileType, String fileName, File file);

  void uploadFile(String fileType, String fileName, File file);

  ByteArrayOutputStream downloadZippedFiles(String fileType, String directory);
}
