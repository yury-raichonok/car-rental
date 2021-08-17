package com.example.carrental.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Configurations for Amazon S3 cloud storage.
 * <p>
 * This class provides data to save and retrieve data from Amazon S3 storage.
 * </p>
 * @author Yury Raichonak
 */
@Configuration
@ConfigurationProperties("amazon")
@RequiredArgsConstructor
@Data
@Validated
public class AmazonConfig {

  @NotBlank
  private String accessKey;
  @NotBlank
  private String secretKey;
  @NotBlank
  private String canonicalId;
  @NotBlank
  private String region;
  @Email
  private String email;
  @NotBlank
  private String carImagesBucket;
  @NotBlank
  private String brandImagesBucket;
  @NotBlank
  private String passportFilesBucket;
  @NotBlank
  private String drivingLicenseFilesBucket;

  @Bean
  public AmazonS3 s3() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(
        accessKey,
        secretKey
    );
    return AmazonS3ClientBuilder.standard().withRegion(region).
        withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
  }
}
