package com.jerry.project.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveFile(MultipartFile file);

    String saveFile(MultipartFile file, String oldFilePath);

    void deleteFile(String path);
}
