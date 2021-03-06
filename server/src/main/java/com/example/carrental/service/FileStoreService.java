package com.example.carrental.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import org.springframework.stereotype.Service;

/**
 * The service for File Storage.
 * <p>
 * This interface describes actions for File Storage.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface FileStoreService {

  String uploadPublicImage(String fileType, String fileName, File file);

  void uploadFile(String fileType, String fileName, File file);

  ByteArrayOutputStream downloadZippedFiles(String fileType, String directory);
}
