package com.vuthy.mobilebankingapi.controller;

import com.vuthy.mobilebankingapi.dto.MediaResponse;
import com.vuthy.mobilebankingapi.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/medias")
public class MediaController {

    private final MediaService mediaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MediaResponse upload(@RequestBody MultipartFile file) {
        return mediaService.upload(file);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/files")
    public List<MediaResponse> uploadMultiple(@RequestBody List<MultipartFile> files) {
        return mediaService.uploadMultiple(files);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName){
       return mediaService.downloadFileByName(fileName);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{fileName}")
    public void deleteFile(@PathVariable String fileName){
        mediaService.deleteFileByName(fileName);
    }

}
