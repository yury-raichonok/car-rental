package com.example.carrental.service.impl;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
import static com.example.carrental.constants.ApplicationConstants.BRAND_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.CAR_IMAGE;
import static com.example.carrental.constants.ApplicationConstants.DRIVING_LICENSE_FILE;
import static com.example.carrental.constants.ApplicationConstants.PASSPORT_FILE;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.carrental.config.AmazonConfig;
import com.example.carrental.service.FileStoreService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * File Store service for performing operations on files.
 * <p>
 * This class saves and loads files from Amazon S3 cloud storage.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileStoreServiceImpl implements FileStoreService {

  private static final String AMAZON_LINK_PATTERN = "https://%s.s3.%s.amazonaws.com/%s";

  private final AmazonConfig amazonConfig;
  private final AmazonS3 s3;

  /**
   * @param fileType one of two: Car image / Brand image.
   * @param fileName data.
   * @param file to upload.
   * @return public link on uploaded tile.
   */
  public String uploadPublicImage(String fileType, String fileName, File file) {
    try {
      if (CAR_IMAGE.equals(fileType)) {
        s3.putObject(new PutObjectRequest(amazonConfig.getCarImagesBucket(), fileName, file)
            .withCannedAcl(PublicRead));
        return String.format(AMAZON_LINK_PATTERN, amazonConfig.getCarImagesBucket(),
            amazonConfig.getRegion(), fileName);
      } else if (BRAND_IMAGE.equals(fileType)) {
        s3.putObject(new PutObjectRequest(amazonConfig.getBrandImagesBucket(), fileName, file)
            .withCannedAcl(PublicRead));
        return String.format(AMAZON_LINK_PATTERN, amazonConfig.getBrandImagesBucket(),
            amazonConfig.getRegion(), fileName);
      } else {
        log.error("Wrong type of file {}. Can't upload to Amazon s3.", fileName);
        throw new IllegalStateException(
            String.format("Wrong type of file %s. Can't upload to Amazon s3.", fileName));
      }

    } catch (AmazonServiceException e) {
      log.error("Failed to store brand image {} to s3. Exception: {}", fileName, e.getMessage());
      throw new IllegalStateException(String
          .format("Failed to store brand image %s to s3. Exception: %s", fileName, e.getMessage()));
    }
  }

  /**
   * @param fileType one of two: Passport file / Driving license file.
   * @param fileName data.
   * @param file to upload.
   */
  @Override
  public void uploadFile(String fileType, String fileName, File file) {
    try {
      if (PASSPORT_FILE.equals(fileType)) {
        s3.putObject(new PutObjectRequest(amazonConfig.getPassportFilesBucket(), fileName, file));
      } else if (DRIVING_LICENSE_FILE.equals(fileType)) {
        s3.putObject(
            new PutObjectRequest(amazonConfig.getDrivingLicenseFilesBucket(), fileName, file));
      } else {
        log.error("Wrong type of file {}. Can't upload to Amazon s3.", fileName);
        throw new IllegalStateException(
            String.format("Wrong type of file %s. Can't upload to Amazon s3.", fileName));
      }
    } catch (AmazonServiceException e) {
      log.error("Failed to store file {} to s3. Exception: {}", fileName, e.getMessage());
      throw new IllegalStateException(String.format("Failed to store file %s to s3. Exception: %s",
          fileName, e.getMessage()));
    }
  }

  /**
   * @param fileType one of two: Passport file / Driving license file.
   * @param directory of files.
   * @return zipped files from specified Amazon S3 directory.
   */
  @Override
  public ByteArrayOutputStream downloadZippedFiles(String fileType, String directory) {
    ObjectListing objectListing;
    List<S3Object> s3ObjectList = new ArrayList<>();
    try {
      if (PASSPORT_FILE.equals(fileType)) {
        objectListing = s3.listObjects(amazonConfig.getPassportFilesBucket(), directory);
        objectListing.getObjectSummaries().forEach(oL -> s3ObjectList
            .add(s3.getObject(new GetObjectRequest(oL.getBucketName(), oL.getKey()))));
      } else if (DRIVING_LICENSE_FILE.equals(fileType)) {
        objectListing = s3.listObjects(amazonConfig.getDrivingLicenseFilesBucket(), directory);
        objectListing.getObjectSummaries().forEach(oL -> s3ObjectList
            .add(s3.getObject(new GetObjectRequest(oL.getBucketName(), oL.getKey()))));
      } else {
        log.error("Wrong type of file. Can't download file from {}.", directory);
        throw new IllegalStateException(
            String.format("Wrong type of file. Can't download file from %s.", directory));
      }
    } catch (AmazonServiceException e) {
      log.error("Failed to download files from {}. Exception: {}", directory, e.getMessage());
      throw new IllegalStateException(
          String.format("Failed to download files from %s. Exception: %s", directory,
              e.getMessage()));
    }

    var byteArrayOutputStream = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
      s3ObjectList.forEach(object -> {
        try (S3ObjectInputStream inputStream = object.getObjectContent()) {
          byte[] content = IOUtils.toByteArray(inputStream);
          ZipEntry entry = new ZipEntry(object.getKey());
          entry.setSize(content.length);
          zos.putNextEntry(entry);
          zos.write(content);
        } catch (IOException e) {
          log.error("Error when packing driving license files of user with id = {}. Message: {}",
              object.getKey(), e.getMessage());
          throw new IllegalStateException(String
              .format("Error when packing driving license files of user with id = %s. Message: %s",
                  object.getKey(), e.getMessage()));
        }
      });
    } catch (IOException e) {
      log.error("Error when downloading driving license files. Message: {}", e.getMessage());
      throw new IllegalStateException(String
          .format("Error when downloading driving license files. Message: %s", e.getMessage()));
    }
    return byteArrayOutputStream;
  }
}
