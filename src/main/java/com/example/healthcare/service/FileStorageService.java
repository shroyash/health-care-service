package com.example.healthcare.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class FileStorageService {

    @Value("${upload.path:/uploads/profile/}")
    private String baseUploadDir;

    public String saveFile(MultipartFile file) {
        try {

            Path uploadPath = Paths.get(baseUploadDir, "profile");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            // Copy file to target location
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File saved: {}", filePath.toAbsolutePath());
            return "/uploads/profile/" + filename;

        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
