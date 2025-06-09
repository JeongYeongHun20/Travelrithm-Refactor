package com.Travelrithm.s3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import java.time.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3Service {

    private final String bucketName = "travelrithm-bucket";
    private final S3Presigner preSigner;
    private final String region = "ap-northeast-2";

    public Map<String,String> generatePreSignedPutUrl(String originalFilename, String contentType) {
        String key = "upload/" + UUID.randomUUID() + "_" + originalFilename;

        try{
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();


            PresignedPutObjectRequest preSignedRequest = preSigner.presignPutObject(b -> b
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
            );

            String preSignedUrl=preSignedRequest.url().toString();
            String dbUrl=preSignedUrl.split("\\?")[0];

            Map<String, String> url = new HashMap<>();
            url.put("preSignedUrl", preSignedUrl);
            url.put("dbUrl", dbUrl);

            return url;

        }catch (Exception e){
            throw new RuntimeException("PreSigned URL 생성 실패", e);
        }

    }

    public String generatePreSignedGetUrl(String key) {

        try{
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            PresignedGetObjectRequest preSignedRequest = preSigner.presignGetObject(b -> b
                    .signatureDuration(Duration.ofMinutes(10))//만료시간 10분
                    .getObjectRequest(getObjectRequest)
            );

            return preSignedRequest.url().toString();
        } catch (Exception e) {
            throw new RuntimeException("PreSigned URL 생성 실패", e);
        }
    }
    public String getPublicUrl(String key) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }
}