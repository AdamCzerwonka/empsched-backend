package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.exception.FileNotFoundException;
import com.example.empsched.employee.exception.FileParsingFailedException;
import com.example.empsched.employee.service.StorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class StorageServiceImpl implements StorageService {
    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Override
    public String uploadProfilePicture(final MultipartFile file, final UUID userId, final String fileType) {
        final String blobName = "profile_pictures/" + userId + "." + fileType;
        try {
            final BlobId blobId = BlobId.of(bucketName, blobName);
            final BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, file.getBytes());
            return blobName;
        } catch (IOException e) {
            throw new FileParsingFailedException(blobName);
        }
    }

    @Override
    public byte[] downloadFile(final String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);

        if (blob == null || !blob.exists()) {
            throw new FileNotFoundException(fileName);
        }
        return blob.getContent();
    }

    @Override
    public void deleteFile(String fileName) {
        final BlobId blobId = BlobId.of(bucketName, fileName);
        storage.delete(blobId);
    }
}
