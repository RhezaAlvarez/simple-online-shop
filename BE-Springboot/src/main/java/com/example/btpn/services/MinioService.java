package com.example.btpn.services;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MinioService {
    @Autowired
    MinioClient minioClient;

    public void uploadFile(String bucketName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getOriginalFilename())
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            log.info("File uploaded successfully.");
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidResponseException | ServerException | XmlParserException |
                 InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.info("Can't upload file: " + e.getMessage());
        }
    }

    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            log.info("Download:" + objectName);
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | ServerException | XmlParserException | InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | IOException e) {
            log.info("Failed to download file");
        }
        return null;
    }

    public String getLink(String bucket, String filename, Integer expiry){
        log.info("Get Url Function");
        try {
            return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(filename)
                            .expiry(expiry, TimeUnit.HOURS)
                            .build()
            );
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | MinioException | IOException e) {
            log.info("Failed to get URL");
            return "Failed to get the URL";
        }
    }
}
