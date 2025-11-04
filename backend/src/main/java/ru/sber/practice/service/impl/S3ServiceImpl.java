package ru.sber.practice.service.impl;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sber.practice.service.S3Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final MinioClient minioClient;
    private final String bucketName = "mathchat";

    @PostConstruct
    private void init() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("Bucket '{}' created successfully", bucketName);
            } else {
                log.info("Bucket '{}' already exists", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to create bucket '{}': {}", bucketName, e.getMessage());
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(key)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        return key;
    }

    @Override
    public byte[] downloadFile(String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(key)
                        .build()
        );
        return stream.readAllBytes();
    }
}
