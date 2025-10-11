package com.idea101.backendengine.common.service.impl;

import com.idea101.backendengine.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.Instant;

@Log4j2
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {

        String fileName = folder + "/" + Instant.now().toEpochMilli() + "_" + file.getOriginalFilename();

        log.info("Uploading file '{}' to bucket '{}' under '{}'", file.getOriginalFilename(), bucketName, fileName);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        String publicUrl = getFileUrl(fileName);
        log.info("File uploaded successfully: {}", publicUrl);
        return publicUrl;
    }

    @Override
    public void deleteFile(String fileUrl) {

        String fileKey = getKeyFromUrl(fileUrl);
        log.info("Deleting file '{}' from bucket '{}'", fileKey, bucketName);
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build());
        log.info("File deleted successfully: {}", fileKey);
    }

    @Override
    public String getFileUrl(String fileKey) {

        String region = s3Client.serviceClientConfiguration().region().id();
        String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileKey);
        log.debug("Generated public URL: {}", url);
        return url;
    }

    @Override
    public String getKeyFromUrl(String fileUrl) {

        return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
    }
}
