package com.idea101.backendengine.common.demo;

import com.idea101.backendengine.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class DemoController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = s3Service.uploadFile(file, "public");
        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("key") String key) {
        s3Service.deleteFile(key);
        return ResponseEntity.ok("Deleted successfully");
    }
}
