package com.project.backend.services.impl;

import com.project.backend.services.inter.SupabaseStorageService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupabaseStorageServiceImpl implements SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Override
    public String uploadFile(String fileName, byte[] fileBytes, String contentType) {
        log.info("Service: Upload to cloud file with name {}", fileName);

        HttpResponse<String> response = Unirest.put(supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName)
                .header("apikey", supabaseKey)
                .header("Authorization", "Bearer " + supabaseKey)
                .header("Content-Type", contentType)
                .body(fileBytes)
                .asString();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Upload failed: " + response.getBody());
        }

        return getPublicUrl(fileName);
    }

    @Override
    public void deleteFile(String fileName) {
        log.info("Service: Delete file with name {}", fileName);
        HttpResponse<String> response = Unirest.delete(supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName)
                .header("apikey", supabaseKey)
                .header("Authorization", "Bearer " + supabaseKey)
                .asString();

        response.getStatus();
    }

    @Override
    public String getPublicUrl(String fileName) {
        log.info("Service: Get public url with name {}", fileName);
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
    }
}

