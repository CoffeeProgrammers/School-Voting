package com.project.backend.services.impl;

import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.models.File;
import com.project.backend.repositories.FileRepository;
import com.project.backend.services.inter.FileService;
import com.project.backend.services.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserService userService;
    private final SupabaseStorageService supabaseStorageService;


    @Override
    public File save(MultipartFile file, long ownerId, String frontendHash)
            throws IOException, NoSuchAlgorithmException {

        String computedHash = chackHash(file, ownerId, frontendHash);
        boolean exists = this.existsByFileHashAndUser_Id(computedHash, ownerId);
        String originalFilename = file.getOriginalFilename();
        String realFileName = createName(originalFilename);
        String publicUrl;

        if (exists) {
            log.info("Service: Using existing file for {}", originalFilename);
            publicUrl = fileRepository.findByFileHashAndUser_Id(computedHash, ownerId)
                    .get(0).getPath();
        } else {
            log.info("Service: Saving new file {}", originalFilename);
            publicUrl = supabaseStorageService.uploadFile(realFileName, file.getBytes(), file.getContentType());
        }

        File entity = new File();
        entity.setFileName(realFileName);
        entity.setFileRealName(originalFilename);
        entity.setFileHash(computedHash);
        entity.setUser(userService.findById(ownerId));
        entity.setFileType(file.getContentType());
        entity.setFileSize(String.valueOf(file.getSize()));
        entity.setPath(publicUrl);

        return fileRepository.save(entity);
    }

    private String chackHash(MultipartFile file, long ownerId, String frontendHash) throws IOException, NoSuchAlgorithmException {
        log.info("Service: Save file with id {}", file.getOriginalFilename());
        byte[] bytes = file.getBytes();
        String computedHash = sha256(bytes);

        if (!computedHash.equalsIgnoreCase(frontendHash)) {
            log.error("Service: Hash of saved file is incorrect");
            throw new IllegalArgumentException("Hash mismatch");
        }

        return computedHash;
    }

    private String createName(String originalName) {
        String realFileName = originalName;
        String pre_UUID = String.valueOf(UUID.randomUUID());

        realFileName = Normalizer.normalize(Objects.requireNonNull(realFileName), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        realFileName = realFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        return pre_UUID + realFileName;
    }

    @Override
    public void delete(long id) {
        log.info("Service: Delete file with id {}", id);
        File file = fileRepository.findById(id).orElseThrow();
        String filename = file.getFileName();
        log.info("Service: Delete file with id {}", filename);
        supabaseStorageService.deleteFile(filename);
        fileRepository.deleteById(id);
    }

    @Override
    public boolean existsByFileHashAndUser_Id(String fileHash, Long userId) {
        log.info("Service: Exists file with hash {} and user id {}", fileHash, userId);
        return !(fileRepository.findByFileHashAndUser_Id(fileHash, userId).isEmpty());
    }

    private String sha256(byte[] bytes) throws NoSuchAlgorithmException {
        log.info("Service: Sha256 hash <UNK> <UNK> <UNK> <UNK> <UNK>");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }


}
