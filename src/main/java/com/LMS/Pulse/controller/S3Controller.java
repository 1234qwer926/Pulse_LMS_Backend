package com.LMS.Pulse.controller;

import com.LMS.Pulse.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String key = s3Service.uploadFile(file);
        return ResponseEntity.ok(key); // Return the file key
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> download(@PathVariable String key) {
        byte[] data = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                .body(data);
    }

    @GetMapping("/generate-presigned-url/{key}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String key) {
        String url = s3Service.generatePresignedUrl(key);
        return ResponseEntity.ok(url);
    }
}
