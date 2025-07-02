package com.project.backend.services.inter;

import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.models.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface FileService {
    File save(MultipartFile file, long ownerId, String frontendHash) throws IOException, NoSuchAlgorithmException;
    void delete(long id);
    boolean existsByFileHashAndUser_Id(String fileHash, Long userId);
}
