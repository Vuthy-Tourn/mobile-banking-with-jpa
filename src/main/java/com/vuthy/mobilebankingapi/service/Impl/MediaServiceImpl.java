package com.vuthy.mobilebankingapi.service.Impl;

import com.vuthy.mobilebankingapi.domain.Media;
import com.vuthy.mobilebankingapi.dto.MediaResponse;
import com.vuthy.mobilebankingapi.repository.MediaRepository;
import com.vuthy.mobilebankingapi.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final String name = UUID.randomUUID().toString();

    @Value("${media.server-path}")
    private String serverPath;

    @Value("${media.base-uri}")
    private String baseUri;

    @Override
    public MediaResponse upload(MultipartFile file) {

        int lastIndexOf = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");

        String extension = file.getOriginalFilename().substring(lastIndexOf + 1);

        // create path object
        Path path = Paths.get(serverPath + String.format("%s.%s", name, extension));

        try {
            Files.copy(file.getInputStream(),path);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "media upload failed");
        }

        Media media = new Media();
        media.setName(name);
        media.setExtension(extension);
        media.setMimeType(file.getContentType());
        media.setIsDeleted(false);

        media = mediaRepository.save(media);

        return MediaResponse.builder()
                .name(media.getName())
                .mimeType(media.getMimeType())
                .size(file.getSize())
                .uri(baseUri + String.format("%s.%s", name, extension))
                .build();
    }

    @Override
    public List<MediaResponse> uploadMultiple(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No files provided");
        }

        return files.stream()
                .map(file -> {
                    String originalFilename = file.getOriginalFilename();
                    if (file.isEmpty()) {
                        return new MediaResponse(originalFilename,file.getContentType(), "File is empty", file.getSize());
                    }

                    int lastIndexOf = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");

                    String extension = file.getOriginalFilename().substring(lastIndexOf + 1);

                    Path path = Paths.get(serverPath + String.format("%s.%s", name, extension));

                    try {
                        Files.copy(file.getInputStream(),path);
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "media upload failed");
                    }

                    Media media = new Media();
                    media.setName(name);
                    media.setExtension(extension);
                    media.setMimeType(file.getContentType());
                    media.setIsDeleted(false);

                    media = mediaRepository.save(media);

                    return MediaResponse.builder()
                            .name(media.getName())
                            .mimeType(media.getMimeType())
                            .size(file.getSize())
                            .uri(baseUri + String.format("%s.%s", name, extension))
                            .build();

                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Resource> downloadFileByName(String fileName) {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filename cannot be empty");
        }


        Media media = mediaRepository.findByName(fileName.split("\\.")[0])
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        // 1. Get the file path
        Path filePath = Paths.get(serverPath).resolve(fileName).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // 2. Check if file exists
        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }

        // 3. download
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .body(resource);
    }

    @Override
    public void deleteFileByName(String fileName) {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filename cannot be empty");
        }


        Media media = mediaRepository.findByName(fileName.split("\\.")[0])
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        try {
            // 1. Secure path resolution
            Path filePath = Paths.get(serverPath).resolve(fileName).normalize();

            // 2. Delete file from filesystem
            Files.delete(filePath);

            // 3. Update database (if using Media entity)
             media.setIsDeleted(true);
             mediaRepository.save(media);

        } catch (IOException e) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "media delete failed");
        }
    }
}
