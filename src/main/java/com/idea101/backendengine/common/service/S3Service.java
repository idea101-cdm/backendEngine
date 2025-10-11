package com.idea101.backendengine.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    String uploadFile (MultipartFile file, String folder) throws IOException;
    void deleteFile(String fileUrl);
    String getFileUrl(String fileKey);
    String getKeyFromUrl(String fileUrl);
}
