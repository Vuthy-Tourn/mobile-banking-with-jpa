package com.vuthy.mobilebankingapi.service;


import com.vuthy.mobilebankingapi.dto.MediaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    MediaResponse upload(MultipartFile file);
}
