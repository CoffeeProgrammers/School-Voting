package com.project.backend.controllers;

import com.project.backend.dto.file.FileResponse;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.FileMapper;
import com.project.backend.models.File;
import com.project.backend.services.inter.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse upload(
            @RequestParam MultipartFile file,
            @RequestParam long ownerId,
            @RequestParam String frontendHash) throws IOException, NoSuchAlgorithmException {
        log.info("Controller: upload file");
        return fileMapper.fromFileToResponse(fileService.save(file, ownerId, frontendHash));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.info("Controller: delete file with id: {}", id);
        fileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
