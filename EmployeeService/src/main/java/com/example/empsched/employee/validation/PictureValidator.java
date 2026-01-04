package com.example.empsched.employee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PictureValidator implements ConstraintValidator<ValidPicture, MultipartFile> {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/bmp",
            "image/webp",
            "image/gif",
            "image/svg+xml"
    );

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            ".jpg", ".jpeg", ".png", ".bmp", ".webp", ".gif", ".svg"
    );

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        final String fileContentType = value.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(fileContentType)) {
            return false;
        }

        String filename = value.getOriginalFilename();
        if (filename == null || !hasValidExtension(filename)) {
            return false;
        }

        return true;
    }

    private boolean hasValidExtension(String filename) {
        String lowerCaseName = filename.toLowerCase();
        return ALLOWED_EXTENSIONS.stream().anyMatch(lowerCaseName::endsWith);
    }
}
