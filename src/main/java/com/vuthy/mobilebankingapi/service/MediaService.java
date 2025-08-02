package com.vuthy.mobilebankingapi.service;


import com.vuthy.mobilebankingapi.dto.MediaResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    MediaResponse upload(MultipartFile file);

    List<MediaResponse> uploadMultiple(List<MultipartFile> files);

    ResponseEntity<Resource> downloadFileByName(String fileName);

    void deleteFileByName(String fileName);
}
