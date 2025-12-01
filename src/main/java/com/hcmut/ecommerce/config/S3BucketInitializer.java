package com.hcmut.ecommerce.config;

import io.awspring.cloud.s3.S3Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class S3BucketInitializer implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(S3BucketInitializer.class);

  private final S3Template s3Template;
  private final S3BucketProperties s3BucketProperties;

  public S3BucketInitializer(S3Template s3Template, S3BucketProperties s3BucketProperties) {
    this.s3Template = s3Template;
    this.s3BucketProperties = s3BucketProperties;
  }

  @Override
  public void run(String... args) throws Exception {
    String bucketName = s3BucketProperties.getBucketName();

    if (bucketName == null || bucketName.isEmpty()) {
      logger.warn("S3 bucket name is not configured. Skipping bucket initialization.");
      return;
    }

    try {
      // Check if bucket exists using S3Template
      if (bucketExists(bucketName)) {
        logger.info("S3 bucket '{}' already exists.", bucketName);
      } else {
        // Create bucket if it doesn't exist
        createBucket(bucketName);
        logger.info("S3 bucket '{}' created successfully.", bucketName);
      }
    } catch (Exception e) {
      logger.error("Error initializing S3 bucket: {}", e.getMessage(), e);
      throw e;
    }
  }

  private boolean bucketExists(String bucketName) {
    try {
      // Use S3Operations to check if bucket exists
      S3Client s3Client = getS3Client();
      if (s3Client == null) {
        logger.warn("Could not access S3Client from S3Template");
        return false;
      }

      HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
          .bucket(bucketName)
          .build();
      s3Client.headBucket(headBucketRequest);
      logger.debug("Bucket '{}' exists in S3.", bucketName);
      return true;
    } catch (Exception e) {
      logger.debug("Bucket '{}' does not exist: {}", bucketName, e.getMessage());
      return false;
    }
  }

  private void createBucket(String bucketName) {
    try {
      // Use S3Template to create bucket
      s3Template.createBucket(bucketName);
      logger.info("Bucket '{}' has been created.", bucketName);
    } catch (Exception e) {
      logger.error("Error creating bucket '{}': {}", bucketName, e.getMessage(), e);
      throw new RuntimeException("Failed to create S3 bucket: " + bucketName, e);
    }
  }

  private S3Client getS3Client() {
    try {
      // S3Template has a getS3Client() method in recent versions
      return (S3Client) s3Template.getClass()
          .getMethod("getS3Client")
          .invoke(s3Template);
    } catch (Exception e) {
      logger.warn("Could not get S3Client via reflection: {}", e.getMessage());
      return null;
    }
  }
}
