package com.Travelrithm.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class S3Controller {


    private final S3Service s3Service;

    @GetMapping("/upload-url")
        public Map<String, String> getUploadUrl(@RequestParam String filename, @RequestParam String contentType) {
        return s3Service.generatePreSignedPutUrl(filename, contentType);
    }

    @GetMapping("/download-url")
    public String getDownloadUrl(@RequestParam String key) {
        return s3Service.generatePreSignedGetUrl(key);
    }
}
