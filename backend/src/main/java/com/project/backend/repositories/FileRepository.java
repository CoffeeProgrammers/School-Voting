package com.project.backend.repositories;

import com.project.backend.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File,Long> {
    List<File> findByFileHashAndUser_Id(String fileHash, Long userId);
}
