package com.project.backend.services.inter;

public interface SupabaseStorageService {
    String uploadFile(String fileName, byte[] fileBytes, String contentType);
    void deleteFile(String fileName);
    String getPublicUrl(String fileName);
}

