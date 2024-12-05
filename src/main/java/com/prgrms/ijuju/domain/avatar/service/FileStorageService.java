package com.prgrms.ijuju.domain.avatar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        //저장할 파일 경로
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // 파일 이름과 확장자 추출
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        // resolve()를 사용하여 디렉토리와 파일이름 결합
        Path filePath = path.resolve(fileName);

        // 파일을 로컬 디렉토리에 저장
        Files.copy(file.getInputStream(), path);

        // 저장된 파일의 경로를 URL 형식으로 반환
        return "/uploads/" + fileName;  // URL 경로
    }
}
