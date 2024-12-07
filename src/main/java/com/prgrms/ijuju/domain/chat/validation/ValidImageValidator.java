package com.prgrms.ijuju.domain.chat.validation;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
        "image/jpeg", "image/png", "image/gif"
    );
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        
        return ALLOWED_MIME_TYPES.contains(file.getContentType()) && 
               file.getSize() <= MAX_SIZE;
    }
} 
