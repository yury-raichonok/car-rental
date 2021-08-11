package com.example.carrental.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.carrental.repository.FileStoreRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileStoreAmazonS3RepositoryImpl implements FileStoreRepository {

  private final AmazonS3 s3;


  @Override
  public String uploadPublicFile(String bucketName, String fileName, File file) {
    s3.putObject(new PutObjectRequest(bucketName, fileName, file)
        .withCannedAcl(CannedAccessControlList.PublicRead));
    return "Success";
  }

  @Override
  public String uploadFile(String bucketName, String fileName, File file) {
    s3.putObject(new PutObjectRequest(bucketName, fileName, file));
    return "Success";
  }

  @Override
  public List<S3Object> downloadFiles(String bucketName, String directory) {
    ObjectListing objectListing = s3.listObjects(bucketName, directory);
    List<S3Object> s3ObjectList = new ArrayList<>();
    objectListing.getObjectSummaries().forEach(oL -> s3ObjectList
        .add(s3.getObject(new GetObjectRequest(oL.getBucketName(), oL.getKey()))));
    return s3ObjectList;
  }
}
