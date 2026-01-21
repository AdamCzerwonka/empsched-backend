package com.example.empsched.employee.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StorageService {
    String uploadProfilePicture(final MultipartFile file, final UUID userId, final String fileType);

    byte[] downloadFile(final String fileName);

    void deleteFile(final String fileName);
}
