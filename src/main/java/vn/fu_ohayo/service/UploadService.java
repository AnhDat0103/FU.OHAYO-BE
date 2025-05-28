package vn.fu_ohayo.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String handleUploadFile(MultipartFile file, String targetFolder);
}
