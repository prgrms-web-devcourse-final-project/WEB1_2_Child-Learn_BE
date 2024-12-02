//package com.prgrms.ijuju.domain.avatar.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.Value;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Slf4j
//public class S3ImageStorageService {
//
//    private final AmazonS3 s3client;
//
//    @Value("${aws.s3.bucket-name}")
//    private String bucketName;
//
//    // 이미지를 s3에 업로드하고 url을 반환하는 메서드
//    public String uploadImage(MultipartFile file) throws IOException {
//        String fileName = "profile-image-" + System.currentTimeMillis() + ".png";
//        InputStream inputStream = file.getInputStream();
//
//        // 메타데이터 설정(ContentType 설정 등)
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//
//        // 파일 업로드
//        s3client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
//
//        // 업로드된 파일의 s3 url 반환
//        return s3client.getUrl(bucketName, fileName).toString();
//    }
//}
